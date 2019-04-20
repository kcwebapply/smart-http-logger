package jp.springbootreference.smarthttplogger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "smartlog.header")
public class SmartLoggerHeaderSecretsConfiguration {

    @Getter
    @Setter
    private List<String> secrets;

}
