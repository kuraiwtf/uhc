package dev.kurai.uhc.helpop;

import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.UUID;

@NullMarked
public record HelpOpTicket(int id, UUID askerId, String askerName, String question, Instant createdAt) {}
