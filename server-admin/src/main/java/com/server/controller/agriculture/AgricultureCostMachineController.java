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
import com.server.domain.AgricultureCostMachine;
import com.server.service.AgricultureCostMachineService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 机械工时Controller
 *
 * @author agriculture
 * @date 2025-06-10
 */
@RestController
@RequestMapping("/agriculture/costMachine")
public class AgricultureCostMachineController extends BaseController
{
    @Autowired
    private AgricultureCostMachineService agricultureCostMachineService;

    /**
     * 查询机械工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureCostMachine agricultureCostMachine)
    {
        startPage();
        return getDataTable(agricultureCostMachineService.selectAgricultureCostMachineList(agricultureCostMachine));
    }

    /**
     * 导出机械工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:export')")
    @Log(title = "机械工时", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureCostMachine agricultureCostMachine)
    {
        List<AgricultureCostMachine> list = agricultureCostMachineService.selectAgricultureCostMachineList(agricultureCostMachine);
        ExcelUtil<AgricultureCostMachine> util = new ExcelUtil<AgricultureCostMachine>(AgricultureCostMachine.class);
        util.exportExcel(response, list, "机械工时数据");
    }

    /**
     * 获取机械工时详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") String costId)
    {
        return success(agricultureCostMachineService.selectAgricultureCostMachineByCostId(costId));
    }

    /**
     * 新增机械工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:add')")
    @Log(title = "机械工时", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureCostMachine agricultureCostMachine)
    {
        return toAjax(agricultureCostMachineService.insertAgricultureCostMachine(agricultureCostMachine));
    }

    /**
     * 修改机械工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:edit')")
    @Log(title = "机械工时", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureCostMachine agricultureCostMachine)
    {
        return toAjax(agricultureCostMachineService.updateAgricultureCostMachine(agricultureCostMachine));
    }

    /**
     * 删除机械工时
     */
    @Log(title = "机械工时", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costId}")
    public AjaxResult remove(@PathVariable Long costId)
    {
        return toAjax(agricultureCostMachineService.deleteAgricultureCostMachineByCostIds(costId));
    }
}
