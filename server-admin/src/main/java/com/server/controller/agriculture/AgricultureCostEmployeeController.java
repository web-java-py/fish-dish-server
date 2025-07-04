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
import com.server.domain.AgricultureCostEmployee;
import com.server.service.AgricultureCostEmployeeService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 人工工时Controller
 *
 * @author agriculture
 * @date 2025-06-13
 */
@RestController
@RequestMapping("/agriculture/costEmployee")
public class AgricultureCostEmployeeController extends BaseController
{
    @Autowired
    private AgricultureCostEmployeeService agricultureCostEmployeeService;

    /**
     * 查询人工工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureCostEmployee agricultureCostEmployee)
    {
        startPage();
        List<AgricultureCostEmployee> list = agricultureCostEmployeeService.selectAgricultureCostEmployeeList(agricultureCostEmployee);
        return getDataTable(list);
    }

    /**
     * 导出人工工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:export')")
    @Log(title = "人工工时", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureCostEmployee agricultureCostEmployee)
    {
        List<AgricultureCostEmployee> list = agricultureCostEmployeeService.selectAgricultureCostEmployeeList(agricultureCostEmployee);
        ExcelUtil<AgricultureCostEmployee> util = new ExcelUtil<AgricultureCostEmployee>(AgricultureCostEmployee.class);
        util.exportExcel(response, list, "人工工时数据");
    }

    /**
     * 获取人工工时详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") String costId)
    {
        return success(agricultureCostEmployeeService.selectAgricultureCostEmployeeByCostId(costId));
    }

    /**
     * 新增人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:add')")
    @Log(title = "人工工时", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureCostEmployee agricultureCostEmployee)
    {
        return toAjax(agricultureCostEmployeeService.insertAgricultureCostEmployee(agricultureCostEmployee));
    }

    /**
     * 修改人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:edit')")
    @Log(title = "人工工时", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureCostEmployee agricultureCostEmployee)
    {
        return toAjax(agricultureCostEmployeeService.updateAgricultureCostEmployee(agricultureCostEmployee));
    }

    /**
     * 删除人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:remove')")
    @Log(title = "人工工时", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costId}")
    public AjaxResult remove(@PathVariable Long costId)
    {
        return toAjax(agricultureCostEmployeeService.deleteAgricultureCostEmployeeByCostIds(costId));
    }
}
