package com.server.controller.app;

import com.server.core.domain.AjaxResult;
import com.server.service.AgricultureConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/console")
@Api(tags = "控制台")
public class AppConsoleController {

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    /**
     * 获取任务数据
     *
     * @return
     */
    @GetMapping("/batchTask")
    @ApiOperation("获取任务数据")
    public AjaxResult getBatchTask() {
        return AjaxResult.success(agricultureConsoleService.listBatchTask());
    }
}
