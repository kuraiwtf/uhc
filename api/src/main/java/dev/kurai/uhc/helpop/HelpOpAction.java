package dev.kurai.uhc.helpop;

import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;

import java.util.function.IntFunction;

@NullMarked
public record HelpOpAction(
        String label, TextColor color, String hoverText, boolean suggestOnly, IntFunction<String> command) {}
