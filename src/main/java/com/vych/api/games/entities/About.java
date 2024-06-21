package com.vych.api.games.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Information about game from repository.
 */
@Getter
@Setter
public class About {
    private String title;
    private int released;
    private String cover;
    private String platform;
    private String rating;
    private String developer;
    private String players;
    private String description;
}
