package jp.springbootreference.smarthttplogger.config;

import jp.springbootreference.smarthttplogger.filter.SmartHttpFilter;
import jp.springbootreference.smarthttplogger.handler.SmartHttpHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SmartLoggerOutputConfiguration.class, SmartLoggerHeaderSecretsConfiguration.class})
public class SmartLoggerAutoConfig {

    @Bean
    public FilterRegistrationBean smartHttpFilter(SmartHttpHandler smartHttpHandler){
        SmartHttpFilter filter = new SmartHttpFilter(smartHttpHandler);
        FilterRegistrationBean bean = new FilterRegistrationBean(filter);
        return bean;
    }

    @Bean
    public SmartHttpHandler smartHttpHandler(SmartLoggerOutputConfiguration outputConfiguration, SmartLoggerHeaderSecretsConfiguration headerSecretsConfiguration){
        return new SmartHttpHandler(outputConfiguration,headerSecretsConfiguration);
    }

}
