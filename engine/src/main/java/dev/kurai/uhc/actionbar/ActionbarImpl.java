package dev.kurai.uhc.actionbar;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcorePlugin;
import dev.kurai.uhc.actionbar.entry.ActionbarEntry;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class ActionbarImpl implements Actionbar {

  private final Map<@NotNull String, @NotNull ActionbarEntry> entries;
  private final UUID id;

  public ActionbarImpl(final @NotNull UUID uniqueId) {
    this.entries = Maps.newHashMap();
    this.id = uniqueId;
  }

  @Override
  public Map<String, ActionbarEntry> getEntries() {
    return this.entries;
  }

  @Override
  public void registerEntry(final @NotNull String key, final @NotNull Component component) {
    this.registerEntry(new ActionbarEntry(key, component));
  }

  @Override
  public void registerEntry(
      final @NotNull String key, final @NotNull Component component, final int ticks) {
    this.registerEntry(new ActionbarEntry(key, component), ticks);
  }

  @Override
  public void registerEntry(final @NotNull ActionbarEntry entry, final int ticks) {
    Bukkit.getScheduler()
        .runTaskLater(
            UltraHardcorePlugin.getPlugin(UltraHardcorePlugin.class),
            () -> this.removeEntry(entry.getId()),
            ticks);
  }

  @Override
  public void registerEntry(final @NotNull ActionbarEntry entry) {
    this.entries.put(entry.getId(), entry);
  }

  @Override
  public void removeEntry(final @NotNull String key) {
    this.entries.remove(key);
  }

  @Override
  public void removeEntry(final @NotNull ActionbarEntry entry) {
    this.entries.remove(entry.getId());
  }

  @Override
  public @NotNull ActionbarEntry getEntry(final @NotNull String key) {
    return this.entries.get(key);
  }

  @Override
  public @NotNull UUID getId() {
    return this.id;
  }
}
