package dev.kurai.uhc.nickname;

import net.kyori.adventure.text.Component;

public record NicknameValue(String id, NicknameValueType type, Component value) {}
