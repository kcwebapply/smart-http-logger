package jp.springbootreference.smarthttplogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LoggingPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPrinter.class);

    /*private static final String LoggingFormat =
            "\n" + "{\n" +
                    "  \"REQUEST\":{\n"+
                    "    \"METHOD:\":\"%s\",\n" +
                    "    \"URL\":\"%s\",\n" +
                    "    \"HEADERS\":%s,\n" +
                    "    \"BODY\":%s\n" +
                    "  },"+
                    "\n\n"+
                    "  \"RESPONSE\":{\n"+
                    "    \"HEADERS\":%s,\n" +
                    "    \"STATUS\":%d,\n" +
                    "    \"BODY\":%s\n" +
                    "  }\n" +
                    "}";*/

    private static final String LoggingFormat =
                    "[ \"%s \"%s\" %d \n" +
                    " %s %s,\n %s, %s\"]";


    private static final String secretExpression = "xxxxxxxxxxxxxxxx";




    static void logging(LogCacheObject logCache, List<String> secretHeaders){

        final int status = logCache.getResponseStatus();
        final String LOGGING_FORMAT = String.format(
                LoggingFormat,
                logCache.getMethod(),
                logCache.getRequestUrl(),
                logCache.getResponseStatus(),
                getHeadersString(logCache.getRequestHeaders(), secretHeaders),
                logCache.getRequestBody(),
                getHeadersString(logCache.getResponseHeaders(), secretHeaders),
                logCache.getResponseBody()
        );

        if(status == HttpStatus.OK.value()){
            LOGGER.info(LOGGING_FORMAT);
        }else{
            LOGGER.error(LOGGING_FORMAT);
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
