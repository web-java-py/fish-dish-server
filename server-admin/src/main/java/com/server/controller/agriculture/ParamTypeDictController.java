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
import com.server.domain.ParamTypeDict;
import com.server.service.ParamTypeDictService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 传感器参数类型中英文对照Controller
 * 
 * @author server
 * @date 2025-06-28
 */
@RestController
@RequestMapping("/device/dict")
public class ParamTypeDictController extends BaseController
{
    @Autowired
    private ParamTypeDictService paramTypeDictService;

    /**
     * 查询传感器参数类型中英文对照列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParamTypeDict paramTypeDict)
    {
        startPage();
        List<ParamTypeDict> list = paramTypeDictService.selectParamTypeDictList(paramTypeDict);
        return getDataTable(list);
    }

    /**
     * 导出传感器参数类型中英文对照列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:export')")
    @Log(title = "传感器参数类型中英文对照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParamTypeDict paramTypeDict)
    {
        List<ParamTypeDict> list = paramTypeDictService.selectParamTypeDictList(paramTypeDict);
        ExcelUtil<ParamTypeDict> util = new ExcelUtil<ParamTypeDict>(ParamTypeDict.class);
        util.exportExcel(response, list, "传感器参数类型中英文对照数据");
    }

    /**
     * 获取传感器参数类型中英文对照详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(paramTypeDictService.selectParamTypeDictById(id));
    }

    /**
     * 新增传感器参数类型中英文对照
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:add')")
    @Log(title = "传感器参数类型中英文对照", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParamTypeDict paramTypeDict)
    {
        return toAjax(paramTypeDictService.insertParamTypeDict(paramTypeDict));
    }

    /**
     * 修改传感器参数类型中英文对照
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:edit')")
    @Log(title = "传感器参数类型中英文对照", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParamTypeDict paramTypeDict)
    {
        return toAjax(paramTypeDictService.updateParamTypeDict(paramTypeDict));
    }

    /**
     * 删除传感器参数类型中英文对照
     */
    @PreAuthorize("@ss.hasPermi('agriculture:dict:remove')")
    @Log(title = "传感器参数类型中英文对照", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(paramTypeDictService.deleteParamTypeDictByIds(ids));
    }
}
