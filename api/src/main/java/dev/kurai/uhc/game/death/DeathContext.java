package dev.kurai.uhc.game.death;

import dev.kurai.uhc.profile.Profile;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jspecify.annotations.Nullable;

public record DeathContext(
    Profile profile, @Nullable Profile killer, PlayerDeathEvent event, boolean offline) {}
