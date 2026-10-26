package dev.kurai.uhc.game.group;

import com.google.common.collect.Sets;
import java.util.Set;

public final class GroupVariableTask implements Runnable {

  private final GroupService groupService;
  private final Set<GroupVariable> appliedVariables;

  public GroupVariableTask(final GroupService groupService) {
    this.groupService = groupService;
    this.appliedVariables = Sets.newHashSet();
  }

  @Override
  public void run() {
    for (final GroupVariable variable : this.groupService.variables()) {
      if (!this.appliedVariables.contains(variable) && variable.shouldApply()) {
        this.appliedVariables.add(variable);
        this.groupService.provider().groups(variable.groupSize());
      }
    }
  }
}
