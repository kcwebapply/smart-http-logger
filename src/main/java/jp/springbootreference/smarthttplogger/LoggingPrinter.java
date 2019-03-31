package jp.springbootreference.smarthttplogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LoggingPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPrinter.class);

    private static final String LoggingFormat = "\n{\n \"url\":\"%s\",\n \"requestHeaders\":%s,\n \"requestBody\":%s,\n \"responseHeaders\":%s,\n \"httpStatus\":%d,\n \"responseBody\":%s\n}";

    private static final String secretExpression = "xxxxxxxxxxxxxxxx";

    static void logging(LogCache logCache,List<String> secretHeaders){
        final String LOGGING_FORMAT = String.format(
                LoggingFormat,
                logCache.getRequestUrl(),
                getHeadersString(logCache.getRequestHeaders(), secretHeaders),
                logCache.getRequestBody(),
                getHeadersString(logCache.getResponseHeaders(), secretHeaders),
                logCache.getResponseStatus(),
                logCache.getResponseBody()
        );

        LOGGER.info(LOGGING_FORMAT);

    }

    private static String getHeadersString(HashMap<String,String> headers, List<String> secretHeaders){
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
