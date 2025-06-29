package com.server.controller.agriculture;

import com.server.core.domain.AjaxResult;
import com.server.service.AgricultureConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agriculture/console")
@Api(tags = "控制台")
public class AgricultureConsoleController {

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    /**
     * 卡片数据
     *
     * @return
     */
    @GetMapping("/cardData")
    @ApiOperation("卡片数据")
    public AjaxResult card() {
        return AjaxResult.success(agricultureConsoleService.cardData());
    }

    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/countData")
    @ApiOperation("统计数据")
    public AjaxResult count() {
        return AjaxResult.success(agricultureConsoleService.countData());
    }
}
