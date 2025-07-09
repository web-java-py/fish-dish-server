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
import com.server.domain.AgricultureTaskEmployee;
import com.server.service.AgricultureTaskEmployeeService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 批次任务工人Controller
 *
 * @author agriculture
 * @date 2025-06-10
 */
@RestController
@RequestMapping("/agriculture/taskEmployee")
public class AgricultureTaskEmployeeController extends BaseController
{
    @Autowired
    private AgricultureTaskEmployeeService agricultureTaskEmployeeService;

    /**
     * 查询批次任务工人列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureTaskEmployee agricultureTaskEmployee)
    {
        startPage();
        return getDataTable( agricultureTaskEmployeeService.selectAgricultureTaskEmployeeList(agricultureTaskEmployee));
    }

    /**
     * 导出批次任务工人列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:export')")
    @Log(title = "批次任务工人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureTaskEmployee agricultureTaskEmployee)
    {
        List<AgricultureTaskEmployee> list = agricultureTaskEmployeeService.selectAgricultureTaskEmployeeList(agricultureTaskEmployee);
        ExcelUtil<AgricultureTaskEmployee> util = new ExcelUtil<AgricultureTaskEmployee>(AgricultureTaskEmployee.class);
        util.exportExcel(response, list, "批次任务工人数据");
    }



    /**
     * 新增批次任务工人
     */
    @Log(title = "批次任务工人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureTaskEmployee agricultureTaskEmployee)
    {
        return toAjax(agricultureTaskEmployeeService.insertAgricultureTaskEmployee(agricultureTaskEmployee));
    }

    /**
     * 修改批次任务工人
     */
    @Log(title = "批次任务工人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureTaskEmployee agricultureTaskEmployee)
    {
        return toAjax(agricultureTaskEmployeeService.updateAgricultureTaskEmployee(agricultureTaskEmployee));
    }

    /**
     * 删除批次任务工人
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:remove')")
    @Log(title = "批次任务工人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{employee_id}")
    public AjaxResult remove(@PathVariable Long employee_id)
    {
        return toAjax(agricultureTaskEmployeeService.deleteAgricultureTaskEmployeeByIds(employee_id));
    }
}
