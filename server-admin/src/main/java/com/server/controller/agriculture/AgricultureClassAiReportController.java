package com.server.controller.agriculture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureClassAiReport;
import com.server.service.AgricultureClassAiReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agriculture/class/report")
@Api(tags = "种类报告")
public class AgricultureClassAiReportController {

    @Autowired
    private AgricultureClassAiReportService agricultureClassAiReportService;


    /**
     * 获取种类报告详细信息
     * @param agricultureClassAiReport
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("获取种类报告详细信息")
    public AjaxResult info(AgricultureClassAiReport agricultureClassAiReport){
        return AjaxResult.success(agricultureClassAiReportService.getAgricultureClassAiReportInfo(agricultureClassAiReport));
    }
}
