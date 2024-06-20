package com.vych.cache;

import com.vych.database.AppDatabase;
import com.vych.database.accessors.CacheAccessor;

import java.time.LocalDateTime;

/**
 * Realisation of app cache.
 * Can store, get items.
 * Remove items if they are expired.
 */
public class AppCache {
    private final static CacheAccessor CACHE = AppDatabase.getCache();

    /**
     * Cache are string type value
     *
     * @param key cached item key, to access it later
     * @param value cached value
     * @param lifetime how long it will be live in cache from moment it was cached
     */
    public static void cacheString(String key, String value, long lifetime) {
        CACHE.add(new CachedItem()
                .setKey(key)
                .setValue(value)
                .setExpiredAfter(LocalDateTime.now().plusSeconds(lifetime))
        );
    }

    /**
     * Get item value from cache.
     * Will be return null if value was expired.
     *
     * @param key cached item key
     * @return cached valuer or null
     */
    public static String getString(String key) {
        CachedItem item = CACHE.get(key);

        if (item == null) {
            return null;
        }

        if (LocalDateTime.now().isAfter(item.getExpiredAfter())) {
            CACHE.remove(key);
            return null;
        }
        return item.getValue();
    }
}
