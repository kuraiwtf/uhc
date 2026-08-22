package dev.kurai.uhc.whitelist.hostmc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;

record GameDataHostMC(
    @JsonProperty("game_id") String code,
    @JsonProperty("players") Collection<PlayerDataHostMC> players) {}
