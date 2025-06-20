package com.server.controller.agriculture;

import com.server.domain.dto.ChatStreamDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/agriculture/ai")
@Api(tags = "AI")
public class AgricultureAiController {

    /**
     * 在线聊天
     */
    @PostMapping(value = "chatStream")
    @ApiOperation("在线聊天")
    public Flux<String> chatStream(@RequestBody ChatStreamDTO chatStreamDTO){
        return null;
    }
}
