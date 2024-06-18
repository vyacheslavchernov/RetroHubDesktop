package com.vych.api.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data entity.
 * Represent full information about game that was given from repo (including available ROMs)
 */
@Getter
@Setter
public class TitleFullInfo {
    private TitleAbout about;
    private List<RomInfo> roms;
}
