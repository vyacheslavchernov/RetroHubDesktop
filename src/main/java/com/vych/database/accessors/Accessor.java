package com.vych.database.accessors;

/**
 * Local database accessor interface
 */
public interface Accessor {
    /**
     * Check existence of settings table in DB,
     * create and filling with default values if not.
     */
    void validateDatabase();
}
