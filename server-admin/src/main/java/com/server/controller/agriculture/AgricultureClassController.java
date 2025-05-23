package com.server.controller.agriculture;

import com.server.core.controller.BaseController;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureClass;
import com.server.service.AgricultureClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agriculture/class")
@Api(tags = "种类")
public class AgricultureClassController extends BaseController {

    @Autowired
    private AgricultureClassService agricultureClassService;


    @GetMapping("/list")
    @ApiOperation("查询种类数据")
    public TableDataInfo list(AgricultureClass agricultureClass){
        startPage();
        return getDataTable(agricultureClassService.selectAgricultureClasList(agricultureClass));
    }
}
