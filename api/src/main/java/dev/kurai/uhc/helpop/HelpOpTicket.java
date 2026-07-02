package dev.kurai.uhc.helpop;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record HelpOpTicket(
    int id, UUID askerId, String askerName, String question, Instant createdAt) {}
