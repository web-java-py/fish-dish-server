package com.server.controller.app;

import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.service.AppAgricultureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 农场
 */
@RestController
@RequestMapping("/app/agriculture")
public class AppAgricultureController {

    @Autowired
    private AppAgricultureService appAgricultureService;

    /**
     * 获取大棚
     * @return
     */
    @GetMapping("/pasture")
    @ApiOperation("获取大棚")
    public AjaxResult getPasture(){
        return AjaxResult.success(appAgricultureService.listPasture());
    }
}
