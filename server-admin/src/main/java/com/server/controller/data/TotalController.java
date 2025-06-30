package com.server.controller.data;

import com.server.constant.SseConstant;
import com.server.enums.SeeMessageType;
import com.server.service.TotalService;
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
@RequestMapping("/data/total")
@Api(tags = "统计")
public class TotalController {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private TotalService totalService;

    /**
     * 获取统计数据
     *
     * @return
     */
    @GetMapping
    @ApiOperation("获取统计数据")
    public SseEmitter getTotal() {
        String data = SeeMessageType.Data.name();
        sseServer.createSse(data);
        Map<String, Object> map = totalService.listTotal();
        sseServer.sendMessage(data, data, map);
        return sseServer.getSseEmitter(data);
    }
}
