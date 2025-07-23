package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureRobot;
import com.server.mapper.AgricultureRobotMapper;
import com.server.service.AgricultureRobotService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/7/23 16:56
 * 小农机器人聊天记录表
 */
@Service
public class AgricultureRobotServiceImpl extends ServiceImpl<AgricultureRobotMapper, AgricultureRobot> implements AgricultureRobotService {

    @Autowired
    private AgricultureRobotMapper agricultureRobotMapper;



    /**
     * 查询小农机器人历史聊天记录
     * @param agricultureRobot
     * @return
     */
    @Override
    public List<AgricultureRobot> getRobotList(AgricultureRobot agricultureRobot) {
        LambdaQueryWrapper<AgricultureRobot> lambdaQueryWrapper = new QueryWrapper<AgricultureRobot>().lambda();
        lambdaQueryWrapper.like(StringUtils.isNotNull(agricultureRobot.getUserId()),AgricultureRobot::getUserId,agricultureRobot.getUserId());
        return agricultureRobotMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增聊天对话信息
     * @param agricultureRobot
     * @return
     */
    @Override
    public int addRobot(AgricultureRobot agricultureRobot) {
        return agricultureRobotMapper.insert(agricultureRobot);
    }
}
