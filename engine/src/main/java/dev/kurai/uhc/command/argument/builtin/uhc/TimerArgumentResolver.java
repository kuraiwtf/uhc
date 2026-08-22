package dev.kurai.uhc.command.argument.builtin.uhc;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.TimerService;
import java.util.Collection;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.Nullable;

public final class TimerArgumentResolver implements ArgumentResolver<@Nullable AbstractTimer> {

  private final BukkitAudiences bukkitAudiences;
  private final TimerService timerService;

  public TimerArgumentResolver(
      final BukkitAudiences bukkitAudiences, final TimerService timerService) {
    this.bukkitAudiences = bukkitAudiences;
    this.timerService = timerService;
  }

  @Override
  public @Nullable AbstractTimer resolve(final CommandSender sender, final String argument) {
    final var found = this.timerService.getTimer(argument).orElse(null);

    if (found == null) {
      this.bukkitAudiences
          .sender(sender)
          .sendMessage(
              prefix()
                  .append(text("Le timer ", RED))
                  .append(text(argument, DARK_RED))
                  .append(text(" n'existe pas.", RED))
                  .build());
      return null;
    }

    return found;
  }

  @Override
  public Collection<String> complete(final CommandSender sender, final String argument) {
    return this.timerService.getTimers().stream()
        .map(AbstractTimer::getIdentifier)
        .filter(s -> s.startsWith(argument.toLowerCase()))
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
  }
}
