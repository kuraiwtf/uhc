package dev.kurai.uhc.team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.nickname.NicknameValue;
import dev.kurai.uhc.nickname.NicknameValueType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeamServiceImpl implements TeamService {

  private final UltraHardcoreAPI ultraHardcore;

  private final Map<Integer, TeamImpl> teams = Maps.newLinkedHashMap();
  private final Map<UUID, Integer> membership = Maps.newHashMap();

  /** Invitee to the (inviter to team id) invitations they hold. */
  private final Map<UUID, Map<UUID, Integer>> invitations = Maps.newHashMap();

  private static final String NICKNAME_VALUE_ID = "team";

  private int nextId = 1;

  public TeamServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  private Optional<TeamModule> module() {
    return this.ultraHardcore.moduleService().getCurrentModule() instanceof final TeamModule module
        ? Optional.of(module)
        : Optional.empty();
  }

  private int teamSize() {
    return this.module().map(TeamModule::teamSize).orElse(1);
  }

  @Override
  public boolean enabled() {
    return this.module().filter(module -> !module.ffa()).isPresent();
  }

  @Override
  public Collection<Team> teams() {
    return List.copyOf(this.teams.values());
  }

  @Override
  public Optional<Team> teamOf(final UUID uniqueId) {
    return Optional.ofNullable(this.membership.get(uniqueId)).map(this.teams::get);
  }

  @Override
  public Optional<Team> createTeam(final UUID leader) {
    if (!this.enabled() || this.membership.containsKey(leader)) {
      return Optional.empty();
    }

    final var team =
        new TeamImpl(
            this.nextId++,
            this.nextStyle(),
            leader,
            () -> this.module().map(TeamModule::teamInventory).orElse(false));
    this.teams.put(team.getId(), team);
    this.membership.put(leader, team.getId());
    this.refreshNicknames(List.of(leader));
    return Optional.of(team);
  }

  private void refreshNicknames(final Collection<UUID> targets) {
    final var nicknameService = this.ultraHardcore.nicknameService();
    for (final var viewer : Bukkit.getOnlinePlayers()) {
      final var holder = nicknameService.holder(viewer.getUniqueId());
      for (final var target : targets) {
        final var nickname = holder.nickname(target);
        nickname.removeValue(NICKNAME_VALUE_ID);

        final var style = this.teamOf(target).map(Team::style).orElse(null);
        if (style == null) {
          holder.removeColor(target);
          continue;
        }

        if (style.symbol() != null) {
          nickname.addValue(
              new NicknameValue(
                  NICKNAME_VALUE_ID,
                  NicknameValueType.PREFIX,
                  Component.text(style.symbol() + " ", style.color().asTextColor())));
        }
        holder.applyColor(target, style.color());
      }
    }
  }

  private TeamStyle nextStyle() {
    final var used = this.teams.values().stream().map(TeamImpl::style).collect(Collectors.toSet());
    return TeamStyle.POOL.stream()
        .filter(style -> !used.contains(style))
        .findFirst()
        .orElseGet(() -> TeamStyle.POOL.get(this.nextId % TeamStyle.POOL.size()));
  }

  @Override
  public boolean invite(final Team team, final UUID invitee) {
    if (!this.enabled()
        || this.membership.containsKey(invitee)
        || team.members().size() >= this.teamSize()) {
      return false;
    }

    this.invitations
        .computeIfAbsent(invitee, key -> Maps.newHashMap())
        .put(team.leader(), team.getId());
    return true;
  }

  @Override
  public Optional<Team> pendingInvite(final UUID invitee, final UUID inviter) {
    return Optional.ofNullable(this.invitations.get(invitee))
        .map(invites -> invites.get(inviter))
        .map(this.teams::get);
  }

  @Override
  public boolean join(final Team team, final UUID uniqueId) {
    if (!(team instanceof final TeamImpl impl)
        || !this.teams.containsKey(impl.getId())
        || this.membership.containsKey(uniqueId)
        || impl.mutableMembers().size() >= this.teamSize()) {
      return false;
    }

    impl.mutableMembers().add(uniqueId);
    this.membership.put(uniqueId, impl.getId());
    this.invitations.remove(uniqueId);
    this.refreshNicknames(List.of(uniqueId));
    return true;
  }

  @Override
  public void leave(final UUID uniqueId) {
    final var id = this.membership.remove(uniqueId);
    if (id == null) {
      return;
    }

    final var team = this.teams.get(id);
    if (team == null) {
      return;
    }

    team.mutableMembers().remove(uniqueId);
    this.refreshNicknames(List.of(uniqueId));
    if (team.mutableMembers().isEmpty()) {
      this.teams.remove(id);
    } else if (team.leader().equals(uniqueId)) {
      team.leader(team.mutableMembers().iterator().next());
    }
  }

  @Override
  public void disband(final Team team) {
    this.teams.remove(team.getId());
    for (final var member : team.members()) {
      this.membership.remove(member);
    }
    this.refreshNicknames(team.members());
  }

  @Override
  public void clear() {
    final var previous = List.copyOf(this.membership.keySet());
    this.teams.clear();
    this.membership.clear();
    this.invitations.clear();
    this.refreshNicknames(previous);
  }

  @Override
  public void assignUnteamed(final Collection<UUID> players) {
    if (!this.enabled()) {
      return;
    }

    final List<UUID> unteamed =
        new ArrayList<>(players.stream().filter(id -> !this.membership.containsKey(id)).toList());
    Collections.shuffle(unteamed);

    for (final var id : unteamed) {
      final var smallest =
          this.teams.values().stream()
              .filter(team -> team.mutableMembers().size() < this.teamSize())
              .min(Comparator.comparingInt(team -> team.mutableMembers().size()));
      if (smallest.isPresent()) {
        this.join(smallest.get(), id);
      } else {
        this.createTeam(id);
      }
    }
  }

  @Override
  public void randomize(final Collection<UUID> players) {
    if (!this.enabled()) {
      return;
    }

    this.clear();

    final List<UUID> shuffled = new ArrayList<>(players);
    Collections.shuffle(shuffled);

    for (final var group : Lists.partition(shuffled, this.teamSize())) {
      final var team = this.createTeam(group.getFirst()).orElseThrow();
      for (final var member : group.subList(1, group.size())) {
        this.join(team, member);
      }
    }
  }
}
