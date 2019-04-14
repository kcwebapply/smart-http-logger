package jp.springbootreference.smarthttplogger;


import jp.springbootreference.smarthttplogger.config.SmartLoggerHeaderSecretsConfiguration;
import jp.springbootreference.smarthttplogger.config.SmartLoggerOutputConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class SmartLoggerFilter extends OncePerRequestFilter {

    private List<String> secretHeaders;

    private HashMap<String,Boolean> outputConfig;


    public SmartLoggerFilter(SmartLoggerOutputConfiguration outputConfiguration, SmartLoggerHeaderSecretsConfiguration secretsConfiguration){
        secretHeaders = secretsConfiguration.getSecrets();
        this.outputConfig = outputConfiguration.toMap();
    }


    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        HttpObject cache = new HttpObject();
        try {
            beforeRequest(cache,request, response);
            filterChain.doFilter(request, response);
        }
        finally {
            afterRequest(cache, request, response);
            response.copyBodyToResponse();
            LoggingPrinter.logging(cache,outputConfig,secretHeaders);
        }
    }

    protected void beforeRequest(HttpObject httpObject, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestHeader(httpObject, request);
        }
    }

    protected void afterRequest(HttpObject httpObject, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestBody(httpObject, request);
            logResponse(httpObject, response);
        }
    }

    private static void logRequestHeader(HttpObject httpObject, ContentCachingRequestWrapper request) {
        httpObject.setMethod(request.getMethod());

        String queryString = request.getQueryString();
        if (queryString == null) {
            httpObject.setURl(request.getRequestURI());
        } else {
            httpObject.setURl(request.getRequestURI()+ "?"+queryString);
        }
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                Collections.list(request.getHeaders(headerName)).forEach(headerValue ->{
                            httpObject.setRequestHeader(headerName,headerValue);
                })
        );
    }

    private static void logRequestBody(HttpObject httpObject, ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            MediaType mediaType = MediaType.valueOf(request.getContentType());
            boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
            if (visible) {
                try {
                    String contentString = new String(content, request.getCharacterEncoding());
                    httpObject.setRequest(contentString);
                } catch (UnsupportedEncodingException e) {
                }
            } else {
            }
        }
    }

    private static void logResponse(HttpObject httpObject, ContentCachingResponseWrapper response) {
        httpObject.setStatus(response.getStatus());
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue -> {
                            httpObject.setResponseHeader(headerName,headerValue);
                        })
        );

        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            MediaType mediaType = MediaType.valueOf(response.getContentType());
            boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
            if (visible) {
                try {
                    String contentString = new String(content, response.getCharacterEncoding());
                    httpObject.setResponse(contentString);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}