package jp.springbootreference.smarthttplogger;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
public class HttpObject implements Serializable {

    private String method;

    private String url;

    private HashMap<String,String> requestHeaders;

    private String request;

    private int status;

    private HashMap<String,String> responseHeaders;

    private String response;

}
