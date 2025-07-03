package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.mapper.AgricultureDeviceMqttConfigMapper;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureAutoControlStrategyMapper;
import com.server.domain.AgricultureAutoControlStrategy;
import com.server.service.AgricultureAutoControlStrategyService;

/**
 * 设备自动调节策略Service业务层处理
 * 
 * @author server
 * @date 2025-07-02
 */
@Service
public class AgricultureAutoControlStrategyServiceImpl extends ServiceImpl<AgricultureAutoControlStrategyMapper, AgricultureAutoControlStrategy> implements AgricultureAutoControlStrategyService
{
    @Autowired
    private AgricultureAutoControlStrategyMapper agricultureAutoControlStrategyMapper;

    /**
     * 查询设备自动调节策略
     * 
     * @param id 设备自动调节策略主键
     * @return 设备自动调节策略
     */
    @Override
    public AgricultureAutoControlStrategy selectAgricultureAutoControlStrategyById(Long id)
    {
        return getById(id);
    }




    /**
     * 查询设备自动调节策略列表
     * 如果所有查询条件（大棚ID、分区ID、设备ID、策略类型）都为空，则返回所有策略数据；
     * 否则根据传入的非空条件进行精确查询。
     *
     * @param agricultureAutoControlStrategy 查询条件封装对象
     * @return 设备自动调节策略列表
     */
    @Override
    public List<AgricultureAutoControlStrategy> selectAgricultureAutoControlStrategyList(AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        // 判断所有条件是否都为空（即无任何筛选条件）
        boolean allEmpty =
                (agricultureAutoControlStrategy.getPastureId() == null || agricultureAutoControlStrategy.getPastureId().isEmpty()) && // 大棚ID为空
                        (agricultureAutoControlStrategy.getBatchId() == null || agricultureAutoControlStrategy.getBatchId().isEmpty()) &&     // 分区ID为空
                        (agricultureAutoControlStrategy.getDeviceId() == null || agricultureAutoControlStrategy.getDeviceId().isEmpty()) &&   // 设备ID为空
                        (agricultureAutoControlStrategy.getStrategyType() == null || agricultureAutoControlStrategy.getStrategyType().isEmpty()); // 策略类型为空

        if (allEmpty) {
            // 没有任何筛选条件，查询全部策略
            return list();
        } else {
            // 构建查询条件
            LambdaQueryWrapper<AgricultureAutoControlStrategy> queryWrapper = new LambdaQueryWrapper<>();

            // 大棚ID不为空时，添加大棚ID等值查询条件
            if (agricultureAutoControlStrategy.getPastureId() != null && !agricultureAutoControlStrategy.getPastureId().isEmpty()) {
                queryWrapper.eq(AgricultureAutoControlStrategy::getPastureId, agricultureAutoControlStrategy.getPastureId());
            }
            // 分区ID不为空时，添加分区ID等值查询条件
            if (agricultureAutoControlStrategy.getBatchId() != null && !agricultureAutoControlStrategy.getBatchId().isEmpty()) {
                queryWrapper.eq(AgricultureAutoControlStrategy::getBatchId, agricultureAutoControlStrategy.getBatchId());
            }
            // 设备ID不为空时，添加设备ID等值查询条件
            if (agricultureAutoControlStrategy.getDeviceId() != null && !agricultureAutoControlStrategy.getDeviceId().isEmpty()) {
                queryWrapper.eq(AgricultureAutoControlStrategy::getDeviceId, agricultureAutoControlStrategy.getDeviceId());
            }
            // 策略类型不为空时，添加策略类型等值查询条件
            if (agricultureAutoControlStrategy.getStrategyType() != null && !agricultureAutoControlStrategy.getStrategyType().isEmpty()) {
                queryWrapper.eq(AgricultureAutoControlStrategy::getStrategyType, agricultureAutoControlStrategy.getStrategyType());
            }

            // 返回按条件查询的结果
            return list(queryWrapper);
        }
    }

    /**
     * 新增设备自动调节策略
     * 
     * @param agricultureAutoControlStrategy 设备自动调节策略
     * @return 结果
     */
    @Override
    public int insertAgricultureAutoControlStrategy(AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        agricultureAutoControlStrategy.setCreateTime(LocalDateTime.now());
        return agricultureAutoControlStrategyMapper.insert(agricultureAutoControlStrategy);
    }

    /**
     * 修改设备自动调节策略
     * 
     * @param agricultureAutoControlStrategy 设备自动调节策略
     * @return 结果
     */
    @Override
    public int updateAgricultureAutoControlStrategy(AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        agricultureAutoControlStrategy.setUpdateTime(LocalDateTime.now());
        return updateById(agricultureAutoControlStrategy)? 1 : 0;
    }

    /**
     * 批量删除设备自动调节策略
     * 
     * @param ids 需要删除的设备自动调节策略主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAutoControlStrategyByIds(Long[] ids)
    {
        return  removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除设备自动调节策略信息
     * 
     * @param id 设备自动调节策略主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAutoControlStrategyById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
