package com.server.controller.agriculture;

import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureRobot;
import com.server.service.AgricultureRobotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zbb
 * @Date: 2025/7/23 16:53
 * 小农机器人聊天记录表
 */
@RestController
@RequestMapping("/agriculture/robot")
public class AgricultureRobotController extends BaseController {

    @Autowired
    private AgricultureRobotService agricultureRobotService;

    /**
     * 查询小农机器人历史聊天记录
     * @param agricultureRobot
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("小农机器人聊天记录")
    public TableDataInfo getRobot(AgricultureRobot agricultureRobot){
        startPage();
        return getDataTable(agricultureRobotService.getRobotList(agricultureRobot));
    }

    /**
     * 用户发送信息就存起来
     */
    @PostMapping()
    @ApiOperation("新增聊天对话信息")
    public AjaxResult addRobot(@RequestBody AgricultureRobot agricultureRobot){
        return toAjax(agricultureRobotService.addRobot(agricultureRobot));
    }
}
