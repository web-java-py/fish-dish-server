package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgricultureTaskLog;
import com.server.service.AgricultureTaskLogService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 批次任务日志Controller
 *
 * @author server
 * @date 2025-06-11
 */
@RestController
@RequestMapping("/agriculture/log")
public class AgricultureTaskLogController extends BaseController
{
    @Autowired
    private AgricultureTaskLogService agricultureTaskLogService;

    /**
     * 查询批次任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureTaskLog agricultureTaskLog)
    {
        startPage();
        List<AgricultureTaskLog> list = agricultureTaskLogService.selectAgricultureTaskLogList(agricultureTaskLog);
        return getDataTable(list);
    }

    /**
     * 导出批次任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:export')")
    @Log(title = "批次任务日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureTaskLog agricultureTaskLog)
    {
        List<AgricultureTaskLog> list = agricultureTaskLogService.selectAgricultureTaskLogList(agricultureTaskLog);
        ExcelUtil<AgricultureTaskLog> util = new ExcelUtil<AgricultureTaskLog>(AgricultureTaskLog.class);
        util.exportExcel(response, list, "批次任务日志数据");
    }

    /**
     * 获取批次任务日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        return success(agricultureTaskLogService.selectAgricultureTaskLogByLogId(logId));
    }

    /**
     * 新增批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:add')")
    @Log(title = "批次任务日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureTaskLog agricultureTaskLog)
    {
        return toAjax(agricultureTaskLogService.insertAgricultureTaskLog(agricultureTaskLog));
    }

    /**
     * 修改批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:edit')")
    @Log(title = "批次任务日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureTaskLog agricultureTaskLog)
    {
        return toAjax(agricultureTaskLogService.updateAgricultureTaskLog(agricultureTaskLog));
    }

    /**
     * 删除批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:remove')")
    @Log(title = "批次任务日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(agricultureTaskLogService.deleteAgricultureTaskLogByLogIds(logIds));
    }
}
