package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.service.AgricultureTraceabilityLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agriculture/traceabilityLog")
public class AgricultureTraceabilityLogController extends BaseController {

    @Autowired
    private AgricultureTraceabilityLogService traceabilityLogService;

    /**
     * 获取溯源统计信息
     */
    @ApiOperation("获取溯源统计信息")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:stats')")
    @GetMapping("/stats")
    public AjaxResult getTraceabilityStats() {
        Map<String, Object> stats = traceabilityLogService.getTraceabilityStats();
        return success(stats);
    }

    /**
     * 获取每个溯源码的统计信息
     */
    @ApiOperation("获取每个溯源码的统计信息")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:codeStats')")
    @GetMapping("/codeStats")
    public AjaxResult getTraceabilityCodeStats() {
        List<Map<String, Object>> stats = traceabilityLogService.getTraceabilityCodeStats();
        return success(stats);
    }

    /**
     * 获取按分区的统计信息
     */
    @ApiOperation("获取按分区的统计信息")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:partitionStats')")
    @GetMapping("/partitionStats")
    public AjaxResult getPartitionTraceabilityStats() {
        List<Map<String, Object>> stats = traceabilityLogService.getPartitionTraceabilityStats();
        return success(stats);
    }

    /**
     * 根据溯源码查询该溯源码的查询次数
     */
    @ApiOperation("根据溯源码查询该溯源码的查询次数")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:count')")
    @GetMapping("/count/{traceCode}")
    public AjaxResult getTraceabilityCountByCode(@PathVariable("traceCode") String traceCode) {
        Long count = traceabilityLogService.getTraceabilityCountByCode(traceCode);
        return success(count);
    }

    /**
     * 根据溯源码查询该溯源码的详细信息（包括查询次数）
     */
    @ApiOperation("根据溯源码查询该溯源码的详细信息")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:detail')")
    @GetMapping("/detail/{traceCode}")
    public AjaxResult getTraceabilityDetailByCode(@PathVariable("traceCode") String traceCode) {
        Map<String, Object> detail = traceabilityLogService.getTraceabilityDetailByCode(traceCode);
        return success(detail);
    }

    /**
     * 根据溯源码查询该溯源码的查询记录列表
     */
    @ApiOperation("根据溯源码查询该溯源码的查询记录列表")
    @PreAuthorize("@ss.hasPermi('traceabilityLog:logs')")
    @GetMapping("/logs/{traceCode}")
    public AjaxResult getTraceabilityLogsByCode(@PathVariable("traceCode") String traceCode) {
        List<Map<String, Object>> logs = traceabilityLogService.getTraceabilityLogsByCode(traceCode);
        return success(logs);
    }
}