package jp.springbootreference.smarthttplogger;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
class HttpObject implements Serializable {

    private String method;

    private String url;

    private HashMap<String,String> requestHeaders = new HashMap<>();

    private String request;

    private int status;

    private HashMap<String,String> responseHeaders = new HashMap<>();

    private String response;


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
