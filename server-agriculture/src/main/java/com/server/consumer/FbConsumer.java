package com.server.consumer;

import com.server.domain.AgriculturePasture;
import com.server.fisco.bcos.AgriculturePastureFB;
import com.server.mapper.AgriculturePastureMapper;
import com.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.server.constant.RabbitMQConstant.*;

@Slf4j
@Component
@ConditionalOnProperty(name = "fisco.enabled", havingValue = "true")
public class FbConsumer {

    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private Client client;

    /**
     * 大棚区块链下操作
     *
     * @param agriculturePasture
     */
    @RabbitListener(queues = FB_AP_QUEUE)
    public void jobProcess(AgriculturePasture agriculturePasture) {
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        if (StringUtils.isEmpty(agriculturePasture.getContractAddr())) {
            try {
                /* 部署合约 拿到合约地址 */
                AgriculturePastureFB agriculturePastureFB = AgriculturePastureFB.deploy(client, cryptoKeyPair);
                /* 调用合约 响应交易信息 */
                agriculturePastureFB.createPasture(
                        BigInteger.valueOf(agriculturePasture.getId()),
                        agriculturePasture.getName(),
                        agriculturePasture.getAddress(),
                        agriculturePasture.getArea(),
                        BigInteger.valueOf(agriculturePasture.getBigBreedingQuantity()),
                        BigInteger.valueOf(0),
                        agriculturePasture.getDescription()
                );
                agriculturePasture.setContractAddr(agriculturePastureFB.getContractAddress());
                agriculturePastureMapper.updateById(agriculturePasture);
            } catch (ContractException e) {
                log.error(e.getMessage());
            }
        } else {
            /* 加载合约 拿到合约地址 */
            AgriculturePastureFB agriculturePastureFB = AgriculturePastureFB.load(agriculturePasture.getContractAddr(), client, cryptoKeyPair);
            /* 调用合约 响应交易信息 */
            agriculturePastureFB.updatePasture(
                    BigInteger.valueOf(agriculturePasture.getId()),
                    agriculturePasture.getName(),
                    agriculturePasture.getAddress(),
                    agriculturePasture.getArea(),
                    agriculturePasture.getDescription()
            );
        }
    }
}
