package com.server.controller.agriculture;

import com.server.core.controller.BaseController;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureMaterial;
import com.server.service.AgricultureMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
     */
    @PostMapping()
    public String insert(@RequestBody AgricultureMaterial agricultureMaterial){
        agricultureMaterialService.saveAgricultureMaterial(agricultureMaterial);
        return "";
    }

    /**
     * 删除农资信息
     */
    @DeleteMapping("/{materialId}")
    public String delete(@PathVariable("material_id") Long materialId){
        agricultureMaterialService.deleteById(materialId);
        return "删除成功";
    }
}

