package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.mapper.AgriculturePastureMapper;
import com.server.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.domain.AgriculturePasture;
import com.server.service.AgriculturePastureService;

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
        return list();
    }

    /**
     * 新增大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture) {
        agriculturePasture.setCreateTime(LocalDateTime.now());
        agriculturePasture.setCreateBy(SecurityUtils.getUsername());
        agriculturePasture.setUpdateBy(SecurityUtils.getUsername());
        int insert = agriculturePastureMapper.insert(agriculturePasture);
        /* 异步上链区块操作 操作在mapper之前 需要拿到大棚响应回来的id */
        rabbitTemplate.convertAndSend(FB_EXCHANGE, FB_AP_KEY, agriculturePasture);
        return insert;
    }

    /**
     * 修改大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture) {
        agriculturePasture.setUpdateTime(LocalDateTime.now());
        int update = agriculturePastureMapper.updateById(agriculturePasture);
        rabbitTemplate.convertAndSend(FB_EXCHANGE, FB_AP_KEY, agriculturePasture);
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
}
