package dev.kurai.uhc.event.defaults.player;

import dev.kurai.uhc.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public final class PlayerDamageEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile profile;
  private double damage;
  private boolean absorptionBypass;
  private boolean visible;

  @Accessors(fluent = false)
  private boolean cancelled;

  public PlayerDamageEvent(
      final Profile profile,
      final double damage,
      final boolean absorptionBypass,
      final boolean visible) {
    this.profile = profile;
    this.damage = damage;
    this.absorptionBypass = absorptionBypass;
    this.visible = visible;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
