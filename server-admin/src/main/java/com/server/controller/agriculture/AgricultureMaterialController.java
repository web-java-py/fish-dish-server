package com.server.controller.agriculture;

import com.server.core.controller.BaseController;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureMaterial;
import com.server.service.AgricultureMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zbb
 * @Date: 2025/5/23 17:50
 */
@RestController
@RequestMapping("/agriculture/material")
@Api(tags = "农资信息")
public class AgricultureMaterialController extends BaseController {

    @Autowired
    private AgricultureMaterialService agricultureMaterialInfoService;

    @GetMapping("/list")
    @ApiOperation("查询农资信息")
    public TableDataInfo list(AgricultureMaterial agricultureMaterialInfo){
        //分页
        startPage();
        return getDataTable(agricultureMaterialInfoService.selectAgricultureMaterialInfoList(agricultureMaterialInfo));
    }

}

