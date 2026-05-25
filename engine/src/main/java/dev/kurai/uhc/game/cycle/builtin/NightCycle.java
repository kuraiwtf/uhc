package dev.kurai.uhc.game.cycle.builtin;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.cycle.AbstractCycle;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class NightCycle extends AbstractCycle {

  public NightCycle(final UltraHardcoreAPI ultraHardcore) {
    super("night", ultraHardcore);
  }

  @Override
  public void onStart() {
    this.ultraHardcore
        .gameService()
        .sendMessage(
            Component.text()
                .append(Component.text(CC.DIAMOND, NamedTextColor.BLUE))
                .appendSpace()
                .append(Component.text("LA NUIT TOMBE", NamedTextColor.BLUE, TextDecoration.BOLD))
                .appendSpace()
                .append(Component.text(CC.DIAMOND, NamedTextColor.BLUE))
                .build());

    this.ultraHardcore.worldService().getWorld().setTime(18_000L);
  }
}
