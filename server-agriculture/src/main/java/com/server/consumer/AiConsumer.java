package com.server.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.server.ai.AiResponseFormat;
import com.server.ai.AiService;

import com.server.constant.SocketConstant;
import com.server.core.redis.RedisCache;
import com.server.domain.AgricultureClassAiReport;
import com.server.domain.AgricultureJob;
import com.server.domain.dto.AiJobDTO;
import com.server.domain.vo.AiMessageVO;
import com.server.domain.vo.SocketMessageVO;
import com.server.enums.ClassType;
import com.server.service.AgricultureClassAiReportService;
import com.server.service.AgricultureJobService;
import com.server.utils.uuid.IdUtils;
import com.server.ws.MqSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.constant.RabbitMQConstant.*;
import static com.server.constant.CacheConstants.*;

@Slf4j
@Component
public class AiConsumer {

    @Autowired
    private AiService aiService;

    @Autowired
    private AgricultureJobService agricultureJobService;

    @Autowired
    private MqSocketServer mqSocketServer;

    @Autowired
    private AgricultureClassAiReportService agricultureClassAiReportService;

    @Autowired
    private RedisCache redisCache;

    /**
     * ai作业处理消费者
     *
     * @param aiMessageVO
     */
    @Transactional
    @RabbitListener(queues = AI_JOB_QUEUE)
    public void jobProcess(AiMessageVO aiMessageVO) {
        try {
            String response = aiService.chat(aiMessageVO.getPrompt(), AiResponseFormat.jobResponseFormat());
            AiJobDTO aiJobDTO = JSON.parseObject(response, AiJobDTO.class);
            List<AgricultureJob> agricultureJobList = aiJobDTO.getJobs().stream()
                    .peek(job -> {
                        job.setClassId(aiMessageVO.getId());
                        job.setUpdateBy(aiMessageVO.getCreateBy());
                        job.setCreateBy(aiMessageVO.getCreateBy());
                    })
                    .collect(Collectors.toList());
            agricultureJobService.delAgricultureJobByClassId(aiMessageVO.getId());
            agricultureJobService.addAgricultureJobBatch(agricultureJobList);
            SocketMessageVO socketResponse = SocketMessageVO.builder()
                    .id(IdUtils.fastSimpleUUID())
                    .type(SocketConstant.AI_JOB_KEY)
                    .content("您的AI助手已经完成种类作业流程的处理啦！")
                    .build();
            mqSocketServer.sendToAllClient(JSON.toJSONString(socketResponse));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            redisCache.deleteObject(AI_JOB_NOT_REPEAT_SUBMIT + aiMessageVO.getId());
        }
    }

    /**
     * ai智能报告消费者
     *
     * @param aiMessageVO
     */
    @RabbitListener(queues = AI_REPORT_QUEUE)
    public void reportProcess(AiMessageVO aiMessageVO) {
        try {
            String response = aiService.chat(aiMessageVO.getPrompt(), AiResponseFormat.classReportResponseFormat(aiMessageVO.getClassType()));
            JSONObject jsonObject = JSON.parseObject(response);
            JSONObject environmentalParams = jsonObject.getJSONObject("environmental_params");
            JSONObject coreIndicators = jsonObject.getJSONObject("core_indicators");
            JSONObject comprehensiveAssessment = jsonObject.getJSONObject("comprehensive_assessment");
            AgricultureClassAiReport agricultureClassAiReport = new AgricultureClassAiReport();
            if (ClassType.FISH == aiMessageVO.getClassType()) {
                JSONArray breedingSuggestions = jsonObject.getJSONArray("breeding_suggestions");
                agricultureClassAiReport.setOptimalWaterTemperature(environmentalParams.get("water_temperature").toString());
                agricultureClassAiReport.setOptimalWaterPh(environmentalParams.get("ph").toString());
                agricultureClassAiReport.setOptimalDissolvedOxygen(environmentalParams.get("dissolved_oxygen").toString());
                agricultureClassAiReport.setOptimalAmmonia(environmentalParams.get("ammonia_nitrogen").toString());
                agricultureClassAiReport.setOptimalNitrite(environmentalParams.get("nitrite").toString());
                agricultureClassAiReport.setFeedConversion((Integer) coreIndicators.get("feed_conversion"));
                agricultureClassAiReport.setCultivationDifficulty(comprehensiveAssessment.get("breeding_difficulty").toString());

                // 按顺序填充养殖建议
                agricultureClassAiReport.setWaterManagement(breedingSuggestions.getJSONObject(0).getString("content"));
                agricultureClassAiReport.setFeedingManagement(breedingSuggestions.getJSONObject(1).getString("content"));
                agricultureClassAiReport.setDiseasePrevention(breedingSuggestions.getJSONObject(2).getString("content"));
                agricultureClassAiReport.setEnvironmentMonitoring(breedingSuggestions.getJSONObject(3).getString("content"));
            } else {
                JSONArray plantingSuggestions = jsonObject.getJSONArray("planting_suggestions");
                agricultureClassAiReport.setOptimalTemperature(environmentalParams.get("temperature").toString());
                agricultureClassAiReport.setOptimalHumidity(environmentalParams.get("humidity").toString());
                agricultureClassAiReport.setOptimalLight(environmentalParams.get("light").toString());
                agricultureClassAiReport.setOptimalWindDirection(environmentalParams.get("wind_direction").toString());
                agricultureClassAiReport.setOptimalWindSpeed(environmentalParams.get("wind_speed").toString());
                agricultureClassAiReport.setOptimalSoilPh(environmentalParams.get("ph").toString());
                agricultureClassAiReport.setFeedConversion((Integer) coreIndicators.get("fertilizer_conversion"));
                agricultureClassAiReport.setCultivationDifficulty(comprehensiveAssessment.get("cultivation_difficulty").toString());

                // 按顺序填充种植建议
                agricultureClassAiReport.setWaterManagement(plantingSuggestions.getJSONObject(0).getString("content"));
                agricultureClassAiReport.setFeedingManagement(plantingSuggestions.getJSONObject(1).getString("content"));
                agricultureClassAiReport.setDiseasePrevention(plantingSuggestions.getJSONObject(2).getString("content"));
                agricultureClassAiReport.setEnvironmentMonitoring(plantingSuggestions.getJSONObject(3).getString("content"));
            }
            agricultureClassAiReport.setGrowthRate((Integer) coreIndicators.get("growth_rate"));
            agricultureClassAiReport.setDiseaseResistance((Integer) coreIndicators.get("disease_resistance"));
            agricultureClassAiReport.setMarketAcceptance((Integer) coreIndicators.get("market_acceptance"));

            agricultureClassAiReport.setGrowthAssessment(comprehensiveAssessment.get("growth_assessment").toString());
            agricultureClassAiReport.setGeneralRecommendations(comprehensiveAssessment.get("general_recommendations").toString());
            agricultureClassAiReport.setMarketAnalysis(comprehensiveAssessment.get("market_analysis").toString());

            agricultureClassAiReport.setClassId(aiMessageVO.getId());
            agricultureClassAiReport.setCreateBy(aiMessageVO.getCreateBy());
            agricultureClassAiReport.setUpdateBy(aiMessageVO.getCreateBy());

            agricultureClassAiReportService.save(agricultureClassAiReport);

            SocketMessageVO socketResponse = SocketMessageVO.builder()
                    .id(agricultureClassAiReport.getReportId())
                    .type(SocketConstant.AI_REPORT_KEY)
                    .content("您的AI助手已经完成种类智能报告的处理啦！")
                    .build();
            mqSocketServer.sendToAllClient(JSON.toJSONString(socketResponse));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            redisCache.deleteObject(AI_REPORT_NOT_REPEAT_SUBMIT + aiMessageVO.getId());
        }
    }
}
