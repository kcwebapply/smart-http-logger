package jp.springbootreference.smarthttplogger;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
class HttpObject implements Serializable {

    private String method;

    private String url;

    private String request;

    private HashMap<String,String> requestHeaders = new HashMap<>();

    private String response;

    private HashMap<String,String> responseHeaders = new HashMap<>();

    private int status;

    void setMethod(String method){this.method = method;}

    void setURl(String url){
        this.url = url;
    }

    void setRequestHeader(String key,String value){
        this.requestHeaders.put(key,value);
    }

    void setRequest(String request){
        this.request = request;
    }

    void setResponseHeader(String key,String value){
        this.requestHeaders.put(key,value);
    }

    void setResponse(String response){
        this.response = response;
    }

    void setStatus(int status){
        this.status = status;
    }

}
