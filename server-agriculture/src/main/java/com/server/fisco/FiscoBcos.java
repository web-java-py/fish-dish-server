package com.server.fisco;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "fisco.enabled", havingValue = "true")
public class FiscoBcos {

    @Bean
    public Client client() {
        ConfigProperty configProperty = loadProperty();
        if (configProperty == null) {
            throw new RuntimeException("区块链配置文件加载失败");
        }

        ConfigOption configOption;
        try {
            configOption = new ConfigOption(configProperty);
        } catch (ConfigException e) {
            log.error("区块链配置初始化失败: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        BcosSDK bcosSDK = new BcosSDK(configOption);
        return bcosSDK.getClient(Integer.valueOf(1));
    }

    @Bean
    public CryptoKeyPair cryptoKeyPair() {
        return client().getCryptoSuite().getCryptoKeyPair();
    }

    private ConfigProperty loadProperty() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(representer);
        String configFile = "/fisco/fisco-config.yml";

        try (InputStream inputStream = this.getClass().getResourceAsStream(configFile)) {
            if (inputStream == null) {
                log.error("区块链配置文件不存在: {}", configFile);
                return null;
            }
            return yaml.loadAs(inputStream, ConfigProperty.class);
        } catch (Exception e) {
            log.error("区块链加载配置文件时发生错误: {}", e.getMessage());
            return null;
        }
    }
}
