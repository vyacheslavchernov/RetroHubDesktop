package com.vych.api;

import com.vych.api.entities.RequestMethod;
import com.vych.api.entities.RequestTemplate;
import com.vych.api.entities.Response;
import com.vych.cache.AppCache;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.vych.api.entities.Headers.ACCEPT;
import static com.vych.api.entities.MediaTypes.JSON;
import static com.vych.cache.AppCache.CACHED_REQUEST_LIFETIME_SEC;

/**
 * Perform http requests
 */
public class RequestApi {
    public final static int CONNECTION_TIMEOUT_MS = 6000;

    @Getter
    private final static RequestApi instance = new RequestApi();

    /**
     * Send a request over http protocol
     *
     * @param template template of request that need to be sent
     * @return response from performed request
     */
    @SneakyThrows
    public Response execute(RequestTemplate template) {
        boolean cacheable = JSON.equals(template.getHeader(ACCEPT));
        if (cacheable) {
            String cached = AppCache.getRequest(template.getPath());
            if (cached != null) {
                return new Response(cached.getBytes(), 200, template);
            }
        }

        URL url = new URL(template.getPath());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(template.getMethod().getValue());
        con.setUseCaches(false);
        con.setConnectTimeout(CONNECTION_TIMEOUT_MS);

        for (String header : template.getHeaders().keySet()) {
            con.setRequestProperty(header, template.getHeaders().get(header));
        }

        if (con.getResponseCode() != 200) {
            return new Response(null, con.getResponseCode(), template);
        }

        InputStream inputStream = con.getInputStream();
        byte[] resultBytes = inputStream.readAllBytes();

        if (cacheable) {
            AppCache.cacheRequest(template.getPath(), new String(resultBytes), CACHED_REQUEST_LIFETIME_SEC);
        }

        inputStream.close();
        con.disconnect();
        return new Response(resultBytes, con.getResponseCode(), template);
    }

    /**
     * Builder for requests templates
     *
     * @return instance of builder
     */
    public RequestBuilder getRequestBuilder() {
        return new RequestBuilder();
    }

    @Setter
    @Accessors(chain = true)
    public class RequestBuilder {
        private String path = "";
        private RequestMethod method = RequestMethod.GET;
        private Map<String, String> headers = new HashMap<>();

        /**
         * Add header to request
         *
         * @param header header name
         * @param value  header value
         * @return this instance
         */
        public RequestBuilder addHeader(String header, String value) {
            headers.put(header, value);
            return this;
        }

        /**
         * Build formed template
         *
         * @return request template
         */
        public RequestTemplate build() {
            return new RequestTemplate(path, method, headers);
        }
    }


}
