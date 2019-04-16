package jp.springbootreference.smarthttplogger.handler;

import jp.springbootreference.smarthttplogger.HttpObject;
import jp.springbootreference.smarthttplogger.config.SmartLoggerHeaderSecretsConfiguration;
import jp.springbootreference.smarthttplogger.config.SmartLoggerOutputConfiguration;
import jp.springbootreference.smarthttplogger.logging.SmartHttpLogger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class SmartHttpHandler {

    private final List<String> secretHeaders;

    private final HashMap<String,Boolean> outputConfig;

    public SmartHttpHandler(SmartLoggerOutputConfiguration outputConfiguration, SmartLoggerHeaderSecretsConfiguration secretsConfiguration){
        secretHeaders = secretsConfiguration.getSecrets();
        this.outputConfig = outputConfiguration.toMap();
    }


    public void handle(HttpObject httpObject, long time){
        SmartHttpLogger.logging(httpObject,outputConfig,secretHeaders,time);
    }


}
