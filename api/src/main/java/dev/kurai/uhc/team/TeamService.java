package dev.kurai.uhc.team;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TeamService {

  boolean enabled();

  Collection<Team> teams();

  Optional<Team> teamOf(final UUID uniqueId);

  default boolean areTeammates(final UUID first, final UUID second) {
    return !first.equals(second) && this.teamOf(first).filter(team -> team.contains(second)).isPresent();
  }

  Optional<Team> createTeam(final UUID leader);

  boolean invite(final Team team, final UUID invitee);

  Optional<Team> pendingInvite(final UUID invitee, final UUID inviter);

  boolean join(final Team team, final UUID uniqueId);

  void leave(final UUID uniqueId);

  void disband(final Team team);

  void clear();

  void assignUnteamed(final Collection<UUID> players);

  void randomize(final Collection<UUID> players);
}
