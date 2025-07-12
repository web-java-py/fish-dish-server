package com.server.controller.agriculture;

import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.task.AgricultureRecent10DaysImportTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 农业最近10天数据导入Controller
 *
 * @author server
 */
@RestController
@RequestMapping("/agriculture/recent10days")
public class AgricultureRecent10DaysController extends BaseController {

    @Autowired
    private AgricultureRecent10DaysImportTask recent10DaysImportTask;

    /**
     * 导入最近10天的所有统计数据（日统计+小时统计）
     */
    @PreAuthorize("@ss.hasPermi('agriculture:stats:import')")
    @PostMapping("/import/all")
    public AjaxResult importAllRecent10DaysData() {
        try {
            // 异步执行，避免接口超时
            new Thread(() -> {
                recent10DaysImportTask.importRecent10DaysData();
            }).start();

            return success("最近10天数据导入任务已启动，请查看日志了解进度");
        } catch (Exception e) {
            return error("启动最近10天数据导入任务失败：" + e.getMessage());
        }
    }

    /**
     * 只导入最近10天的日统计数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:stats:import')")
    @PostMapping("/import/daily")
    public AjaxResult importDailyStatsOnly() {
        try {
            // 异步执行，避免接口超时
            new Thread(() -> {
                recent10DaysImportTask.importDailyStatsOnly();
            }).start();

            return success("最近10天日统计数据导入任务已启动");
        } catch (Exception e) {
            return error("启动最近10天日统计数据导入任务失败：" + e.getMessage());
        }
    }

    /**
     * 只导入最近10天的小时统计数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:stats:import')")
    @PostMapping("/import/hourly")
    public AjaxResult importHourlyStatsOnly() {
        try {
            // 异步执行，避免接口超时
            new Thread(() -> {
                recent10DaysImportTask.importHourlyStatsOnly();
            }).start();

            return success("最近10天小时统计数据导入任务已启动");
        } catch (Exception e) {
            return error("启动最近10天小时统计数据导入任务失败：" + e.getMessage());
        }
    }
}