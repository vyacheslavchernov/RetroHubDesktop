package com.vych.cache;

import com.vych.database.AppDatabase;
import com.vych.database.accessors.CacheAccessor;
import com.vych.utils.FilesUtils;

import java.time.LocalDateTime;

/**
 * Realisation of app cache.
 * Can store, get items.
 * Remove items if they are expired.
 */
public class AppCache {
    public final static long CACHED_REQUEST_LIFETIME_SEC = 300;
    public final static long CACHED_IMAGE_LIFETIME_SEC = 300;

    private final static CacheAccessor CACHE = AppDatabase.getCache();

    /**
     * Cache request value
     *
     * @param key      cached item key, to access it later
     * @param value    cached value
     * @param lifetime how long it will be live in cache from moment it was cached
     */
    public static void cacheRequest(String key, String value, long lifetime) {
        cacheItem(key, value, lifetime, CacheType.REQUEST);
    }

    /**
     * Get cached request value.
     * Will be return null if value was expired.
     *
     * @param key cached request key
     * @return cached value or null
     */
    public static String getRequest(String key) {
        CacheItem item = CACHE.get(key, CacheType.REQUEST);

        if (item == null) {
            return null;
        }

        if (LocalDateTime.now().isAfter(item.getExpiredAfter())) {
            CACHE.remove(key, CacheType.REQUEST);
            return null;
        }
        return item.getValue();
    }

    /**
     * Cache image path
     *
     * @param key      cached item key, to access it later
     * @param path     cached path
     * @param lifetime how long it will be live in cache from moment it was cached
     */
    public static void cacheImage(String key, String path, long lifetime) {
        cacheItem(key, path, lifetime, CacheType.IMAGE);
    }

    /**
     * Get cached image path.
     * Will be return null if value was expired.
     *
     * @param key cached request key
     * @return cached value or null
     */
    public static String getCachedImage(String key) {
        CacheItem item = CACHE.get(key, CacheType.IMAGE);

        if (item == null) {
            return null;
        }

        if (LocalDateTime.now().isAfter(item.getExpiredAfter())) {
            CACHE.remove(key, CacheType.IMAGE);
            return null;
        }

        if (!FilesUtils.isFileExist(item.getValue())) {
            CACHE.remove(key, CacheType.IMAGE);
            return null;
        }

        return item.getValue();
    }

    /**
     * Cache some value to specific cache table based on item type
     *
     * @param key      cached item key, to access it later
     * @param value    cached value
     * @param lifetime how long it will be live in cache from moment it was cached
     * @param type     cached item type
     */
    private static void cacheItem(String key, String value, long lifetime, CacheType type) {
        CACHE.add(
                new CacheItem()
                        .setKey(key)
                        .setValue(value)
                        .setExpiredAfter(LocalDateTime.now().plusSeconds(lifetime)),
                type
        );
    }
}
