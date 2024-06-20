package com.vych.database.accessors;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Basic accessor to local database.
 * Extend all other accessor from this one.
 */
public abstract class BasicAccessor implements Accessor {
    protected final Connection connection;

    public BasicAccessor(Connection connection) {
        this.connection = connection;
        validateDatabase();
    }

    /**
     * Perform an query to local DB
     *
     * @param sqlTemplate setting name
     * @param args        setting init value
     */
    protected List<HashMap<String, Object>> performQuery(String sqlTemplate, String... args) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format(sqlTemplate, (Object[]) args));

            ResultSet resultSet = statement.getResultSet();
            if (resultSet == null) {
                return new ArrayList<>();
            }
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            List<HashMap<String, Object>> extractedRows = new ArrayList<>();
            if (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<>();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    switch (resultSetMetaData.getColumnType(i)) {
                        case Types.INTEGER:
                            row.put(resultSetMetaData.getColumnLabel(i), resultSet.getInt(i));
                            break;
                        case Types.VARCHAR:
                            row.put(resultSetMetaData.getColumnLabel(i), resultSet.getString(i));
                    }
                }
                extractedRows.add(row);
            }
            return extractedRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
