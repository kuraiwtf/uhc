package dev.kurai.uhc.actionbar;

import com.google.common.collect.Lists;
import dev.kurai.uhc.util.CC;
import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class ActionbarUpdaterTask implements Runnable {

  private final BukkitAudiences bukkitAudiences;
  private final ActionbarService actionbarService;

  public ActionbarUpdaterTask(
      final @NotNull BukkitAudiences bukkitAudiences,
      final @NotNull ActionbarService actionbarService) {
    this.bukkitAudiences = bukkitAudiences;
    this.actionbarService = actionbarService;
  }

  @Override
  public void run() {
    for (final var player : Bukkit.getOnlinePlayers()) {
      final var actionbar = this.actionbarService.getActionbar(player);
      if (actionbar.getEntries().isEmpty()) {
        continue;
      }

      final var entries = Lists.newArrayList(actionbar.getEntries().values());
      this.bukkitAudiences.player(player).sendActionBar(this.asActionbar(entries));
    }
  }

  private @NotNull Component asActionbar(final @NotNull List<ActionbarEntry> entries) {
    final var all = entries.stream().sorted(Comparator.comparing(ActionbarEntry::getId)).toList();
    Component result = Component.empty();
    result = result.append(Component.text("» ", NamedTextColor.DARK_GRAY));

    for (final ActionbarEntry entry : all) {
      result = result.append(entry.content());

      if (!entry.getId().equals(all.getLast().getId())) {
        result = result.append(Component.text(" §8" + CC.BAR + " §r"));
      }
    }

    result = result.append(Component.text(" «", NamedTextColor.DARK_GRAY));
    return result;
  }
}
