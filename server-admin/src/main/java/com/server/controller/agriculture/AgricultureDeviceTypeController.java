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
import com.server.domain.AgricultureDeviceType;
import com.server.service.AgricultureDeviceTypeService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 设备类型Controller
 *
 * @author server
 * @date 2025-06-20
 */
@RestController
@RequestMapping("/device/type")
public class AgricultureDeviceTypeController extends BaseController
{
    @Autowired
    private AgricultureDeviceTypeService agricultureDeviceTypeService;

    /**
     * 查询设备类型列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDeviceType agricultureDeviceType)
    {
        startPage();
        List<AgricultureDeviceType> list = agricultureDeviceTypeService.selectAgricultureDeviceTypeList(agricultureDeviceType);
        return getDataTable(list);
    }

    /**
     * 导出设备类型列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:export')")
    @Log(title = "设备类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureDeviceType agricultureDeviceType)
    {
        List<AgricultureDeviceType> list = agricultureDeviceTypeService.selectAgricultureDeviceTypeList(agricultureDeviceType);
        ExcelUtil<AgricultureDeviceType> util = new ExcelUtil<AgricultureDeviceType>(AgricultureDeviceType.class);
        util.exportExcel(response, list, "设备类型数据");
    }

    /**
     * 获取设备类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureDeviceTypeService.selectAgricultureDeviceTypeById(id));
    }

    /**
     * 新增设备类型
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:add')")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureDeviceType agricultureDeviceType)
    {
        return toAjax(agricultureDeviceTypeService.insertAgricultureDeviceType(agricultureDeviceType));
    }

    /**
     * 修改设备类型
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:edit')")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureDeviceType agricultureDeviceType)
    {
        return toAjax(agricultureDeviceTypeService.updateAgricultureDeviceType(agricultureDeviceType));
    }

    /**
     * 删除设备类型
     */
    @PreAuthorize("@ss.hasPermi('agriculture:type:remove')")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureDeviceTypeService.deleteAgricultureDeviceTypeByIds(ids));
    }
}
