package com.vych.database.accessors;

import com.vych.cache.CacheItem;
import com.vych.cache.CacheType;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Accessor for cache table in local DB.
 * Provide access to it.
 */
public class CacheAccessor extends BasicAccessor {
    public final static String REQUESTS_TABLE_NAME = "cached_requests";
    public final static String IMAGES_TABLE_NAME = "cached_images";
    public final static String KEY_COLUMN = "key";
    public final static String VALUE_COLUMN = "value";
    public final static String EXPIRED_AFTER_COLUMN = "expired_after";

    public CacheAccessor(Connection connection) {
        super(connection);
    }

    /**
     * Get cached value from specific table from DB
     *
     * @param key  target
     * @param type target item type
     * @return target setting value
     */
    public CacheItem get(String key, CacheType type) {
        List<HashMap<String, Object>> result = performQuery(
                "SELECT * FROM \"%s\" WHERE \"%s\" == \"%s\"",
                getTargetTable(type), KEY_COLUMN, key
        );

        if (result.isEmpty()) {
            return null;
        }

        return new CacheItem()
                .setKey((String) result.get(0).get(KEY_COLUMN))
                .setValue((String) result.get(0).get(VALUE_COLUMN))
                .setExpiredAfter(LocalDateTime.parse((String) result.get(0).get(EXPIRED_AFTER_COLUMN)));
    }

    /**
     * Add cached value to specific table in DB
     *
     * @param item item to add
     * @param type target item type
     */
    public void add(CacheItem item, CacheType type) {
        performQuery(
                "INSERT INTO '%s' ('%s', '%s', '%s') VALUES ('%s', '%s', '%s');",
                getTargetTable(type), KEY_COLUMN, VALUE_COLUMN, EXPIRED_AFTER_COLUMN,
                item.getKey(), item.getValue(), item.getExpiredAfter().toString()
        );
    }

    /**
     * Remove cached value from specific table in DB
     *
     * @param key  item key to remove
     * @param type target item type
     */
    public void remove(String key, CacheType type) {
        performQuery(
                "DELETE FROM \"%s\" WHERE \"%s\" == \"%s\"",
                getTargetTable(type), KEY_COLUMN, key
        );
    }


    /**
     * Check existence of cache tables in DB,
     * create and filling with default values if not.
     */
    @SneakyThrows
    public void validateDatabase() {
        performQuery(
                "CREATE TABLE if not exists '%s' " +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, '%s' text, '%s' text, '%s' text);",
                REQUESTS_TABLE_NAME, KEY_COLUMN, VALUE_COLUMN, EXPIRED_AFTER_COLUMN
        );

        performQuery(
                "CREATE TABLE if not exists '%s' " +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, '%s' text, '%s' text, '%s' text);",
                IMAGES_TABLE_NAME, KEY_COLUMN, VALUE_COLUMN, EXPIRED_AFTER_COLUMN
        );
    }

    /**
     * Get cache table name based on cached item type
     *
     * @param type cached item type
     * @return target table name
     */
    private String getTargetTable(CacheType type) {
        return switch (type) {
            case REQUEST -> REQUESTS_TABLE_NAME;
            case IMAGE -> IMAGES_TABLE_NAME;
        };
    }
}
