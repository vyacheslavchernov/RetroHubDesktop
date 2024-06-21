package com.vych.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * An item that can be cached
 */
@Getter
@Setter
@Accessors(chain = true)
public class CacheItem {
    private String key;
    private String value;
    private LocalDateTime expiredAfter;
}
