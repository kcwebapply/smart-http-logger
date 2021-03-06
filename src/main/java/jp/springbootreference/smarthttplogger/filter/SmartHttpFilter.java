package jp.springbootreference.smarthttplogger.filter;


import jp.springbootreference.smarthttplogger.HttpObject;
import jp.springbootreference.smarthttplogger.handler.SmartHttpHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class SmartHttpFilter extends OncePerRequestFilter {


    private final SmartHttpHandler smartHttpHandler;

    public SmartHttpFilter(SmartHttpHandler smartHttpHandler){
        this.smartHttpHandler = smartHttpHandler;
    }
    /**/
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
        // if async , not working.
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = wrapRequest(request);
        ContentCachingResponseWrapper responseWrapper = wrapResponse(response);
        HttpObject httpInternalCache = new HttpObject();

        long start = System.currentTimeMillis();

        try {
            setMethod(httpInternalCache,requestWrapper);
            setRequestHeader(httpInternalCache,requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
        }
        finally {
            long time  = System.currentTimeMillis() - start;
            setRequestBody(httpInternalCache, requestWrapper);
            setResponseStatus(httpInternalCache,responseWrapper);
            setResponseHeader(httpInternalCache,responseWrapper);
            setResponseBody(httpInternalCache,responseWrapper);
            responseWrapper.copyBodyToResponse();
            smartHttpHandler.handle(httpInternalCache,time);
        }


    }


    protected static void setMethod(HttpObject httpObject, ContentCachingRequestWrapper request){
        httpObject.setMethod(request.getMethod());
    }

    protected static void setRequestHeader(HttpObject httpObject, ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            httpObject.setUrl(request.getRequestURI());
        } else {
            httpObject.setUrl(request.getRequestURI()+ "?"+queryString);
        }
        final HashMap<String,String> header = new HashMap<>();
        Collections.list(request.getHeaderNames()).stream().forEach(headerName ->
            header.put(headerName,request.getHeader(headerName))
        );
        httpObject.setRequestHeaders(header);

    }

    private static void setRequestBody(HttpObject httpObject, ContentCachingRequestWrapper request) {
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
            }
        }
    }

    private static void setResponseStatus(HttpObject httpObject, ContentCachingResponseWrapper response){
        httpObject.setStatus(response.getStatus());
    }

    private static void setResponseHeader(HttpObject httpObject, ContentCachingResponseWrapper response){
        final HashMap<String,String> header = new HashMap<>();
        response.getHeaderNames().forEach(headerName ->
               header.put(headerName,response.getHeader(headerName))
        );
        httpObject.setResponseHeaders(header);
    }

    private static void setResponseBody(HttpObject httpObject, ContentCachingResponseWrapper response) {
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