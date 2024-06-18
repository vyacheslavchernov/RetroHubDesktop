package com.vych.database;

import com.vych.database.accessors.SettingsAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * App local database.
 * Store app settings and other stuff.
 */
public class AppDatabase {
    public static final String DATABASE_NAME = "app.sqlite";

    private static Connection connection = null;
    private static SettingsAccessor settingsAccessor = null;

    /**
     * Get accessor to settings table in local DB
     *
     * @return accessor
     */
    public static SettingsAccessor getSettings() {
        if (settingsAccessor == null) {
            settingsAccessor = new SettingsAccessor(getConnection());
        }
        return settingsAccessor;
    }

    /**
     * Get connection to local DB.
     * If not exist, create one.
     *
     * @return created connection
     */
    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
