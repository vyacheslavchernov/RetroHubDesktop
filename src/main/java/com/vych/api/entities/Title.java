package com.vych.api.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Data entity.
 * Represent short information about game that was given from repo
 */
@Getter
@Setter
public class Title {
    private String title;
    private String path;
}
