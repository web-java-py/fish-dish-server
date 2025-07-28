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
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 设备MQTT配置Controller
 *
 * @author server
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/device/mqttconfig")
public class AgricultureDeviceMqttConfigController extends BaseController
{
    @Autowired
    private AgricultureDeviceMqttConfigService agricultureDeviceMqttConfigService;

    /**
     * 查询设备MQTT配置列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        startPage();
        List<AgricultureDeviceMqttConfig> list = agricultureDeviceMqttConfigService.selectAgricultureDeviceMqttConfigList(agricultureDeviceMqttConfig);
        return getDataTable(list);
    }

    /**
     * 通过设备id查询MQTT配置
     */
    @GetMapping("/byDeviceId/{deviceId}")
    public AjaxResult getByDeviceId(@PathVariable("deviceId") Long deviceId) {
        AgricultureDeviceMqttConfig config = agricultureDeviceMqttConfigService.getByDeviceId(deviceId);
        return AjaxResult.success(config);
    }


    /**
     * 导出设备MQTT配置列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:export')")
    @Log(title = "设备MQTT配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        List<AgricultureDeviceMqttConfig> list = agricultureDeviceMqttConfigService.selectAgricultureDeviceMqttConfigList(agricultureDeviceMqttConfig);
        ExcelUtil<AgricultureDeviceMqttConfig> util = new ExcelUtil<AgricultureDeviceMqttConfig>(AgricultureDeviceMqttConfig.class);
        util.exportExcel(response, list, "设备MQTT配置数据");
    }

    /**
     * 获取设备MQTT配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(agricultureDeviceMqttConfigService.selectAgricultureDeviceMqttConfigById(id));
    }

    /**
     * 新增设备MQTT配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:add')")
    @Log(title = "设备MQTT配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        return toAjax(agricultureDeviceMqttConfigService.insertAgricultureDeviceMqttConfig(agricultureDeviceMqttConfig));
    }

    /**
     * 修改设备MQTT配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:edit')")
    @Log(title = "设备MQTT配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        return toAjax(agricultureDeviceMqttConfigService.updateAgricultureDeviceMqttConfig(agricultureDeviceMqttConfig));
    }

    /**
     * 删除设备MQTT配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:mqttconfig:remove')")
    @Log(title = "设备MQTT配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(agricultureDeviceMqttConfigService.deleteAgricultureDeviceMqttConfigByIds(ids));
    }
}
