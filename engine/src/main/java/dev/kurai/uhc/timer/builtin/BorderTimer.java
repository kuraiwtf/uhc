package dev.kurai.uhc.timer.builtin;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.Tag.inserting;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.*;

import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import dev.kurai.uhc.world.WorldService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

@Identifier(BorderTimer.IDENTIFIER)
@Name("Bordure")
@Duration(min = 60 * 60, defaultValue = 90 * 60, max = 2 * 60 * 60)
public final class BorderTimer extends AbstractTimer {

  static final String IDENTIFIER = "border";

  private final WorldService worldService;
  private final BukkitAudiences bukkitAudiences;

  public BorderTimer(final WorldService worldService, final BukkitAudiences bukkitAudiences) {
    this.worldService = worldService;
    this.bukkitAudiences = bukkitAudiences;
  }

  @Override
  public void onEnd() {
    final var world = this.worldService.getWorld();
    final var time =
        (INITIAL_SIZE_OPTION.getValue() - FINAL_SIZE_OPTION.getValue())
            / SHRINK_SPEED_OPTION.getValue();
    world.getWorldBorder().setSize(FINAL_SIZE_OPTION.getValue() * 2, (long) time);

    this.bukkitAudiences
        .all()
        .sendMessage(
            MiniMessage.miniMessage()
                .deserialize(
                    "<st><dark_red>+------</dark_red><red>-------</red>-------<red>-------</red><dark_red>------+</dark_red></st><newline><newline> <red><b>»</b> Bordure</red><newline><newline>  Réduction de la bordure en cours.<newline>  Taille finale: <red>±<final_size></red><newline><newline><st><dark_red>+------</dark_red><red>-------</red>-------<red>-------</red><dark_red>------+</dark_red></st>",
                    resolver("final_size", inserting(text(FINAL_SIZE_OPTION.getValue())))));

    Bukkit.getOnlinePlayers()
        .forEach(
            player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f));
  }
}
