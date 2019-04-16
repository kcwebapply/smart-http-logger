package jp.springbootreference.smarthttplogger.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "smarthlog.output")
public class SmartLoggerOutputConfiguration {

    @Getter
    @Setter
    private boolean method = true;
    @Getter
    @Setter
    private boolean url = true;
    @Getter
    @Setter
    private boolean request = true;
    @Getter
    @Setter
    private boolean  requestHeaders = true;
    @Getter
    @Setter
    private boolean response = true;
    @Getter
    @Setter
    private boolean responseHeaders = true;
    @Getter
    @Setter
    private boolean status = true;


    public HashMap<String,Boolean> toMap(){
        final HashMap<String,Boolean> map = new HashMap();
        for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
            try {
                map.put(field.getName(), (Boolean) field.get(this));
            } catch (IllegalAccessException e) {
                map.put(field.getName(),false);
            }
        }

        return map;
    }

}
