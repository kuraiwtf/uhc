package dev.kurai.uhc.game.group;

public interface GroupService {

  GroupProvider provider();

  boolean enabled();

  void enabled(final boolean enabled);
}
