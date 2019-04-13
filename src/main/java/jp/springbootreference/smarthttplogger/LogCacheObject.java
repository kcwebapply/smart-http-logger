package jp.springbootreference.smarthttplogger;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
class LogCacheObject implements Serializable {

    private String method;

    private String requestUrl;

    private String requestBody;

    private HashMap<String,String> requestHeaders = new HashMap<>();

    private String responseBody;

    private HashMap<String,String> responseHeaders = new HashMap<>();

    private int responseStatus;

    void setMethod(String method){this.method = method;}

    void setRequestURl(String requestUrl){
        this.requestUrl = requestUrl;
    }

    void setRequestHeader(String key,String value){
        this.requestHeaders.put(key,value);
    }

    void setRequestBody(String requestBody){
        this.requestBody = requestBody;
    }

    void setResponseHeader(String key,String value){
        this.requestHeaders.put(key,value);
    }

    void setResponseBody(String responseBody){
        this.responseBody = responseBody;
    }

    void setResponseStatus(int responseStatus){
        this.responseStatus = responseStatus;
    }

}
