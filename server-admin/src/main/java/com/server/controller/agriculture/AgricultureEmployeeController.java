package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.Log;
import com.server.core.controller.BaseController;
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
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgricultureEmployee;
import com.server.service.AgricultureEmployeeService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 雇员Controller
 *
 * @author agriculture
 * @date 2025-06-10
 */
@RestController
@RequestMapping("/agriculture/employee")
public class AgricultureEmployeeController extends BaseController
{
    @Autowired
    private AgricultureEmployeeService agricultureEmployeeService;

    /**
     * 查询雇员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureEmployee agricultureEmployee)
    {
        startPage();
        return getDataTable(agricultureEmployeeService.selectAgricultureEmployeeList(agricultureEmployee));
    }

    /**
     * 导出雇员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:export')")
    @Log(title = "雇员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureEmployee agricultureEmployee)
    {
        List<AgricultureEmployee> list = agricultureEmployeeService.selectAgricultureEmployeeList(agricultureEmployee);
        ExcelUtil<AgricultureEmployee> util = new ExcelUtil<AgricultureEmployee>(AgricultureEmployee.class);
        util.exportExcel(response, list, "雇员数据");
    }

    /**
     * 获取雇员详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:query')")
    @GetMapping(value = "/{employeeId}")
    public AjaxResult getInfo(@PathVariable("employeeId") String employeeId)
    {
        return success(agricultureEmployeeService.selectAgricultureEmployeeByEmployeeId(employeeId));
    }

    /**
     * 新增雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:add')")
    @Log(title = "雇员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureEmployee agricultureEmployee)
    {
        return toAjax(agricultureEmployeeService.insertAgricultureEmployee(agricultureEmployee));
    }

    /**
     * 修改雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:edit')")
    @Log(title = "雇员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureEmployee agricultureEmployee)
    {
        return toAjax(agricultureEmployeeService.updateAgricultureEmployee(agricultureEmployee));
    }

    /**
     * 删除雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:remove')")
    @Log(title = "雇员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{employeeIds}")
    public AjaxResult remove(@PathVariable String[] employeeIds)
    {
        return toAjax(agricultureEmployeeService.deleteAgricultureEmployeeByEmployeeIds(employeeIds));
    }
}
