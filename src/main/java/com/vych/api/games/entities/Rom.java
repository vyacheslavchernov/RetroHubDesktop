package com.vych.api.games.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Information about ROM for game from repository.
 */
@Getter
@Setter
public class Rom {
    private String title;
    private String region;
    private boolean official;
    private String path;
}
