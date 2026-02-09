package dev.kurai.uhc.command.impl;

import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.data.ArgumentData;
import java.lang.reflect.Method;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record SubCommandData(
    CommandMeta commandMeta, Object object, Method method, List<@NotNull ArgumentData> arguments) {}
