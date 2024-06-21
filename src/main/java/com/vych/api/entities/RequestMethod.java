package com.vych.api.entities;

import lombok.Getter;

/**
 * Methods for http requests
 */
@Getter
public enum RequestMethod {
    GET("GET");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }
}
