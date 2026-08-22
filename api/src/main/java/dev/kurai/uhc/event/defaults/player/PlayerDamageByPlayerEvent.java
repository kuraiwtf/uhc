package dev.kurai.uhc.event.defaults.player;

import dev.kurai.uhc.profile.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Getter
@RequiredArgsConstructor
public final class PlayerDamageByPlayerEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile player;
  private final Profile victim;

  private final EntityDamageByEntityEvent bukkitEvent;

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
