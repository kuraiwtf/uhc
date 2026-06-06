package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
public final class DisconnectComponent implements Component {

  private long timeLeft;
  private Instant lastLogin;

  private @Nullable Location lastLocation;
  private ItemStack[] inventory = new ItemStack[36];
  private ItemStack[] armor = new ItemStack[4];

  public DisconnectComponent(final long timeLeft, final Instant lastLogin) {
    this.timeLeft = timeLeft;
    this.lastLogin = lastLogin;
  }
}
