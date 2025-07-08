package com.server.controller.data;

import com.server.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.service.ScaleService;

@RestController
@RequestMapping("/data/scope")
@Api(tags = "规模")
public class DataScaleController {

    @Autowired
    private ScaleService scaleService;

    /**
     * 获取设备规模
     * @return
     */
    @GetMapping("/device")
    @ApiOperation("获取设备规模")
    public AjaxResult getDevice(){
        return AjaxResult.success(scaleService.listDevice());
    }

    /**
     * 获取农场规模
     * @return
     */
    @GetMapping("/agriculture")
    @ApiOperation("获取农场规模")
    public AjaxResult getAgriculture(){
        return AjaxResult.success(scaleService.listAgriculture());
    }
}
