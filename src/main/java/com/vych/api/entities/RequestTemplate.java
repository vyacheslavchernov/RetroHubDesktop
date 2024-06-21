package com.vych.api.entities;

import lombok.Getter;

import java.util.Map;

/**
 * Template for request that can be performed by {@link com.vych.api.RequestApi#execute(RequestTemplate)}
 */
@Getter
public class RequestTemplate {
    private final String path;
    private final RequestMethod method;
    private final Map<String, String> headers;

    public RequestTemplate(String path, RequestMethod method, Map<String, String> headers) {
        this.path = path;
        this.method = method;
        this.headers = headers;
    }

    /**
     * Get request header
     *
     * @param header header name
     * @return header value
     */
    public String getHeader(String header) {
        return headers.get(header);
    }
}
