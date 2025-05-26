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
import com.server.domain.AgricultureDevice;
import com.server.service.AgricultureDeviceService;

import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 设备信息Controller
 * 
 * @author bxwy
 * @date 2025-05-26
 */
@RestController
@RequestMapping("/server/device")
public class AgricultureDeviceController extends BaseController
{
    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

    /**
     * 查询设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('server:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDevice agricultureDevice)
    {
        startPage();
        List<AgricultureDevice> list = agricultureDeviceService.selectAgricultureDeviceList(agricultureDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('server:device:export')")
    @Log(title = "设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureDevice agricultureDevice)
    {
        List<AgricultureDevice> list = agricultureDeviceService.selectAgricultureDeviceList(agricultureDevice);
        ExcelUtil<AgricultureDevice> util = new ExcelUtil<AgricultureDevice>(AgricultureDevice.class);
        util.exportExcel(response, list, "设备信息数据");
    }

    /**
     * 获取设备信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(agricultureDeviceService.selectAgricultureDeviceById(id));
    }

    /**
     * 新增设备信息
     */
    @PreAuthorize("@ss.hasPermi('server:device:add')")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureDevice agricultureDevice)
    {
        return toAjax(agricultureDeviceService.insertAgricultureDevice(agricultureDevice));
    }

    /**
     * 修改设备信息
     */
    @PreAuthorize("@ss.hasPermi('server:device:edit')")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureDevice agricultureDevice)
    {
        return toAjax(agricultureDeviceService.updateAgricultureDevice(agricultureDevice));
    }

    /**
     * 删除设备信息
     */
    @PreAuthorize("@ss.hasPermi('server:device:remove')")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(agricultureDeviceService.deleteAgricultureDeviceByIds(ids));
    }
}
