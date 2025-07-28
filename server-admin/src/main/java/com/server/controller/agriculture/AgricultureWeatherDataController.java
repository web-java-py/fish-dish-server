package com.server.controller.agriculture;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.AgricultureWeatherData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
     * 气象趋势数据（24小时/7天/30天）
     */
//    @PreAuthorize("@ss.hasPermi('agriculture:weather:trend')")
    @GetMapping("/trend")
    public AjaxResult trend(
            @RequestParam("pastureId") String pastureId,
            @RequestParam("batchId") String batchId,
            @RequestParam("range") String range //range（时间范围："day"、"week"、"month"）
    ) {
        Map<String, Object> trendData = agricultureWeatherDataService.getTrendData(pastureId, batchId, range);
        return success(trendData);
    }

    /**
     * 查询气象数据列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:list')")
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
    @PreAuthorize("@ss.hasPermi('agriculture:weather:export')")
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
    @PreAuthorize("@ss.hasPermi('agriculture:weather:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureWeatherDataService.selectAgricultureWeatherDataById(id));
    }

    /**
     * 获取合并后的最新一条气象数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:query')")
    @GetMapping("/latest")
    public AjaxResult getLatestWeatherData(@RequestParam("pastureId") String pastureId) {
        AgricultureWeatherData latest = agricultureWeatherDataService.getLatestMergedWeatherData(pastureId);
        return success(latest);
    }

    /**
     * 新增气象数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:add')")
    @Log(title = "气象数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureWeatherData agricultureWeatherData)
    {
        return toAjax(agricultureWeatherDataService.insertAgricultureWeatherData(agricultureWeatherData));
    }

    /**
     * 修改气象数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:edit')")
    @Log(title = "气象数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureWeatherData agricultureWeatherData)
    {
        return toAjax(agricultureWeatherDataService.updateAgricultureWeatherData(agricultureWeatherData));
    }

    /**
     * 删除气象数据
     */
    @PreAuthorize("@ss.hasPermi('agriculture:weather:remove')")
    @Log(title = "气象数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureWeatherDataService.deleteAgricultureWeatherDataByIds(ids));
    }
}
