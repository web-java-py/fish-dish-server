package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureMaterial;
import com.server.enums.BusinessType;
import com.server.service.AgricultureMaterialService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/23 17:50
 */
@RestController
@RequestMapping("/agriculture/material")
@Api(tags = "农资信息")
public class AgricultureMaterialController extends BaseController {

    @Autowired
    private AgricultureMaterialService agricultureMaterialService;

    /**
     * 查询农资信息
     * @param
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询农资信息")
    public TableDataInfo list(AgricultureMaterial agricultureMaterialInfo){
        //分页
        startPage();
        return getDataTable(agricultureMaterialService.selectAgricultureMaterialInfoList(agricultureMaterialInfo));
    }

    /**
     * 新增农资信息
     * Log是记录日志 BusinessType.INSERT 表明这是一项插入操作
     */
    @Log(title = "农资管理",businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult add(@RequestBody AgricultureMaterial agricultureMaterial){
        return toAjax(agricultureMaterialService.addAgricultureMaterial(agricultureMaterial));
    }

    /**
     * 删除农资信息
     */
    @Log(title = "农资管理",businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialId}")
    public AjaxResult delete(@PathVariable Long materialId){
        return toAjax(agricultureMaterialService.deleteById(materialId));
    }
    /**
     * 修改农资信息
     */
    @PutMapping()
    @ApiOperation("修改农资信息")
    @Log(title = "农资管理",businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AgricultureMaterial agricultureMaterial){
        return toAjax(agricultureMaterialService.updateagricultureMaterial(agricultureMaterial));
    }
    /**
     * 导出农资信息
     */
    @Log(title = "农资管理",businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出农资信息")
    public void export(HttpServletResponse response,AgricultureMaterial agricultureMaterial){
        List<AgricultureMaterial> agricultureMaterials  =agricultureMaterialService.selectAgricultureMaterialInfoList(agricultureMaterial);
        ExcelUtil<AgricultureMaterial> util = new ExcelUtil<AgricultureMaterial>(AgricultureMaterial.class);
        util.exportExcel(response,agricultureMaterials,"农资信息");
    }
}

