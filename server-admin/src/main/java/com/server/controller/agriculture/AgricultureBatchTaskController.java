package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureBatchTask;
import com.server.enums.BusinessType;
//import com.server.service.AgricultureBatchTaskService;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureBatchTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 批次任务Controller
 * @Author: zbb
 * @Date: 2025/5/28 22:18
 */
@RestController
@RequestMapping("/agriculture/batchTask")
public class AgricultureBatchTaskController extends BaseController {

    @Autowired
    private AgricultureBatchTaskService agricultureBatchTaskService;

    /**
     * 查询批次任务按钮列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batchTask:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureBatchTask agricultureBatchTask)
    {
        startPage();  // 开启分页
        return getDataTable(agricultureBatchTaskService.selectBatchTaskList(agricultureBatchTask));
    }
    /**
     * 获取批次任务按钮详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batchTask:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId){
        return AjaxResult.success(agricultureBatchTaskService.selectBatchTaskByTaskId(taskId));
    }

    /**
     * 删除批次任务
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batchTask:remove')")
    @Log(title = "分区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskId}")
    @SeeRefreshData
    public AjaxResult remove(@PathVariable Long taskId)
    {
        return toAjax(agricultureBatchTaskService.deleteAgricultureCropBatchByBatchIds(taskId));
    }
    /**
     * 修改批次任务
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batchTask:edit')")
    @Log(title = "批次任务", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.AGRICULTURE)
    public AjaxResult edit(@RequestBody AgricultureBatchTask agricultureBatchTask)
    {
        return toAjax(agricultureBatchTaskService.updateBatchTask(agricultureBatchTask));
    }
    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batchTask:add')")
    @Log(title = "批次任务", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData
    public AjaxResult add(@RequestBody AgricultureBatchTask agricultureBatchTask)
    {
        return toAjax(agricultureBatchTaskService.insertBatchTask(agricultureBatchTask));
    }

}
