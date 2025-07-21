package com.server.controller.app;

import com.server.core.domain.AjaxResult;
import com.server.service.AgricultureConsoleService;
import com.server.service.AppConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/console")
@Api(tags = "控制台")
public class AppConsoleController {

    @Autowired
    private AppConsoleService appConsoleService;

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    /**
     * 获取分区
     *
     * @return
     */
    @GetMapping("/batch")
    @ApiOperation("获取分区")
    public AjaxResult getBatch() {
        return AjaxResult.success(appConsoleService.listBatch());
    }

    /**
     * 获取分区详情
     * @param id
     * @return
     */
    @GetMapping("/batch/{id}")
    @ApiOperation("获取分区详情")
    public AjaxResult getBatchInfo(@PathVariable Long id) {
        return AjaxResult.success(appConsoleService.batchInfo(id));
    }

    /**
     * 获取大棚（选择框）
     *
     * @return
     */
    @GetMapping("/pastureDrop")
    @ApiOperation("获取大棚（选择框）")
    public AjaxResult getPastureDrop() {
        return AjaxResult.success(appConsoleService.listPastureNameMap());
    }

    /**
     * 获取任务统计
     *
     * @return
     */
    @GetMapping("/batchTaskTotal")
    @ApiOperation("获取任务统计")
    public AjaxResult getBatchTaskTotal() {
        return AjaxResult.success(agricultureConsoleService.listBatchTask());
    }

    /**
     * 获取任务
     *
     * @return
     */
    @GetMapping("/batchTask")
    @ApiOperation("获取任务")
    public AjaxResult getBatchTask() {
        return AjaxResult.success(appConsoleService.listBatchTask());
    }
}