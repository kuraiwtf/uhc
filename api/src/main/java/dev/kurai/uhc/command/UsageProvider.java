package dev.kurai.uhc.command;

import dev.kurai.uhc.command.argument.data.ArgumentData;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface UsageProvider {

    Component getUsage(final String command, final List<ArgumentData> arguments);
}
