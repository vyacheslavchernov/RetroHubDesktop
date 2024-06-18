package com.vych.api.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Data entity.
 * Represent full information about game that was given from repo
 */
@Getter
@Setter
public class TitleAbout {
    private String title;
    private int released;
    private String cover;
    private String platform;
    private String rating;
    private String developer;
    private String players;
    private String description;
}
