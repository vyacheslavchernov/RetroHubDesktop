package com.vych.database;

import lombok.Getter;

/**
 * Enum of app settings and default values
 */
@Getter
public enum SettingsDefaults {
    REPOSITORY_IP("repository_ip", "http://127.0.0.1:5000"),
    DEFAULT_THEME("default_theme", "Dracula");

    private final String name;
    private final String defaultValue;

    SettingsDefaults(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }
}
