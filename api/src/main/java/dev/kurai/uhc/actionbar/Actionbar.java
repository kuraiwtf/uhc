package dev.kurai.uhc.actionbar;

import dev.kurai.uhc.actionbar.entry.ActionbarEntry;
import dev.kurai.uhc.util.api.Identifiable;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;

public interface Actionbar extends Identifiable<UUID> {

  @UnmodifiableView
  Map<@NotNull String, @NotNull ActionbarEntry> getEntries();

  void registerEntry(final @NotNull String key, final @NotNull Component component);

  void registerEntry(
      final @NotNull String key,
      final @NotNull Component component,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ticks);

  void registerEntry(final @NotNull ActionbarEntry entry);

  void registerEntry(
      final @NotNull ActionbarEntry entry,
      final @Range(from = 0, to = Integer.MAX_VALUE) int ticks);

  void removeEntry(final @NotNull String key);

  void removeEntry(final @NotNull ActionbarEntry entry);

  @NotNull
  ActionbarEntry getEntry(final @NotNull String key);
}
