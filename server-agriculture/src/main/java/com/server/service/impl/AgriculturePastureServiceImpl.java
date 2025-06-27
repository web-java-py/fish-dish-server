package com.server.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.core.domain.AjaxResult;
import com.server.core.text.Convert;
import com.server.domain.AgricultureClass;
import com.server.exception.ServiceException;
import com.server.fisco.bcos.AgriculturePastureFB;
import com.server.mapper.AgriculturePastureMapper;
import com.server.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.domain.AgriculturePasture;
import com.server.service.AgriculturePastureService;

import static com.server.constant.RabbitMQConstant.*;

/**
 * 大棚Service业务层处理
 *
 * @author server
 * @date 2025-05-27
 */
@Service
public class AgriculturePastureServiceImpl extends ServiceImpl<AgriculturePastureMapper, AgriculturePasture> implements AgriculturePastureService {
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired(required = false)
    private Client client;

    @Value("${fisco.enabled}")
    private String fiscoEnabled;

    /**
     * 查询大棚
     *
     * @param id 大棚主键
     * @return 大棚
     */
    @Override
    public AgriculturePasture selectAgriculturePastureById(Long id) {
        return getById(id);
    }

    /**
     * 查询大棚列表
     *
     * @param agriculturePasture 大棚
     * @return 大棚
     */
    @Override
    public List<AgriculturePasture> selectAgriculturePastureList(AgriculturePasture agriculturePasture) {
        return list();
    }

    /**
     * 新增大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture) {
        validate(agriculturePasture);
        if (Convert.toBool(fiscoEnabled)){
            try {
                /* 部署合约 拿到合约地址 */
                AgriculturePastureFB agriculturePastureFB = AgriculturePastureFB.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
                /* 调用合约 响应交易信息 */
                agriculturePastureFB.createPasture(
                        agriculturePasture.getName(),
                        agriculturePasture.getAddress(),
                        agriculturePasture.getArea(),
                        BigInteger.valueOf(agriculturePasture.getBigBreedingQuantity()),
                        BigInteger.valueOf(0),
                        agriculturePasture.getDescription()
                );
                agriculturePasture.setContractAddr(agriculturePastureFB.getContractAddress());
            } catch (ContractException e) {
                log.error(e.getMessage());
            }
        }
        return agriculturePastureMapper.insert(agriculturePasture);
    }

    /**
     * 修改大棚
     *
     * @param agriculturePasture 大棚
     * @return 结果
     */
    @Override
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture) {
        validate(agriculturePasture);
        int update = agriculturePastureMapper.updateById(agriculturePasture);
        agriculturePasture.setUpdateBy(SecurityUtils.getUsername());
        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agriculturePasture);
        return update;
    }

    /**
     * 批量删除大棚
     *
     * @param ids 需要删除的大棚主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除大棚信息
     *
     * @param id 大棚主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 校验
     *
     * @param agriculturePasture
     */
    private void validate(AgriculturePasture agriculturePasture) {
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<AgriculturePasture> lambda = new QueryWrapper<AgriculturePasture>().lambda();
        lambda
                .eq(AgriculturePasture::getName, agriculturePasture.getName());
        AgriculturePasture info = agriculturePastureMapper.selectOne(lambda);
        if (!ObjectUtils.isEmpty(info) && info.getId() != agriculturePasture.getId()) {
            throw new ServiceException(AgricultureConstants.Pasture_NAME_EXIST);
        }
    }
}
