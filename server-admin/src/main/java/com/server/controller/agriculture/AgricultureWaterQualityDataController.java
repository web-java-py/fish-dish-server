package com.server.controller.agriculture;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.AgricultureWaterQualityData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @PreAuthorize("@ss.hasPermi('agriculture:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        startPage();
        List<AgricultureWaterQualityData> list = agricultureWaterQualityDataService.selectAgricultureWaterQualityDataList(agricultureWaterQualityData);
        return getDataTable(list);
    }

    /**
     * 水质趋势数据（24小时/7天/30天）
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:trend')")
    @GetMapping("/trend")
    public AjaxResult trend(
            @RequestParam("pastureId") String pastureId,
            @RequestParam("batchId") String batchId,
            @RequestParam("range") String range //range（时间范围："day"、"week"、"month"）
    ) {
        Map<String, Object> trendData = agricultureWaterQualityDataService.getTrendData(pastureId, batchId, range);
        return success(trendData);
    }

    /**
     * 导出水质数据列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:data:export')")
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
    @PreAuthorize("@ss.hasPermi('agriculture:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureWaterQualityDataService.selectAgricultureWaterQualityDataById(id));
    }

    /**
     * 获取最新一条水质数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:data:query')")
    @GetMapping("/latest")
    public AjaxResult getLatestWaterQualityData(@RequestParam("pastureId") String pastureId) {
        AgricultureWaterQualityData latest = agricultureWaterQualityDataService.getLatestWaterQualityData(pastureId);
        return success(latest);
    }

    /**
     * 新增水质数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:data:add')")
    @Log(title = "水质数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return toAjax(agricultureWaterQualityDataService.insertAgricultureWaterQualityData(agricultureWaterQualityData));
    }

    /**
     * 修改水质数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:data:edit')")
    @Log(title = "水质数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return toAjax(agricultureWaterQualityDataService.updateAgricultureWaterQualityData(agricultureWaterQualityData));
    }

    /**
     * 删除水质数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:data:remove')")
    @Log(title = "水质数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureWaterQualityDataService.deleteAgricultureWaterQualityDataByIds(ids));
    }
}
