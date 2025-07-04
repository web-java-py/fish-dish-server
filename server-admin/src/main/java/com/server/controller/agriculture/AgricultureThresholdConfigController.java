package com.server.controller.agriculture;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureThresholdConfig;
import com.server.service.AgricultureDeviceService;
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
import com.server.service.AgricultureThresholdConfigService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 阈值配置Controller
 * 
 * @author bxwy
 * @date 2025-06-08
 */
@RestController
@RequestMapping("/device/config")
public class AgricultureThresholdConfigController extends BaseController
{
    @Autowired
    private AgricultureThresholdConfigService agricultureThresholdConfigService;

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;
    /**
     * 查询阈值配置列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureThresholdConfig agricultureThresholdConfig)
    {
        startPage();
        List<AgricultureThresholdConfig> list = agricultureThresholdConfigService.selectAgricultureThresholdConfigList(agricultureThresholdConfig);
        return getDataTable(list);
    }

    /**
     * 根据大棚id和分区id 传感器设备类型 查询所有设备的阈值配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:list')")
    @GetMapping("/listByPastureAndBatch")
    public AjaxResult listByPastureAndBatch(Long pastureId, Long batchId, String deviceType) {
        // 1. 查询所有设备
        List<AgricultureDevice> devices = agricultureDeviceService.list(
                new LambdaQueryWrapper<AgricultureDevice>()
                        .eq(AgricultureDevice::getPastureId, pastureId)
                        .eq(AgricultureDevice::getBatchId, batchId)
        );
        // 2. 过滤设备类型
        if (deviceType != null) {
            devices = devices.stream()
                    .filter(d -> d.getDeviceTypeId() != null && d.getDeviceTypeId().equals(deviceType))
                    .collect(Collectors.toList());
        }
        List<Long> deviceIds = devices.stream().map(AgricultureDevice::getId).collect(Collectors.toList());
        if (deviceIds.isEmpty()) {
            return AjaxResult.success(Collections.emptyList());
        }
        // 3. 查询所有设备的阈值配置
        List<AgricultureThresholdConfig> configs = agricultureThresholdConfigService.selectByDeviceIds(deviceIds);
        return AjaxResult.success(configs);
    }

    /**
     * 根据设备ID查询阈值配置列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:list')")
    @GetMapping("/listByDeviceId/{deviceId}")
    public AjaxResult listByDeviceId(@PathVariable Long deviceId) {
        List<AgricultureThresholdConfig> list = agricultureThresholdConfigService.selectByDeviceId(deviceId);
        return AjaxResult.success(list);
    }

    /**
     * 导出阈值配置列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:export')")
    @Log(title = "阈值配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureThresholdConfig agricultureThresholdConfig)
    {
        List<AgricultureThresholdConfig> list = agricultureThresholdConfigService.selectAgricultureThresholdConfigList(agricultureThresholdConfig);
        ExcelUtil<AgricultureThresholdConfig> util = new ExcelUtil<AgricultureThresholdConfig>(AgricultureThresholdConfig.class);
        util.exportExcel(response, list, "阈值配置数据");
    }

    /**
     * 获取阈值配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureThresholdConfigService.selectAgricultureThresholdConfigById(id));
    }

    /**
     * 新增阈值配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:add')")
    @Log(title = "阈值配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureThresholdConfig agricultureThresholdConfig)
    {
        return toAjax(agricultureThresholdConfigService.insertAgricultureThresholdConfig(agricultureThresholdConfig));
    }

    /**
     * 修改阈值配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:edit')")
    @Log(title = "阈值配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureThresholdConfig agricultureThresholdConfig)
    {
        return toAjax(agricultureThresholdConfigService.updateAgricultureThresholdConfig(agricultureThresholdConfig));
    }

    /**
     * 删除阈值配置
     */
    @PreAuthorize("@ss.hasPermi('agriculture:config:remove')")
    @Log(title = "阈值配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureThresholdConfigService.deleteAgricultureThresholdConfigByIds(ids));
    }
}
