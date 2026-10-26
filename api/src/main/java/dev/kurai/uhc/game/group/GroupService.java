package dev.kurai.uhc.game.group;

import java.util.Collection;

public interface GroupService {

  GroupProvider provider();

  Collection<GroupVariable> variables();

  void addGroupVariable(final GroupVariable variable);

  void removeGroupVariable(final GroupVariable variable);

  void start();

  void stop();

  boolean enabled();

  void enabled(final boolean enabled);

  void alertGroups();
}
