package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.AgricultureWaterQualityData;
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
import com.server.service.AgricultureWaterQualityDataService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 水质数据Controller
 * 
 * @author bxwy
 * @date 2025-06-08
 */
@RestController
@RequestMapping("/device/quality")
public class AgricultureWaterQualityDataController extends BaseController
{
    @Autowired
    private AgricultureWaterQualityDataService agricultureWaterQualityDataService;

    /**
     * 查询水质数据列表
     */
    @PreAuthorize("@ss.hasPermi('server:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        startPage();
        List<AgricultureWaterQualityData> list = agricultureWaterQualityDataService.selectAgricultureWaterQualityDataList(agricultureWaterQualityData);
        return getDataTable(list);
    }

    /**
     * 导出水质数据列表
     */
    @PreAuthorize("@ss.hasPermi('server:data:export')")
    @Log(title = "水质数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureWaterQualityData agricultureWaterQualityData)
    {
        List<AgricultureWaterQualityData> list = agricultureWaterQualityDataService.selectAgricultureWaterQualityDataList(agricultureWaterQualityData);
        ExcelUtil<AgricultureWaterQualityData> util = new ExcelUtil<AgricultureWaterQualityData>(AgricultureWaterQualityData.class);
        util.exportExcel(response, list, "水质数据数据");
    }

    /**
     * 获取水质数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureWaterQualityDataService.selectAgricultureWaterQualityDataById(id));
    }

    /**
     * 新增水质数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:add')")
    @Log(title = "水质数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return toAjax(agricultureWaterQualityDataService.insertAgricultureWaterQualityData(agricultureWaterQualityData));
    }

    /**
     * 修改水质数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:edit')")
    @Log(title = "水质数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return toAjax(agricultureWaterQualityDataService.updateAgricultureWaterQualityData(agricultureWaterQualityData));
    }

    /**
     * 删除水质数据
     */
    @PreAuthorize("@ss.hasPermi('server:data:remove')")
    @Log(title = "水质数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureWaterQualityDataService.deleteAgricultureWaterQualityDataByIds(ids));
    }
}
