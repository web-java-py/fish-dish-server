package com.server.controller.common;

import com.server.core.domain.AjaxResult;
import com.server.sse.SseServer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse/{uid}")
@Api(tags = "SSE")
public class SseController {

    @Autowired
    private SseServer sseServer;

    /**
     * 创建SSE
     * @param uid
     * @return
     */
    @GetMapping
    public SseEmitter createSse(@PathVariable String uid) {
        return sseServer.createSse(uid);
    }

    /**
     * 关闭SSE
     * @param uid
     * @return
     */
    @DeleteMapping
    public AjaxResult closeSse(@PathVariable String uid) {
        sseServer.closeSse(uid);
        return AjaxResult.success();
    }
}
