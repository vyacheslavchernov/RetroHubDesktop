package com.vych.api.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Data entity.
 * Represent information about ROM that was given from repo
 */
@Getter
@Setter
public class RomInfo {
    private String title;
    private String region;
    private boolean official;
    private String path;
}
