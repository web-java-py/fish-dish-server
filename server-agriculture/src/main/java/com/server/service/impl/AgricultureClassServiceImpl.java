package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.ai.AiResponseFormat;
import com.server.constant.AgricultureConstants;
import com.server.constant.CacheConstants;
import com.server.constant.Constants;
import com.server.constant.RabbitMQConstant;
import com.server.core.redis.RedisCache;
import com.server.domain.AgricultureClass;
import com.server.domain.vo.AiMessageVO;
import com.server.enums.ClassType;
import com.server.exception.ServiceException;
import com.server.mapper.AgricultureClassMapper;
import com.server.service.AgricultureClassService;
import com.server.utils.SecurityUtils;
import com.server.utils.StringUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgricultureClassServiceImpl extends ServiceImpl<AgricultureClassMapper, AgricultureClass> implements AgricultureClassService {

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询种类数据
     *
     * @param agricultureClass
     * @return
     */
    @Override
    public List<AgricultureClass> selectAgricultureClassList(AgricultureClass agricultureClass) {
        LambdaQueryWrapper<AgricultureClass> lambda = new QueryWrapper<AgricultureClass>().lambda();
        lambda
                .like(StringUtils.isNotEmpty(agricultureClass.getClassName()), AgricultureClass::getClassName, agricultureClass.getClassName())
                .like(StringUtils.isNotEmpty(agricultureClass.getClassTypeName()), AgricultureClass::getClassTypeName, agricultureClass.getClassTypeName());
        return agricultureClassMapper.selectList(lambda);
    }

    /**
     * 新增种类数据
     *
     * @param agricultureClass
     * @return
     */
    @Override
    public int addAgricultureClass(AgricultureClass agricultureClass) {
        validate(agricultureClass);
        return agricultureClassMapper.insert(agricultureClass);
    }

    /**
     * 修改种类数据
     *
     * @param agricultureClass
     * @return
     */
    @Override
    public int editAgricultureClass(AgricultureClass agricultureClass) {
        validate(agricultureClass);
        return agricultureClassMapper.updateById(agricultureClass);
    }

    /**
     * 删除种类数据
     *
     * @param classId
     * @return
     */
    @Override
    public int delAgricultureClass(Long classId) {
        return agricultureClassMapper.deleteById(classId);
    }

    /**
     * 种类智能报告
     *
     * @param agricultureClass
     */
    @Override
    public void aiAddAgricultureClassReport(AgricultureClass agricultureClass) {
        String key = CacheConstants.AI_REPORT_NOT_REPEAT_SUBMIT + agricultureClass.getClassId();
        if (StringUtils.isNotEmpty((String) redisCache.getCacheObject(key))){
            throw new ServiceException(Constants.AI_NOT_REPEAT_SUBMIT_MESSAGE);
        }
        InputStream inputStream;
        ClassType classType = ClassType.findByCode(agricultureClass.getClassType());
        if (classType == ClassType.FISH) {
            inputStream = getClass().getClassLoader().getResourceAsStream("templates/ai/fish-type-content.txt");
        } else {
            inputStream = getClass().getClassLoader().getResourceAsStream("templates/ai/dish-type-content.txt");
        }
        TextDocumentParser parser = new TextDocumentParser();
        Document document = parser.parse(inputStream);
        String prompt = document.text().replace("%s", agricultureClass.getClassTypeName() + agricultureClass.getClassName());
        AiMessageVO aiMessageVO = AiMessageVO.builder()
                .id(agricultureClass.getClassId())
                .prompt(prompt)
                .createBy(SecurityUtils.getUsername())
                .classType(classType)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConstant.AI_EXCHANGE, RabbitMQConstant.AI_REPORT_KEY, aiMessageVO);
        redisCache.setCacheObject(key, agricultureClass.getClassName(), Constants.AI_NOT_REPEAT_SUBMIT_EXPIRATION, TimeUnit.MINUTES);
    }

    /**
     * 校验
     *
     * @param agricultureClass
     */
    private void validate(AgricultureClass agricultureClass) {
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<AgricultureClass> lambda = new QueryWrapper<AgricultureClass>().lambda();
        lambda
                .eq(AgricultureClass::getClassName, agricultureClass.getClassName());
        AgricultureClass info = agricultureClassMapper.selectOne(lambda);
        if (!ObjectUtils.isEmpty(info) && info.getClassId() != agricultureClass.getClassId()) {
            throw new ServiceException(AgricultureConstants.CLASS_NAME_EXIST);
        }
    }
}
