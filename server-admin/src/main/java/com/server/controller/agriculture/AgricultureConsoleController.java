package com.server.controller.agriculture;

import com.server.constant.SseConstant;
import com.server.core.domain.AjaxResult;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureConsoleService;
import com.server.sse.SseServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/agriculture/console")
@Api(tags = "控制台")
public class AgricultureConsoleController {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    /**
     * 获取统计数据
     *
     * @return
     */
    @GetMapping("/total")
    @ApiOperation("获取统计数据")
    public SseEmitter getTotal() {
        String console = SeeMessageType.CONSOLE.name();
        sseServer.createSse(console);
        Map<String, Object> map = agricultureConsoleService.listTotal();
        sseServer.sendMessage(console, console, map);
        return sseServer.getSseEmitter(console);
    }

    /**
     * 获取农场数据
     *
     * @return
     */
    @GetMapping("/agriculture")
    @ApiOperation("获取农场数据")
    public AjaxResult getAgriculture() {
        return AjaxResult.success(agricultureConsoleService.listAgriculture());
    }

    /**
     * 获取任务数据
     *
     * @return
     */
    @GetMapping("/batchTask")
    @ApiOperation("获取任务数据")
    public AjaxResult getBatchTask() {
        return AjaxResult.success(agricultureConsoleService.listBatchTask());
    }
}
