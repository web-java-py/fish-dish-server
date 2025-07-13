package com.server.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.core.domain.AjaxResult;
import com.server.core.text.Convert;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.dto.AgriculturePastureDTO;
import com.server.exception.ServiceException;
import com.server.fisco.bcos.AgriculturePastureFB;
import com.server.mapper.AgriculturePastureMapper;
import com.server.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.domain.AgriculturePasture;
import com.server.service.AgriculturePastureService;
import com.server.service.AgricultureCropBatchService;

import static com.server.constant.RabbitMQConstant.*;

/**
 * 大棚Service业务层处理
 *
 * @author server
 * @date 2025-05-27
 */
@Service
public class AgriculturePastureServiceImpl extends ServiceImpl<AgriculturePastureMapper, AgriculturePasture> implements AgriculturePastureService {
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired(required = false)
    private Client client;

    @Value("${fisco.enabled}")
    private String fiscoEnabled;

    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询大棚
     *
     * @param id 大棚主键
     * @return 大棚
     */
    @Override
    public AgriculturePasture selectAgriculturePastureById(Long id) {
        return getById(id);
    }

    /**
     * 查询大棚列表
     *
     * @param agriculturePasture 大棚
     * @return 大棚
     */
    @Override
    public List<AgriculturePasture> selectAgriculturePastureList(AgriculturePasture agriculturePasture) {
        // 查询所有大棚
        List<AgriculturePasture> pastureList = list();
        // 遍历每个大棚
        for (AgriculturePasture pasture : pastureList) {
            // 统计该大棚下分区数量（pasture_id == id）
            int count = agricultureCropBatchService.count(
                new LambdaQueryWrapper<AgricultureCropBatch>()
                    .eq(AgricultureCropBatch::getPastureId, pasture.getId())
            );
            // 设置当前分区数量
            pasture.setBreedingQuantity((long) count);
            // 实时更新到数据库
            agriculturePastureMapper.updateById(pasture);
        }
        // 返回处理后的大棚列表
        return pastureList;
    }

    /**
     * 新增大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture) {
        validate(agriculturePasture);
        if (Convert.toBool(fiscoEnabled)){
            try {
                /* 部署合约 拿到合约地址 */
                AgriculturePastureFB agriculturePastureFB = AgriculturePastureFB.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
                /* 调用合约 响应交易信息 */
                agriculturePastureFB.createPasture(
                        agriculturePasture.getName(),
                        agriculturePasture.getAddress(),
                        agriculturePasture.getArea(),
                        BigInteger.valueOf(agriculturePasture.getBigBreedingQuantity()),
                        BigInteger.valueOf(0),
                        agriculturePasture.getDescription()
                );
                agriculturePasture.setContractAddr(agriculturePastureFB.getContractAddress());
            } catch (ContractException e) {
                log.error(e.getMessage());
            }
        }
        return agriculturePastureMapper.insert(agriculturePasture);
    }

    /**
     * 修改大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture) {
        validate(agriculturePasture);
        int update = agriculturePastureMapper.updateById(agriculturePasture);
        agriculturePasture.setUpdateBy(SecurityUtils.getUsername());
        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agriculturePasture);
        return update;
    }

    /**
     * 批量删除大棚
     *
     * @param ids 需要删除的大棚主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除大棚信息
     *
     * @param id 大棚主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 获取大棚剩余面积信息
     * <p>
     * 该方法会遍历所有大棚，统计每个大棚下所有分区的种植面积总和（cropArea），
     * 然后用大棚面积（area）减去分区总面积，得到剩余面积（remainingArea），
     * 并将这些信息封装到AgriculturePastureDTO列表中返回。
     * </p>
     * @param agriculturePastureDTO 查询参数（目前未用，可扩展筛选条件）
     * @return 每个大棚的面积、剩余面积、分区面积等信息的DTO列表
     */
    @Override
    public List<AgriculturePastureDTO> selectRemainingArea(AgriculturePastureDTO agriculturePastureDTO) {
        // 查询所有大棚信息
        List<AgriculturePasture> pastures = list();
        // 用于存放结果的DTO列表
        List<AgriculturePastureDTO> result = new java.util.ArrayList<>();
        // 遍历每个大棚，计算其剩余面积
        for (AgriculturePasture pasture : pastures) {
            // 查询该大棚下所有分区
            List<AgricultureCropBatch> cropBatches = agricultureCropBatchService.list(
                new LambdaQueryWrapper<AgricultureCropBatch>().eq(AgricultureCropBatch::getPastureId, pasture.getId())
            );
            // 统计所有分区的种植面积总和 ，将这个列表转换为一个流
            long totalCropArea = cropBatches.stream()
                    //filter方法用于筛选流中的元素
                .filter(cb -> cb.getCropArea() != null)
                    //mapToLong方法将流中的每个作物批次对象（cb）映射到一个long值并调用longValue()来将其转换为基本类型long
                .mapToLong(cb -> cb.getCropArea().longValue())
                .sum();
            // 统计所有分区的鱼塘面积总和
            long totalFishArea = cropBatches.stream()
                .filter(cb -> cb.getFishArea() != null)
                .mapToLong(cb -> cb.getFishArea().longValue())
                .sum();
            // area和remainingArea为字符串类型，需做类型转换
            long area = 0L;
            try {
                // 将大棚面积字符串转为long类型
                area = pasture.getArea() != null ? Long.parseLong(pasture.getArea()) : 0L;
            } catch (NumberFormatException e) {
                // 如果转换失败，默认面积为0
                area = 0L;
            }
            // 计算剩余面积 = 大棚面积 - crop_area 总和 - fish_area 总和
            long remaining = area - totalCropArea - totalFishArea;
            // 构建DTO对象，封装结果
            AgriculturePastureDTO dto = new AgriculturePastureDTO();
            dto.setId(pasture.getId()); // 设置大棚ID
            dto.setPasture_id(pasture.getId()); // 设置大棚ID（与id一致，便于关联）
            dto.setArea(pasture.getArea()); // 设置大棚面积
            dto.setRemaining_area(String.valueOf(remaining)); // 设置剩余面积
            dto.setCrop_area(totalCropArea); // 设置分区总面积
            dto.setBig_breeding_quantity(pasture.getBigBreedingQuantity()); // 设置最大分区数量
            dto.setBreeding_quantity(pasture.getBreedingQuantity()); // 设置当前分区数量
            dto.setFish_area(totalFishArea); // 设置鱼塘总面积
            // 添加到结果列表
            result.add(dto);
        }
        // 返回所有大棚的面积信息
        return result;
    }

    /**
     * 校验
     *
     * @param agriculturePasture
     */
    private void validate(AgriculturePasture agriculturePasture) {
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<AgriculturePasture> lambda = new QueryWrapper<AgriculturePasture>().lambda();
        lambda
                .eq(AgriculturePasture::getName, agriculturePasture.getName());
        AgriculturePasture info = agriculturePastureMapper.selectOne(lambda);
        if (!ObjectUtils.isEmpty(info) && info.getId() != agriculturePasture.getId()) {
            throw new ServiceException(AgricultureConstants.Pasture_NAME_EXIST);
        }
    }

}
