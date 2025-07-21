package com.server.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgricultureCropBatchDTO;
import com.server.mapper.AgricultureJobMapper;
import com.server.mapper.AgriculturePastureMapper;
import com.server.service.AgricultureBatchTaskService;
import com.server.service.AgricultureJobService;
import com.server.service.AgricultureTaskLogService;
import com.server.utils.DateUtils;
import com.server.utils.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.service.AgricultureCropBatchService;

import static com.server.constant.RabbitMQConstant.FB_EXCHANGE;

/**
 * 分区Service业务层处理
 *
 * @author server
 * @date 2025-05-28
 */
@Service
public class AgricultureCropBatchServiceImpl extends ServiceImpl<AgricultureCropBatchMapper, AgricultureCropBatch> implements AgricultureCropBatchService
{
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;
    @Autowired
    private AgricultureJobService agricultureJobService;
    @Autowired
    private AgricultureBatchTaskService agricultureBatchTaskService;
    @Autowired
    private AgricultureTaskLogService agricultureTaskLogService;
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询分区
     *
     * @param batchId 分区主键
     * @return 分区
     */
    @Override
    public AgricultureCropBatch selectAgricultureCropBatchByBatchId(Long batchId)
    {
        return getById(batchId);
    }

    /**
     * 根据大棚ID查询分区列表
     *
     * @param pastureId 大棚ID
     * @return 分区列表
     */
    @Override
    public List<AgricultureCropBatchDTO> selectBatchByPastureId(Long pastureId) {
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureCropBatch::getPastureId, pastureId);
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }

    /**
     * 导出分区列表
     *
     * @param agricultureCropBatch 分区
     * @return 分区
     */
    @Override
    public List<AgricultureCropBatch> selectAgricultureCropBatchList(AgricultureCropBatch agricultureCropBatch)
    {
        return list();
    }

    /**
     * 新增分区
     *
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    @Override
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        // 获取大棚合约地址
        AgriculturePasture agriculturePasture = agriculturePastureMapper.selectById(agricultureCropBatch.getPastureId());
        agricultureCropBatch.setContractAddr(agriculturePasture.getContractAddr());
        // 先插入分区，以获取自增主键
        int result = agricultureCropBatchMapper.insert(agricultureCropBatch);
        // 成功插入后，agricultureCropBatch.getBatchId() 将会持有数据库生成的ID
        if (result > 0) {
            // 1. 查询鱼的标准作业
            List<AgricultureJob> fishJobList = null;
            if (agricultureCropBatch.getGermplasmId() != null) {
                AgricultureJob fishJob = new AgricultureJob();
                fishJob.setClassId(agricultureCropBatch.getGermplasmId());
                fishJobList = agricultureJobService.selectAgricultureJobList(fishJob);
            }

            // 2. 查询菜的标准作业
            List<AgricultureJob> vegJobList = null;
            if (agricultureCropBatch.getVegetableId() != null) {
                AgricultureJob vegJob = new AgricultureJob();
                vegJob.setClassId(agricultureCropBatch.getVegetableId());
                vegJobList = agricultureJobService.selectAgricultureJobList(vegJob);
            }

            // 3. 合并两个作业列表
            List<AgricultureJob> sjList = new ArrayList<>();
            if (fishJobList != null) sjList.addAll(fishJobList);
            if (vegJobList != null) sjList.addAll(vegJobList);

            // 4. 去重（如果有必要，可以根据jobId去重）
            sjList = sjList.stream().distinct().collect(Collectors.toList());

            // 5. 遍历插入任务（保持你原有的插入逻辑）
            for (AgricultureJob sj : sjList) {
                AgricultureBatchTask agricultureBatchTask = new AgricultureBatchTask();
                //设置批次任务的相关信息
                agricultureBatchTask.setBatchId(agricultureCropBatch.getBatchId());
                agricultureBatchTask.setResponsiblePersonId(agricultureCropBatch.getResponsiblePersonId());
                agricultureBatchTask.setTaskName(sj.getJobName());
                // 新增：设置fishDish字段，鱼为0，菜为1
                if (agricultureCropBatch.getGermplasmId() != null && sj.getClassId() != null && sj.getClassId().equals(agricultureCropBatch.getGermplasmId())) {
                    agricultureBatchTask.setFishDish(0L); // 鱼
                } else if (agricultureCropBatch.getVegetableId() != null && sj.getClassId() != null && sj.getClassId().equals(agricultureCropBatch.getVegetableId())) {
                    agricultureBatchTask.setFishDish(1L); // 菜
                }
                // 根据周期单位设置乘数（0:天, 1:周）
                int mult = sj.getCycleUnit().equals("0")? 1 : 7;
                LocalDateTime startTime = agricultureCropBatch.getStartTime();
                // 如果批次开始时间为空，则默认为当前时间
                if (startTime == null) {
                    startTime = LocalDateTime.now();
                }
                // 计算计划开始时间
                LocalDateTime planStartDateTime = startTime.plusDays(sj.getJobStart() * mult);
                agricultureBatchTask.setPlanStart(Date.from(planStartDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                // 计算计划完成时间
                LocalDateTime planFinishDateTime = startTime.plusDays(sj.getJobFinish() * mult);
                agricultureBatchTask.setPlanFinish(Date.from(planFinishDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                // 插入批次任务到数据库
                agricultureBatchTaskService.insertBatchTask(agricultureBatchTask);
                //创建任务日志
                AgricultureTaskLog agricultureTaskLog = new AgricultureTaskLog();
                agricultureTaskLog.setLogId(agricultureBatchTask.getTaskId());
                agricultureTaskLog.setOperDes("创建任务");  // 操作描述
                // 插入任务日志到数据库
                agricultureTaskLogService.insertAgricultureTaskLog(agricultureTaskLog);
            }
        }
        //  异步处理分区上链操作 清空实体合约地址
        agriculturePasture.setContractAddr(null);
        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agricultureCropBatch);
        // 返回插入作物批次的结果
        return result;
    }

    /**
     * 修改分区
     *
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    @Override
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        // 获取大棚合约地址
        AgriculturePasture agriculturePasture = agriculturePastureMapper.selectById(agricultureCropBatch.getPastureId());
        agricultureCropBatch.setContractAddr(agriculturePasture.getContractAddr());
        int update = agricultureCropBatchMapper.updateById(agricultureCropBatch);
        //  异步处理分区上链操作
        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agricultureCropBatch);
        return update;
    }

    /**
     * 批量删除分区
     *删除的同时将分区下的任务一起删掉，避免数据库过多不用的数据
     * @param batchId 需要删除的分区主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchId)
    {
        LambdaQueryWrapper<AgricultureBatchTask> queryWrapper = new LambdaQueryWrapper<>();
        //传给batch_id的参数是一个序列化的Java对象 如 Long[] 或 List<Long>）而不是单个 Long 类型的分区ID
        queryWrapper.in(AgricultureBatchTask::getBatchId, Arrays.asList(batchId));
        //AgricultureBatchTask的batchId 和 AgricultureCropBatch的batchId相等删除之后再删除分区
        agricultureBatchTaskService.remove(queryWrapper);
        return removeByIds(Arrays.asList(batchId)) ? batchId.length : 0;
    }

    /**
     * 删除分区信息
     *
     * @param batchId 分区主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCropBatchByBatchId(Long batchId)
    {
        return removeById(batchId) ? 1 : 0;
    }

    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     *
     * @param agricultureCropBatchDTO 包含查询条件的DTO，如批次名称batchName
     * @return 符合条件的作物批次DTO列表
     */
    @Override
    public List<AgricultureCropBatchDTO> getCropBatchWithClassImages(AgricultureCropBatchDTO agricultureCropBatchDTO) {
        // 创建LambdaQueryWrapper，用于构建查询条件，基于数据库实体类AgricultureCropBatch
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        // 如果DTO中的批次名称不为空，则添加批次名称的模糊查询条件
        if (StringUtils.isNotEmpty(agricultureCropBatchDTO.getBatchName())) {
            queryWrapper.like(AgricultureCropBatch::getBatchName, agricultureCropBatchDTO.getBatchName());
        }
        queryWrapper.like(StringUtils.isNotEmpty(agricultureCropBatchDTO.getClassId()),
                AgricultureCropBatch::getGermplasmId,agricultureCropBatchDTO.getClassId());
        // 调用Mapper方法执行查询，并将Wrapper作为参数传递，让Mapper根据条件拼接SQL
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }
}
