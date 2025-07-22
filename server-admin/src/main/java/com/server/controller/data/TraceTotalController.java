package com.server.controller.data;

import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.service.TraceTotalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/7/16 20:00
 */

@RestController
@RequestMapping("/data/traceTotal")
public class TraceTotalController extends BaseController {

    @Autowired
    private TraceTotalService traceTotalService;

    /**
     * 查询2025年和2024年当前月及前四个月（共五个月）的溯源日志数量，按年和月分组
     */
    @ApiOperation("查询2025年和2024年当前月及前四个月的溯源日志数量")
    @GetMapping("/list")
    public AjaxResult getTraceTotal() {
        List<Map<String, Object>> result = traceTotalService.getTraceTotal();
        return success(result);
    }
}
