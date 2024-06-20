package com.vych.database.accessors;

import com.vych.database.SettingsDefaults;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

/**
 * Accessor for settings table in local DB.
 * Provide access to it.
 */
public class SettingsAccessor extends BasicAccessor {
    public final static String TABLE_NAME = "settings";
    public final static String SETTING_COLUMN = "setting";
    public final static String VALUE_COLUMN = "value";

    public SettingsAccessor(Connection connection) {
        super(connection);
    }

    /**
     * Get setting value from DB
     *
     * @param setting target
     * @return target setting value
     */
    public String get(String setting) {
        List<HashMap<String, Object>> result = performQuery(
                "SELECT * FROM \"%s\" WHERE \"%s\" == \"%s\"",
                TABLE_NAME, SETTING_COLUMN, setting
        );

        if (result.isEmpty()) {
            return null;
        }
        return (String) result.get(0).get(VALUE_COLUMN);
    }

    /**
     * Set setting value in DB
     *
     * @param setting target
     * @param value   value to set
     */
    public void set(String setting, String value) {
        performQuery(
                "UPDATE \"%s\" SET \"%s\" = \"%s\" WHERE \"%s\" == \"%s\";",
                TABLE_NAME, VALUE_COLUMN, value, SETTING_COLUMN, setting
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
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, '%s' text, '%s' text);",
                TABLE_NAME, SETTING_COLUMN, VALUE_COLUMN
        );

        for (SettingsDefaults setting : SettingsDefaults.values()) {
            if (
                    performQuery(
                            "SELECT * FROM '%s' WHERE \"%s\" == \"%s\"",
                            TABLE_NAME, SETTING_COLUMN, setting.getName()
                    ).isEmpty()
            ) {
                performQuery(
                        "INSERT INTO '%s' ('%s', '%s') VALUES ('%s', '%s');",
                        TABLE_NAME, SETTING_COLUMN, VALUE_COLUMN, setting.getName(), setting.getDefaultValue()
                );
            }
        }
    }
}
