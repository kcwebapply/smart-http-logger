package jp.springbootreference.smarthttplogger.logging;

import jp.springbootreference.smarthttplogger.HttpObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartHttpLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartHttpLogger.class);
    private static final String secretExpression = "xxxxxxxxxxxxxxxx";

    public static void logging(HttpObject httpObject, HashMap<String,Boolean> outputConfig, List<String> secretHeaders, long time){ ;
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (Field field : httpObject.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final String fieldName = field.getName();
                final Object fieldValue;
                if(fieldName.equals("requestHeaders") || fieldName.equals("responseHeaders")){
                    fieldValue = getHeadersString((HashMap<String, String>) field.get(httpObject),secretHeaders);
                }else{
                    fieldValue = field.get(httpObject);
                }

                if(outputConfig.get(fieldName)==true){
                    sb.append("'"+fieldName+ "' = '" + fieldValue + "', ");
                }
            } catch (IllegalAccessException e) {
                sb.append(field.getName() + " = " + "access denied\n");
            }
        }

        sb.append("'time' = '"+time+"ms'");
        sb.append(" ]");

        final int status = httpObject.getStatus();
        if(HttpStatus.valueOf(status).is2xxSuccessful()){
            LOGGER.info(sb.toString());
        }else{
            LOGGER.error(sb.toString());
        }

    }

    private  static String getHeadersString(HashMap<String,String> headers, List<String> secretHeaders){
        final StringBuffer buffer = new StringBuffer("{");
        List<String> HeaderSets = new ArrayList<>();
        headers.forEach((key,value)->{
            value = !secretHeaders.contains(key)?value:secretExpression;
            HeaderSets.add("\""+key + "\":\""+value+"\"");
        });
        buffer.append(String.join(",",HeaderSets));
        buffer.append("}");
        return buffer.toString();
    }

}
