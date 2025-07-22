package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.AgricultureBatchTask;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePasture;
import com.server.domain.vo.BatchInfoVO;
import com.server.domain.vo.BatchTaskVO;
import com.server.domain.vo.BatchVO;
import com.server.mapper.AgricultureBatchTaskMapper;
import com.server.mapper.AgricultureClassMapper;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.mapper.AgriculturePastureMapper;
import com.server.service.AppConsoleService;
import com.server.utils.bean.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppConsoleServiceImpl implements AppConsoleService {

    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    @Autowired
    private AgricultureBatchTaskMapper agricultureBatchTaskMapper;

    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    /**
     * 获取分区
     *
     * @return
     */
    @Override
    public List<BatchVO> listBatch() {
        List<AgricultureCropBatch> agricultureCropBatches = agricultureCropBatchMapper.selectList(null);
        List<BatchVO> result = new ArrayList<BatchVO>();
        agricultureCropBatches.forEach(item -> {
            result.add(getBatchVO(item));
        });
        return result;
    }

    /**
     * 获取分区详情
     *
     * @param id
     * @return
     */
    @Override
    public BatchInfoVO batchInfo(Long id) {
        LambdaQueryWrapper<AgricultureCropBatch> lambda = new QueryWrapper<AgricultureCropBatch>().lambda();
        lambda.eq(AgricultureCropBatch::getBatchId, id);
        AgricultureCropBatch agricultureCropBatch = agricultureCropBatchMapper.selectOne(lambda);
        List<AgricultureClass> agricultureClasses = agricultureClassMapper.selectList(classLambda(agricultureCropBatch));
        String[] classNames = agricultureClasses.stream().map(AgricultureClass::getClassName).toArray(String[]::new);
        BatchVO batchVO = getBatchVO(agricultureCropBatch);
        LambdaQueryWrapper<AgricultureBatchTask> batchTaskLambda = new QueryWrapper<AgricultureBatchTask>().lambda();
        batchTaskLambda
                .eq(AgricultureBatchTask::getBatchId, agricultureCropBatch.getBatchId())
                .orderByAsc(AgricultureBatchTask::getPlanStart);
        List<AgricultureBatchTask> agricultureBatchTaskList = agricultureBatchTaskMapper.selectList(batchTaskLambda);
        return BatchInfoVO.builder()
                .batchId(id)
                .way("室内")
                .className(classNames)
                .area(agricultureCropBatch.getFishArea() + agricultureCropBatch.getCropArea())
                .batchVO(batchVO)
                .agricultureBatchTaskList(agricultureBatchTaskList)
                .build();
    }

    /**
     * 获取大棚（选择框）
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> listPastureNameMap() {
        List<AgriculturePasture> agriculturePastures = agriculturePastureMapper.selectList(null);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        agriculturePastures.forEach(ag -> {
            Map<String, Object> hasMap = new HashMap<String, Object>();
            hasMap.put("label", ag.getName());
            hasMap.put("value", ag.getId());
            result.add(hasMap);
        });
        return result;
    }

    /**
     * 获取任务
     *
     * @return
     */
    @Override
    public List<BatchTaskVO> listBatchTask() {
        List<BatchTaskVO> result = new ArrayList<BatchTaskVO>();
        List<AgricultureBatchTask> agricultureBatchTaskList = agricultureBatchTaskMapper.selectList(null);
        agricultureBatchTaskList.forEach(item -> {
            LambdaQueryWrapper<AgricultureCropBatch> lambda = new QueryWrapper<AgricultureCropBatch>().lambda();
            lambda.eq(AgricultureCropBatch::getBatchId, item.getBatchId());
            AgricultureCropBatch agricultureCropBatch = agricultureCropBatchMapper.selectOne(lambda);
            LambdaQueryWrapper<AgriculturePasture> pastureLambda = new QueryWrapper<AgriculturePasture>().lambda();
            pastureLambda.eq(AgriculturePasture::getId, agricultureCropBatch.getPastureId());
            AgriculturePasture agriculturePasture = agriculturePastureMapper.selectOne(pastureLambda);
            result.add(
                    BatchTaskVO.builder()
                            .taskId(item.getTaskId())
                            .batchName(agricultureCropBatch.getBatchName())
                            .pastureName(agriculturePasture.getName())
                            .agricultureBatchTask(item)
                            .build()
            );
        });
        return result;
    }

    /**
     * 获取分区基本信息
     */
    private BatchVO getBatchVO(AgricultureCropBatch item) {
        BatchVO batchVO = new BatchVO();
        BeanUtils.copyBeanProp(batchVO, item);
        List<AgricultureClass> agricultureClasses = agricultureClassMapper.selectList(classLambda(item));
        batchVO.setImages(agricultureClasses.stream().map(AgricultureClass::getClassImage).toArray(String[]::new));
        LambdaQueryWrapper<AgricultureBatchTask> batchTaskLambda = new QueryWrapper<AgricultureBatchTask>().lambda();
        batchTaskLambda
                .eq(AgricultureBatchTask::getBatchId, item.getBatchId())
                .eq(AgricultureBatchTask::getStatus, "3")
                .orderByDesc(AgricultureBatchTask::getPlanStart);
        List<AgricultureBatchTask> agricultureBatchTasks = agricultureBatchTaskMapper.selectList(batchTaskLambda);
        long days = agricultureBatchTasks.stream()
                .map(task -> Duration.between(task.getPlanStart().toInstant(), task.getPlanFinish().toInstant()))
                .reduce(Duration.ZERO, Duration::plus)
                .toDays();
        batchVO.setDays(days);
        batchVO.setStage(agricultureBatchTasks.size() > 0 ? agricultureBatchTasks.get(0).getTaskName() : "暂无数据");
        LambdaQueryWrapper<AgriculturePasture> pastureLambda = new QueryWrapper<AgriculturePasture>().lambda();
        pastureLambda.eq(AgriculturePasture::getId, item.getPastureId());
        batchVO.setPastureName(agriculturePastureMapper.selectOne(pastureLambda).getName());
        return batchVO;
    }

    private LambdaQueryWrapper<AgricultureClass> classLambda(AgricultureCropBatch item) {
        return new QueryWrapper<AgricultureClass>().lambda()
                .eq(ObjectUtils.isNotEmpty(item.getGermplasmId()), AgricultureClass::getClassId, item.getGermplasmId())
                .or()
                .eq(ObjectUtils.isNotEmpty(item.getVegetableId()), AgricultureClass::getClassId, item.getVegetableId());
    }
}
