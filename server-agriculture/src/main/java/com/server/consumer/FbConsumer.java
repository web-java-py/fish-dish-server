package com.server.consumer;

import com.server.domain.AgricultureDevice;
import com.server.domain.AgriculturePasture;
import com.server.fisco.bcos.AgricultureDeviceFB;
import com.server.fisco.bcos.AgriculturePastureFB;
import com.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


import static com.server.constant.RabbitMQConstant.*;

@Slf4j
@Component
@ConditionalOnProperty(name = "fisco.enabled", havingValue = "true")
@RabbitListener(queues = FB_QUEUE)
public class FbConsumer {

    @Autowired
    private Client client;

    @Autowired
    private CryptoKeyPair cryptoKeyPair;

    /**
     * 设备区块链操作
     *
     * @param agricultureDevice
     */
    @RabbitHandler
    public void agricultureDeviceProcess(AgricultureDevice agricultureDevice) {
        if (StringUtils.isNotEmpty(agricultureDevice.getBlockAddress())) {
            /* 加载合约 拿到合约地址 */
            AgricultureDeviceFB agricultureDeviceFB = AgricultureDeviceFB.load(agricultureDevice.getBlockAddress(), client, cryptoKeyPair);
            /* 调用合约 响应交易信息 */
            agricultureDeviceFB.updateDevice(
                    agricultureDevice.getPastureId(),
                    agricultureDevice.getBatchId(),
                    agricultureDevice.getDeviceTypeId(),
                    agricultureDevice.getDeviceName()
            );
        }
    }

    /**
     * 大棚区块链操作
     *
     * @param agriculturePasture
     */
    @RabbitHandler
    public void agriculturePastureProcess(AgriculturePasture agriculturePasture) {
        if (StringUtils.isNotEmpty(agriculturePasture.getContractAddr())) {
            /* 加载合约 拿到合约地址 */
            AgriculturePastureFB agriculturePastureFB = AgriculturePastureFB.load(agriculturePasture.getContractAddr(), client, cryptoKeyPair);
            /* 调用合约 响应交易信息 */
            agriculturePastureFB.updatePasture(
                    agriculturePasture.getName(),
                    agriculturePasture.getAddress(),
                    agriculturePasture.getArea(),
                    agriculturePasture.getDescription()
            );
        }
    }
}
