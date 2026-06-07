package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

@RequiredArgsConstructor
@Getter
public final class PlayerInformationComponent implements Component {

  private final Location lastLocation;

  private final ItemStack[] inventory;
  private final ItemStack[] armor;

  private final Collection<PotionEffect> effects;

  private final int fireTicks;

  private final float fallDistance;
}
