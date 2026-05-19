package dev.kurai.uhc.actionbar;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ActionbarService {

  default @NotNull Actionbar getActionbar(final @NotNull Player player) {
    return this.getActionbar(player.getUniqueId());
  }

  @NotNull
  Actionbar getActionbar(final @NotNull UUID uniqueId);
}
