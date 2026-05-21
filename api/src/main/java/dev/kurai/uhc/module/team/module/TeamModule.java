package dev.kurai.uhc.module.team.module;

public interface TeamModule {

  boolean teamView();

  void teamView(final boolean teamView);

  int minTeamSize();

  void minTeamSize(final int minTeamSize);

  int teamSize();

  void teamSize(final int teamSize);

  int maxTeamSize();

  void maxTeamSize(final int maxTeamSize);

  default boolean ffa() {
    return this.teamSize() == 1;
  }

  boolean friendlyFire();

  void friendlyFire(final boolean friendlyFire);

  boolean randomTeam();

  void randomTeam(final boolean randomTeam);
}
