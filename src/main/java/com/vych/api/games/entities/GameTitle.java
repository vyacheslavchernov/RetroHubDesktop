package com.vych.api.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * Represent short information about game that was given from repo
 */
@Getter
@Setter
public class GameTitle {
    private String title;
    private String id;

    @JsonIgnore
    private String cover;
}
