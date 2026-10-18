package dev.kurai.uhc.nickname;

import static net.kyori.adventure.text.Component.text;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.kurai.uhc.util.Color;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

final class NicknameHolderImpl implements NicknameHolder {

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final UUID ownerId;
  private final NicknameService nicknameService;
  private final Map<UUID, Color> appliedColors;
  private final Set<String> createdTeams;
  private final Map<UUID, Nickname> overlays;

  NicknameHolderImpl(final UUID ownerId, final NicknameService nicknameService) {
    this.ownerId = ownerId;
    this.nicknameService = nicknameService;
    this.appliedColors = Maps.newHashMap();
    this.createdTeams = Sets.newHashSet();
    this.overlays = Maps.newHashMap();
  }

  void resetKnownTeams() {
    this.createdTeams.clear();
  }

  @Override
  public Nickname nickname(final UUID targetId) {
    final Nickname base = this.nicknameService.nickname(targetId);
    return new ViewerNickname(targetId, base);
  }

  @Override
  public boolean hasColor(final UUID targetId, final Color color) {
    return this.appliedColors.getOrDefault(
            targetId, this.nicknameService.nickname(targetId).getColor())
        == color;
  }

  @Override
  public void applyColor(final UUID targetId, final Color color) {
    this.appliedColors.put(targetId, color);
    this.updateNickname(targetId);
  }

  @Override
  public void removeColor(final UUID targetId) {
    this.appliedColors.remove(targetId);
    this.updateNickname(targetId);
  }

  @Override
  public void updateNickname(final UUID targetId) {
    final Player player = Bukkit.getPlayer(this.ownerId);
    if (player == null) {
      return;
    }

    final User user = PLAYER_MANAGER.getUser(player);
    if (user == null) {
      return;
    }

    final Player target = Bukkit.getPlayer(targetId);
    if (target == null) {
      return;
    }

    final Nickname nickname = this.nickname(targetId);
    final String teamName =
        nickname.getGroup() == null ? target.getName() : nickname.getGroup() + target.getName();

    if (!this.createdTeams.add(teamName)) {
      final WrapperPlayServerTeams removePacket =
          new WrapperPlayServerTeams(
              teamName, WrapperPlayServerTeams.TeamMode.REMOVE, Optional.empty());
      user.writePacket(removePacket);
    }

    final Component prefixWithNameColor =
        nickname
            .getPrefix()
            .append(text(nickname.getColor().asBukkitColor().toString()))
            .append(
                text(
                    nickname.getDecorations().stream()
                        .map(NicknameStyle::character)
                        .collect(Collectors.joining())));

    final WrapperPlayServerTeams infoPacket =
        new WrapperPlayServerTeams(
            teamName,
            WrapperPlayServerTeams.TeamMode.CREATE,
            new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                text(target.getName()),
                prefixWithNameColor,
                nickname.getSuffix(),
                ((player.canSee(target) && !target.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    ? WrapperPlayServerTeams.NameTagVisibility.ALWAYS
                    : WrapperPlayServerTeams.NameTagVisibility.NEVER),
                WrapperPlayServerTeams.CollisionRule.PUSH_OTHER_TEAMS,
                nickname.getColor().asNamedColor(),
                WrapperPlayServerTeams.OptionData.ALL),
            target.getName());
    user.writePacket(infoPacket);

    user.flushPackets();
  }

  private final class ViewerNickname implements Nickname {

    private final UUID targetId;
    private final Nickname base;

    private ViewerNickname(final UUID targetId, final Nickname base) {
      this.targetId = targetId;
      this.base = base;
    }

    @Override
    public Color getColor() {
      return NicknameHolderImpl.this.appliedColors.getOrDefault(
          this.targetId, this.base.getColor());
    }

    @Override
    public void setColor(final Color color) {
      NicknameHolderImpl.this.appliedColors.put(this.targetId, color);
    }

    private Nickname overlay() {
      return NicknameHolderImpl.this.overlays.computeIfAbsent(
          this.targetId, id -> new NicknameImpl(this.base.getColor()));
    }

    @Override
    public Set<NicknameStyle> getDecorations() {
      final Set<NicknameStyle> merged = Sets.newHashSet(this.base.getDecorations());
      merged.addAll(this.overlay().getDecorations());
      return merged;
    }

    @Override
    public void addStyle(final NicknameStyle style) {
      this.overlay().addStyle(style);
    }

    @Override
    public void removeStyle(final NicknameStyle style) {
      this.overlay().removeStyle(style);
    }

    @Override
    public String getGroup() {
      final String own = this.overlay().getGroup();
      return own != null ? own : this.base.getGroup();
    }

    @Override
    public void setGroup(final String group) {
      this.overlay().setGroup(group);
    }

    @Override
    public List<NicknameValue> getValues() {
      final List<NicknameValue> merged = Lists.newArrayList(this.base.getValues());
      merged.addAll(this.overlay().getValues());
      return merged;
    }

    @Override
    public void addValue(final NicknameValue value) {
      this.overlay().addValue(value);
    }

    @Override
    public void removeValue(final String id) {
      this.overlay().removeValue(id);
    }

    @Override
    public void clearValues(final NicknameValueType type) {
      this.overlay().clearValues(type);
    }

    @Override
    public Component getPrefix() {
      return this.join(NicknameValueType.PREFIX);
    }

    @Override
    public Component getSuffix() {
      return this.join(NicknameValueType.SUFFIX);
    }

    private Component join(final NicknameValueType type) {
      Component component = Component.empty();
      for (final NicknameValue value : this.getValues()) {
        if (value.type() == type) {
          component = component.append(value.value());
        }
      }
      return component;
    }

    @Override
    public Component render(final String baseName) {
      return this.getPrefix()
          .append(
              Component.text(
                  baseName,
                  this.getColor().asTextColor(),
                  this.getDecorations().stream()
                      .map(NicknameStyle::decoration)
                      .collect(Collectors.toSet())))
          .append(this.getSuffix());
    }
  }
}
