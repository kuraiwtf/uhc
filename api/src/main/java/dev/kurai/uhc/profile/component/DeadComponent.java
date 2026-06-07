package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public final class DeadComponent implements Component {

  private UUID killer;
  private long deathTime;

  private ItemStack[] inventory;
  private ItemStack[] armor;
}
