package dev.kurai.uhc.timer.builtin;

import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import dev.kurai.uhc.world.WorldService;

@Identifier(PvPTimer.IDENTIFIER)
@Name("PvP")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 60 * 60)
public final class PvPTimer extends AbstractTimer {

  static final String IDENTIFIER = "pvp";

  private final WorldService worldService;

  public PvPTimer(final WorldService worldService) {
    this.worldService = worldService;
  }

  @Override
  public void onEnd() {
    this.worldService.getWorld().setPVP(true);
  }
}
