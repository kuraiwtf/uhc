package dev.kurai.uhc.event.defaults.player;

import dev.kurai.uhc.profile.Profile;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public final class PlayerExplosionEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile source;
  private final Explosion explosion;
  private final Location location;
  private final double radius;

  @Setter
  @Accessors(fluent = false)
  private boolean cancelled;

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public record Explosion(Collection<BlockData> blocks) {}

  public record BlockData(Location location, Material material, byte data) {}
}
