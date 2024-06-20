package com.vych.database.accessors;

import com.vych.cache.CachedItem;
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
    public final static String TABLE_NAME = "cache";
    public final static String KEY_COLUMN = "key";
    public final static String VALUE_COLUMN = "value";
    public final static String EXPIRED_AFTER_COLUMN = "expired_after";

    public CacheAccessor(Connection connection) {
        super(connection);
    }

    /**
     * Get cached value from DB
     *
     * @param key target
     * @return target setting value
     */
    public CachedItem get(String key) {
        List<HashMap<String, Object>> result = performQuery(
                "SELECT * FROM \"%s\" WHERE \"%s\" == \"%s\"",
                TABLE_NAME, KEY_COLUMN, key
        );

        if (result.isEmpty()) {
            return null;
        }

        return new CachedItem()
                .setKey((String) result.get(0).get(KEY_COLUMN))
                .setValue((String) result.get(0).get(VALUE_COLUMN))
                .setExpiredAfter(LocalDateTime.parse((String) result.get(0).get(EXPIRED_AFTER_COLUMN)));
    }

    /**
     * Add cached value to DB
     *
     * @param item item to add
     */
    public void add(CachedItem item) {
        performQuery(
                "INSERT INTO '%s' ('%s', '%s', '%s') VALUES ('%s', '%s', '%s');",
                TABLE_NAME, KEY_COLUMN, VALUE_COLUMN, EXPIRED_AFTER_COLUMN,
                item.getKey(), item.getValue(), item.getExpiredAfter().toString()
        );
    }

    /**
     * Remove cached value from DB
     *
     * @param key item key to remove
     */
    public void remove(String key) {
        performQuery(
                "DELETE FROM \"%s\" WHERE \"%s\" == \"%s\"",
                TABLE_NAME, KEY_COLUMN, key
        );
    }


    /**
     * Check existence of settings table in DB,
     * create and filling with default values if not.
     */
    @SneakyThrows
    public void validateDatabase() {
        performQuery(
                "CREATE TABLE if not exists '%s' " +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, '%s' text, '%s' text, '%s' text);",
                TABLE_NAME, KEY_COLUMN, VALUE_COLUMN, EXPIRED_AFTER_COLUMN
        );
    }
}
