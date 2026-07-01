package dev.kurai.uhc.scoreboard.sidebar;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class SidebarImpl implements Sidebar {

  private static final String OBJECTIVE_NAME = "arena";

  private final UUID uniqueId;
  private final Map<Integer, Component> entries;

  private Component previousTitle;

  public SidebarImpl(final UUID uniqueId) {
    this.uniqueId = uniqueId;
    this.entries = Maps.newHashMap();

    this.writePacket(
        new WrapperPlayServerScoreboardObjective(
            OBJECTIVE_NAME,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE,
            Component.text("Scoreboard"),
            null));

    this.writePacket(new WrapperPlayServerDisplayScoreboard(1, OBJECTIVE_NAME));
  }

  @Override
  public void editTitle(final Component title) {
    Preconditions.checkNotNull(title, "title cannot be null");
    if (this.previousTitle != null && this.previousTitle.equals(title)) {
      return;
    }

    this.previousTitle = title;
    this.writePacket(
        new WrapperPlayServerScoreboardObjective(
            OBJECTIVE_NAME,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE,
            title,
            null));
  }

  @Override
  public void overrideLine(final int score, final Component content) {
    Preconditions.checkNotNull(content, "content cannot be null");

    if (this.hasEntry(score)) {
      if (this.entries.get(score).equals(content)) {
        return;
      }

      this.removeLine(score);
    }

    this.writePacket(
        new WrapperPlayServerUpdateScore(
            this.provideColor(score)
                + ""
                + ChatColor.WHITE
                + LegacyComponentSerializer.legacySection().serialize(content),
            WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
            OBJECTIVE_NAME,
            score,
            Component.empty(),
            ScoreFormat.blankScore()));
    this.entries.put(score, content);
  }

  @Override
  public void removeLine(final int score) {
    if (!this.hasEntry(score)) {
      return;
    }

    final var component = this.entries.get(score);
    if (component == null) {
      return;
    }

    this.writePacket(
        new WrapperPlayServerUpdateScore(
            this.provideColor(score)
                + ""
                + ChatColor.WHITE
                + LegacyComponentSerializer.legacySection().serialize(component),
            WrapperPlayServerUpdateScore.Action.REMOVE_ITEM,
            OBJECTIVE_NAME,
            score,
            component,
            ScoreFormat.blankScore()));
    this.entries.remove(score);
  }

  @Override
  public void trimLines(final Set<Integer> activeScores) {
    final var staleScores = Maps.newHashMap(this.entries).keySet();
    staleScores.removeAll(activeScores);
    staleScores.forEach(this::removeLine);
  }

  @Override
  public void destroy() {
    for (final var entry : this.entries.entrySet()) {
      this.removeLine(entry.getKey());
      this.entries.remove(entry.getKey());
    }

    this.writePacket(
        new WrapperPlayServerScoreboardObjective(
            OBJECTIVE_NAME,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE,
            Component.empty(),
            WrapperPlayServerScoreboardObjective.RenderType.INTEGER,
            ScoreFormat.blankScore()));
  }

  @Override
  public void send() {
    final var player = Bukkit.getPlayer(this.uniqueId);
    if (player == null) {
      return;
    }

    final var user = PacketEvents.getAPI().getPlayerManager().getUser(player);
    if (user == null) {
      return;
    }

    user.flushPackets();
  }

  private ChatColor provideColor(final int score) {
    return ChatColor.values()[score % ChatColor.values().length];
  }

  private boolean hasEntry(final int line) {
    return this.entries.containsKey(line);
  }

  private void writePacket(final PacketWrapper<?> removePacket) {
    final var player = Bukkit.getPlayer(this.uniqueId);
    if (player == null) {
      return;
    }

    final var user = PacketEvents.getAPI().getPlayerManager().getUser(player);
    if (user == null) {
      return;
    }

    user.writePacket(removePacket);
  }
}
