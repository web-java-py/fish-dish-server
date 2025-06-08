package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.AgricultureWeatherData;
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
import com.server.service.AgricultureWeatherDataService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 气象数据Controller
 * 
 * @author bxwy
 * @date 2025-06-08
 */
@RestController
@RequestMapping("/device/weather")
public class AgricultureWeatherDataController extends BaseController
{
    @Autowired
    private AgricultureWeatherDataService agricultureWeatherDataService;

    /**
     * 查询气象数据列表
     */
    @PreAuthorize("@ss.hasPermi('server:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureWeatherData agricultureWeatherData)
    {
        startPage();
        List<AgricultureWeatherData> list = agricultureWeatherDataService.selectAgricultureWeatherDataList(agricultureWeatherData);
        return getDataTable(list);
    }

    /**
     * 导出气象数据列表
     */
    @PreAuthorize("@ss.hasPermi('server:data:export')")
    @Log(title = "气象数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureWeatherData agricultureWeatherData)
    {
        List<AgricultureWeatherData> list = agricultureWeatherDataService.selectAgricultureWeatherDataList(agricultureWeatherData);
        ExcelUtil<AgricultureWeatherData> util = new ExcelUtil<AgricultureWeatherData>(AgricultureWeatherData.class);
        util.exportExcel(response, list, "气象数据数据");
    }

    /**
     * 获取气象数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureWeatherDataService.selectAgricultureWeatherDataById(id));
    }

    /**
     * 新增气象数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:add')")
    @Log(title = "气象数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureWeatherData agricultureWeatherData)
    {
        return toAjax(agricultureWeatherDataService.insertAgricultureWeatherData(agricultureWeatherData));
    }

    /**
     * 修改气象数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:edit')")
    @Log(title = "气象数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureWeatherData agricultureWeatherData)
    {
        return toAjax(agricultureWeatherDataService.updateAgricultureWeatherData(agricultureWeatherData));
    }

    /**
     * 删除气象数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:remove')")
    @Log(title = "气象数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureWeatherDataService.deleteAgricultureWeatherDataByIds(ids));
    }
}
