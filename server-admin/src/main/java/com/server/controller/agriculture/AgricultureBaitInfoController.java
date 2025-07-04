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
import com.server.domain.AgricultureBaitInfo;
import com.server.service.AgricultureBaitInfoService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 饵料信息Controller
 *
 * @author server
 * @date 2025-06-14
 */
@RestController
@RequestMapping("/agriculture/baitInfo")
public class AgricultureBaitInfoController extends BaseController
{
    @Autowired
    private AgricultureBaitInfoService agricultureBaitInfoService;

    /**
     * 查询饵料信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureBaitInfo agricultureBaitInfo)
    {
        startPage();
        return getDataTable(agricultureBaitInfoService.selectAgricultureBaitInfoList(agricultureBaitInfo));
    }

    /**
     * 导出饵料信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:export')")
    @Log(title = "饵料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureBaitInfo agricultureBaitInfo)
    {
        List<AgricultureBaitInfo> list = agricultureBaitInfoService.selectAgricultureBaitInfoList(agricultureBaitInfo);
        ExcelUtil<AgricultureBaitInfo> util = new ExcelUtil<AgricultureBaitInfo>(AgricultureBaitInfo.class);
        util.exportExcel(response, list, "饵料信息数据");
    }

    /**
     * 获取饵料信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:query')")
    @GetMapping(value = "/{baitId}")
    public AjaxResult getInfo(@PathVariable("baitId") String baitId)
    {
        return success(agricultureBaitInfoService.selectAgricultureBaitInfoByBaitId(baitId));
    }

    /**
     * 新增饵料信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:add')")
    @Log(title = "饵料信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureBaitInfo agricultureBaitInfo)
    {
        return toAjax(agricultureBaitInfoService.insertAgricultureBaitInfo(agricultureBaitInfo));
    }

    /**
     * 修改饵料信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:edit')")
    @Log(title = "饵料信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureBaitInfo agricultureBaitInfo)
    {
        return toAjax(agricultureBaitInfoService.updateAgricultureBaitInfo(agricultureBaitInfo));
    }

    /**
     * 删除饵料信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:info:remove')")
    @Log(title = "饵料信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{baitId}")
    public AjaxResult remove(@PathVariable Long baitId)
    {
        return toAjax(agricultureBaitInfoService.deleteAgricultureBaitInfoByBaitIds(baitId));
    }
}
