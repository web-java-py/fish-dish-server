package com.server.controller.agriculture;

import com.server.core.domain.AjaxResult;
import com.server.service.AgricultureConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agriculture/console")
@Api(tags = "控制台")
public class AgricultureConsoleController {

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    /**
     * 获取农场数据
     *
     * @return
     */
    @GetMapping("/agriculture")
    @ApiOperation("获取农场数据")
    public AjaxResult getAgriculture() {
        return AjaxResult.success(agricultureConsoleService.listAgriculture());
    }

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

    /**
     * 获取溯源数据
     */
    @GetMapping("/traceTotal")
    @ApiOperation("获取溯源数据")
    public AjaxResult getTraceTotal() {
        return AjaxResult.success(agricultureConsoleService.listTraceTotal());
    }
}
