package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.domain.vo.AgricultureDeviceVO;
import com.server.enums.SeeMessageType;
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
@RequestMapping("/device")
public class AgricultureDeviceController extends BaseController
{
    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

    /**
     * 查询设备信息列表
     */
//    @PreAuthorize("@ss.hasPermi('agriculture:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDevice agricultureDevice)
    {
        startPage();
        List<AgricultureDeviceVO> list = agricultureDeviceService.selectAgricultureDeviceListVO(agricultureDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:device:export')")
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
    @PreAuthorize("@ss.hasPermi('agriculture:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(agricultureDeviceService.selectAgricultureDeviceById(id));
    }

    /**
     * 新增设备信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:device:add')")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult add(@RequestBody AgricultureDevice agricultureDevice)
    {
        Long id = agricultureDeviceService.insertAgricultureDevice(agricultureDevice);
        return AjaxResult.success("操作成功", id);
    }

    /**
     * 修改设备信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:device:edit')")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult edit(@RequestBody AgricultureDevice agricultureDevice)
    {
        return toAjax(agricultureDeviceService.updateAgricultureDevice(agricultureDevice));
    }

    /**
     * 删除设备信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:device:remove')")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(agricultureDeviceService.deleteAgricultureDeviceByIds(ids));
    }
}
