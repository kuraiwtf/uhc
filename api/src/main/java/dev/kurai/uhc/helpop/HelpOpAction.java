package dev.kurai.uhc.helpop;

import java.util.function.IntFunction;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record HelpOpAction(
    String label,
    TextColor color,
    String hoverText,
    boolean suggestOnly,
    IntFunction<String> command) {}
