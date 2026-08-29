package dev.kurai.uhc.tablist.part;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.tablist.TabListPart;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class GlobalInformationTablistPart implements TabListPart {

  private final UltraHardcoreAPI ultraHardcore;

  public GlobalInformationTablistPart(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public String key() {
    return "global_information";
  }

  @Override
  public Component render(final Player player) {
    final int ping = ((CraftPlayer) player).getHandle().ping;
    final var pingColor = this.providePingColor(ping);
    final var tps = MinecraftServer.getServer().recentTps[0];

    final AbstractModule module = this.ultraHardcore.moduleService().getCurrentModule();
    return text()
        .append(text('-', DARK_GRAY))
        .appendSpace()
        .append(text(module.getName(), GOLD, TextDecoration.BOLD))
        .appendSpace()
        .append(text('-', DARK_GRAY))
        .appendNewline()
        .appendNewline()
        .append(text("Ping: "))
        .append(text(ping, pingColor))
        .append(text("ms", pingColor))
        .appendSpace()
        .append(text(CC.BAR, DARK_GRAY))
        .appendSpace()
        .append(text("Joueurs: "))
        .append(text(Bukkit.getOnlinePlayers().size(), GREEN))
        .appendSpace()
        .append(text(CC.BAR, DARK_GRAY))
        .appendSpace()
        .append(text("TPS: "))
        .append(text(tps > 20 ? "*20" : "%.1f".formatted(tps), this.provideTpsColor(tps)))
        .build();
  }

  @Override
  public Position position() {
    return Position.TOP;
  }

  private TextColor providePingColor(final int ping) {
    return ping < 25 ? AQUA : ping < 50 ? GREEN : ping < 100 ? YELLOW : ping < 150 ? GOLD : RED;
  }

  private TextColor provideTpsColor(final double tps) {
    return tps > 20 ? AQUA : tps > 18 ? GREEN : tps > 15 ? YELLOW : tps > 10 ? GOLD : RED;
  }
}
