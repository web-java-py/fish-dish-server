package com.server.fisco;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
@Slf4j
@ConditionalOnProperty(name = "fisco.enabled", havingValue = "true")
public class FiscoBcos {

    @PostConstruct
    @Bean
    public Client client() {
        ConfigProperty configProperty = loadProperty();
        ConfigOption configOption;
        try {
            configOption = new ConfigOption(configProperty);
        } catch (ConfigException e) {
            log.error(e.getMessage());
            return null;
        }
        BcosSDK bcosSDK = new BcosSDK(configOption);
        return bcosSDK.getClient(Integer.valueOf(1));
    }

    public ConfigProperty loadProperty() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(representer);
        String configFile = "/fisco/fisco-config.yml";
        try (InputStream inputStream = this.getClass().getResourceAsStream(configFile)) {
            return yaml.loadAs(inputStream, ConfigProperty.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
