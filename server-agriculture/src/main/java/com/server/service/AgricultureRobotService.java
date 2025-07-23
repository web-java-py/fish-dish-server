package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureRobot;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/7/23 16:55
 * 小农机器人聊天记录表
 */
public interface AgricultureRobotService extends IService<AgricultureRobot> {

    /**
     * 查询小农机器人历史聊天记录
     * @param agricultureRobot
     * @return
     */
    List<AgricultureRobot> getRobotList(AgricultureRobot agricultureRobot);

    /**
     * 新增聊天对话信息
     * @param agricultureRobot
     * @return
     */
    int addRobot(AgricultureRobot agricultureRobot);
}
