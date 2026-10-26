package dev.kurai.uhc.extension.mumble;

import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.HoverEvent.showText;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.extension.mumble.command.MumbleCommand;
import dev.kurai.uhc.extension.mumble.item.MumbleItem;
import dev.kurai.uhc.extension.mumble.listener.GameListener;
import dev.kurai.uhc.extension.mumble.listener.PlayerJoinListener;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class MumbleExtension {

  private final UltraHardcoreAPI ultraHardcore;

  private boolean initialized;
  private BukkitTask advertiseTask;

  public MumbleExtension(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  public void init() {
    if (!this.initialized) {
      this.ultraHardcore.commandRegistrar().registerCommand(new MumbleCommand(this));

      final EventService eventService = this.ultraHardcore.eventService();
      eventService.registerListeners(new PlayerJoinListener(this), new GameListener(this));

      this.ultraHardcore.itemService().registerItem(new MumbleItem(this));
      this.initialized = true;
    }
  }

  public void advertise(final Player player) {
    final String url = this.ultraHardcore.plugin().getConfig().getString("mumble.url", "");
    if (url.isBlank()) {
      return;
    }

    player.sendMessage("");
    player.sendMessage(center("&b&lMUMBLE LINK"));
    player.sendMessage("");
    player.sendMessage(center("Cette partie dispose de&b Mumble Link&r."));
    player.sendMessage("");
    player.sendMessage(center("Utilisez le lien ci-dessous pour vous"));
    player.sendMessage(center("&aconnecter&r automatiquement au&b Mumble&r."));
    player.sendMessage("");
    final Audience audience = this.ultraHardcore.bukkitAudiences().player(player);
    audience.sendMessage(
        center(
            text("Cliquez-ici")
                .decorate(TextDecoration.UNDERLINED)
                .hoverEvent(showText(text("Cliquez pour ouvrir le lien.")))
                .clickEvent(openUrl(url))));
    player.sendMessage("");
  }

  public void start() {
    if (this.advertiseTask != null) {
      return;
    }

    this.advertiseTask =
        Bukkit.getScheduler()
            .runTaskTimer(
            this.ultraHardcore.plugin(),
            () -> Bukkit.getOnlinePlayers().forEach(this::advertise),
            0,
            20 * (60 * 5));
  }
}
