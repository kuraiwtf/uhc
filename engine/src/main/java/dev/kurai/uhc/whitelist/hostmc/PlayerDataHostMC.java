package dev.kurai.uhc.whitelist.hostmc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

record PlayerDataHostMC(
    @JsonProperty("discord") String discordIdentifier,
    @JsonProperty("username") String name,
    @JsonProperty("uuid") String uniqueId,
    @JsonProperty("moderator") boolean moderator,
    @JsonProperty("team") Optional<Integer> teamId) {}
