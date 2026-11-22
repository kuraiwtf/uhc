package dev.kurai.uhc.extension.minigame.duel;

import dev.kurai.uhc.extension.minigame.duel.type.DuelType;

import java.time.Instant;
import java.util.UUID;

public record DuelRequest(UUID sender, UUID receiver, DuelType duelType, Instant sentAt) {

    public boolean expired() {
        return Instant.now().isAfter(this.sentAt.plusSeconds(30));
    }
}
