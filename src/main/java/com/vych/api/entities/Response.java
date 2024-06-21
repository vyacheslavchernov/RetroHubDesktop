package com.vych.api.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.FileOutputStream;

import static com.vych.api.entities.Headers.ACCEPT;
import static com.vych.api.entities.MediaTypes.JSON;

/**
 * Response that was produced by request performed in {@link com.vych.api.RequestApi}
 */
@Getter
@Setter
public class Response {
    @Setter(AccessLevel.NONE)
    private byte[] rawBody;

    @Setter(AccessLevel.NONE)
    private int statusCode;

    @Setter(AccessLevel.NONE)
    private RequestTemplate request;

    public Response(byte[] rawBody, int statusCode, RequestTemplate request) {
        this.rawBody = rawBody;
        this.statusCode = statusCode;
        this.request = request;
    }

    /**
     * Get response body as json if possible
     *
     * @return json or null if response type not {@link MediaTypes#JSON}
     */
    public String getJson() {
        String accept = request.getHeader(ACCEPT);

        if (accept == null) {
            return null;
        }
        if (!JSON.equals(accept)) {
            return null;

        }

        return new String(rawBody);
    }

    /**
     * Parse json from response body,
     * cast it to object and return it.
     *
     * @param valueType expected body type
     * @return parsed json as casted object
     */
    @SneakyThrows
    public <T> T getParsed(Class<T> valueType) {
        String json = getJson();
        if (json == null) {
            return null;
        }

        return new ObjectMapper().readValue(json, valueType);
    }

    /**
     * Parse json from response body,
     * cast it to object and return it.
     *
     * @param valueTypeRef expected body type
     * @return parsed json as casted object
     */
    @SneakyThrows
    public <T> T getParsed(TypeReference<T> valueTypeRef) {
        String json = getJson();
        if (json == null) {
            return null;
        }

        return new ObjectMapper().readValue(json, valueTypeRef);
    }

    /**
     * Save response body as file
     *
     * @param path path to new file
     */
    @SneakyThrows
    public void saveAsFile(String path) {
        FileOutputStream outputStream = new FileOutputStream(path);
        outputStream.write(rawBody);
        outputStream.close();
    }
}
