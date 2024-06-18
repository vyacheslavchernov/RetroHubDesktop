package com.vych.database.accessors;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Accessor for settings table in local DB.
 * Provide access to it.
 */
// TODO: interface maybe?
public class SettingsAccessor {
    public final static String TABLE_NAME = "settings";
    public final static String SETTING_COLUMN_NAME = "setting";
    public final static String VALUE_COLUMN_NAME = "value";

    public final static String REPOSITORY_IP_SETTING = "repository_ip";

    private final Connection connection;

    public SettingsAccessor(Connection connection) {
        this.connection = connection;
        validateDatabase();
    }

    /**
     * Get setting value from DB
     *
     * @param setting target
     * @return target setting value
     */
    public String getString(String setting) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format(
                    "SELECT * FROM \"%s\" WHERE \"%s\" == \"%s\"", TABLE_NAME, SETTING_COLUMN_NAME, setting
            ));
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getString(VALUE_COLUMN_NAME);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get setting value in DB
     *
     * @param setting target
     * @param value   value to set
     */
    public void setString(String setting, String value) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format(
                    "UPDATE \"%s\" SET \"%s\" = \"%s\" WHERE \"%s\" == \"%s\";",
                    TABLE_NAME, VALUE_COLUMN_NAME, value, SETTING_COLUMN_NAME, setting
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check existence of settings table in DB,
     * create and filling with default values if not.
     */
    @SneakyThrows
    private void validateDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format(
                    "CREATE TABLE if not exists '%s' " +
                            "('id' INTEGER PRIMARY KEY AUTOINCREMENT, '%s' text, '%s' text);",
                    TABLE_NAME, SETTING_COLUMN_NAME, VALUE_COLUMN_NAME
            ));

            statement.execute(String.format("SELECT * FROM '%s' LIMIT 1", TABLE_NAME));
            if (!statement.getResultSet().next()) {
                // DEFAULT VALUES HERE
                insert(statement, REPOSITORY_IP_SETTING, "http://127.0.0.1:5000");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insert row to settings table.
     *
     * @param statement active connection statement
     * @param setting   setting name
     * @param value     setting init value
     * @throws SQLException ..
     */
    private void insert(Statement statement, String setting, String value) throws SQLException {
        statement.execute(String.format(
                "INSERT INTO '%s' ('%s', '%s') VALUES ('%s', '%s');",
                TABLE_NAME, SETTING_COLUMN_NAME, VALUE_COLUMN_NAME, setting, value
        ));
    }
}
