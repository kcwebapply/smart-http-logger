package jp.springbootreference.smarthttplogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class LoggingPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPrinter.class);
   /* private static final String LoggingFormat =
            "\n" + "{\n" +
                    "  \"REQUEST\":{\n"+
                    "    \"METHOD:\":\"%s\",\n" +
                    "    \"URL\":\"%s\",\n" +
                    "    \"HEADERS\":%s,\n" +
                    "    \"BODY\":%s\n" +
                    "  },"+
                    "\n\n"+
                    "  \"RESPONSE\":{\n"+
                    "    \"STATUS\":%d,\n" +
                    "    \"HEADERS\":%s,\n" +
                    "    \"BODY\":%s\n" +
                    "  }\n" +
                    "}";*/

    private static final String LoggingFormat =
                    "[ \"%s \"%s\" %d \n" +
                    " %s %s,\n %s, %s\"]";


    private static final String secretExpression = "xxxxxxxxxxxxxxxx";




    static void logging(HttpObject httpObject, HashMap<String,Boolean> outputConfiguration, List<String> secretHeaders,long time){


        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        /*for (Field field : httpObject.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final String fieldName = field.getName();
                if(outputConfiguration.get(fieldName)==true){
                    sb.append("\""+fieldName+ "\" = \"" + field.get(httpObject) + "\", ");
                }
            } catch (IllegalAccessException e) {
                sb.append(field.getName() + " = " + "access denied\n");
            }
        }*/

        final List<String> outputParameters = Arrays.stream(httpObject.getClass().getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    return outputConfiguration.get(field.getName());
                }).map(field -> {
            try {
                return field.get(httpObject) + "";//"\""+field.getName()+"\":\""+field.get(httpObject)+"\" ";
            } catch (IllegalAccessException e) {
                return null;
            }
        }).collect(Collectors.toList());
        outputParameters.add(String.valueOf(time));
        final String expression = String.join(" ",outputParameters);
        sb.append(expression);
        sb.append(" ]");

        final int status = httpObject.getStatus();


        LOGGER.info(sb.toString());


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
