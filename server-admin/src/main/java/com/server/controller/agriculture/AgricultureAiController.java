package com.server.controller.agriculture;

import com.server.ai.AiService;
import com.server.core.domain.AjaxResult;
import com.server.domain.dto.ChatStreamDTO;
import com.server.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/agriculture/ai")
@Api(tags = "AI")
public class AgricultureAiController {

    @Autowired
    private AiService aiService;

    /**
     * 在线聊天
     */
    @PostMapping(value = "/chatStream")
    @ApiOperation("在线聊天")
    public Flux<String> chatStream(@RequestBody ChatStreamDTO chatStreamDTO){
        return StringUtils.isNotEmpty(chatStreamDTO.getFile()) ?
                aiService.chatVl(chatStreamDTO.getPrompt(),chatStreamDTO.getFile())
                :aiService.chatAgentsStream(chatStreamDTO.getPrompt());
    }

    /**
     * 智能分析
     */
    @PostMapping(value = "/identify")
    @ApiOperation("智能分析")
    public AjaxResult identify(@RequestBody ChatStreamDTO chatStreamDTO){
        return AjaxResult.success(aiService.identify(chatStreamDTO.getPrompt(), chatStreamDTO.getFile()));
    }
}
