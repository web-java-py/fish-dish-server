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
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.service.AgricultureDeviceSensorAlertService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 传感器预警信息Controller
 *
 * @author bxwy
 * @date 2025-05-26
 */
@RestController
@RequestMapping("/device/alert")
public class AgricultureDeviceSensorAlertController extends BaseController
{
    @Autowired
    private AgricultureDeviceSensorAlertService agricultureDeviceSensorAlertService;

    /**
     * 查询传感器预警信息列表
     */
//    @PreAuthorize("@ss.hasPermi('agriculture:alert:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        startPage();
        List<AgricultureDeviceSensorAlert> list = agricultureDeviceSensorAlertService.selectAgricultureDeviceSensorAlertList(agricultureDeviceSensorAlert);
        return getDataTable(list);
    }

    /**
     * 导出传感器预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:alert:export')")
    @Log(title = "传感器预警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        List<AgricultureDeviceSensorAlert> list = agricultureDeviceSensorAlertService.selectAgricultureDeviceSensorAlertList(agricultureDeviceSensorAlert);
        ExcelUtil<AgricultureDeviceSensorAlert> util = new ExcelUtil<AgricultureDeviceSensorAlert>(AgricultureDeviceSensorAlert.class);
        util.exportExcel(response, list, "传感器预警信息数据");
    }

    /**
     * 获取传感器预警信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:alert:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureDeviceSensorAlertService.selectAgricultureDeviceSensorAlertById(id));
    }

    /**
     * 新增传感器预警信息
     */
//    @PreAuthorize("@ss.hasPermi('agriculture:alert:add')")
//    @Log(title = "传感器预警信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        AgricultureDeviceSensorAlert saved = agricultureDeviceSensorAlertService.insertAgricultureDeviceSensorAlert(agricultureDeviceSensorAlert);
        return AjaxResult.success(saved); // 返回完整对象
    }

    /**
     * 修改传感器预警信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:alert:edit')")
    @Log(title = "传感器预警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        return toAjax(agricultureDeviceSensorAlertService.updateAgricultureDeviceSensorAlert(agricultureDeviceSensorAlert));
    }

    /**
     * 删除传感器预警信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:alert:remove')")
    @Log(title = "传感器预警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureDeviceSensorAlertService.deleteAgricultureDeviceSensorAlertByIds(ids));
    }
}
