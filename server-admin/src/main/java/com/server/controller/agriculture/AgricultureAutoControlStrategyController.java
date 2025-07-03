package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgricultureAutoControlStrategy;
import com.server.service.AgricultureAutoControlStrategyService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 设备自动调节策略Controller
 * 
 * @author server
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/device/strategy")
public class AgricultureAutoControlStrategyController extends BaseController
{
    @Autowired
    private AgricultureAutoControlStrategyService agricultureAutoControlStrategyService;

    /**
     * 查询设备自动调节策略列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        startPage();
        List<AgricultureAutoControlStrategy> list = agricultureAutoControlStrategyService.selectAgricultureAutoControlStrategyList(agricultureAutoControlStrategy);
        return getDataTable(list);
    }

    /**
     * 导出设备自动调节策略列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:export')")
    @Log(title = "设备自动调节策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        List<AgricultureAutoControlStrategy> list = agricultureAutoControlStrategyService.selectAgricultureAutoControlStrategyList(agricultureAutoControlStrategy);
        ExcelUtil<AgricultureAutoControlStrategy> util = new ExcelUtil<AgricultureAutoControlStrategy>(AgricultureAutoControlStrategy.class);
        util.exportExcel(response, list, "设备自动调节策略数据");
    }

    /**
     * 获取设备自动调节策略详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureAutoControlStrategyService.selectAgricultureAutoControlStrategyById(id));
    }

    /**
     * 新增设备自动调节策略
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:add')")
    @Log(title = "设备自动调节策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        return toAjax(agricultureAutoControlStrategyService.insertAgricultureAutoControlStrategy(agricultureAutoControlStrategy));
    }

    /**
     * 修改设备自动调节策略
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:edit')")
    @Log(title = "设备自动调节策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureAutoControlStrategy agricultureAutoControlStrategy)
    {
        return toAjax(agricultureAutoControlStrategyService.updateAgricultureAutoControlStrategy(agricultureAutoControlStrategy));
    }

    /**
     * 删除设备自动调节策略
     */
    @PreAuthorize("@ss.hasPermi('agriculture:strategy:remove')")
    @Log(title = "设备自动调节策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureAutoControlStrategyService.deleteAgricultureAutoControlStrategyByIds(ids));
    }
}
