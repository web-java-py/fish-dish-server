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
import com.server.domain.AgricultureCamera;
import com.server.service.AgricultureCameraService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 摄像头参数Controller
 * 
 * @author bxwy
 * @date 2025-07-08
 */
@RestController
@RequestMapping("/device/camera")
public class AgricultureCameraController extends BaseController
{
    @Autowired
    private AgricultureCameraService agricultureCameraService;

    /**
     * 查询摄像头参数列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureCamera agricultureCamera)
    {
        startPage();
        List<AgricultureCamera> list = agricultureCameraService.selectAgricultureCameraList(agricultureCamera);
        return getDataTable(list);
    }

    /**
     * 根据设备ID获取摄像头参数
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:query')")
    @GetMapping("/byDeviceId/{deviceId}")
    public AjaxResult getByDeviceId(@PathVariable("deviceId") Long deviceId) {
        return success(agricultureCameraService.selectByDeviceId(deviceId));
    }

    /**
     * 导出摄像头参数列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:export')")
    @Log(title = "摄像头参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureCamera agricultureCamera)
    {
        List<AgricultureCamera> list = agricultureCameraService.selectAgricultureCameraList(agricultureCamera);
        ExcelUtil<AgricultureCamera> util = new ExcelUtil<AgricultureCamera>(AgricultureCamera.class);
        util.exportExcel(response, list, "摄像头参数数据");
    }

    /**
     * 获取摄像头参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureCameraService.selectAgricultureCameraById(id));
    }

    /**
     * 新增摄像头参数
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:add')")
    @Log(title = "摄像头参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureCamera agricultureCamera)
    {
        return toAjax(agricultureCameraService.insertAgricultureCamera(agricultureCamera));
    }

    /**
     * 修改摄像头参数
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:edit')")
    @Log(title = "摄像头参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureCamera agricultureCamera)
    {
        return toAjax(agricultureCameraService.updateAgricultureCamera(agricultureCamera));
    }

    /**
     * 删除摄像头参数
     */
    @PreAuthorize("@ss.hasPermi('agriculture:camera:remove')")
    @Log(title = "摄像头参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureCameraService.deleteAgricultureCameraByIds(ids));
    }
}
