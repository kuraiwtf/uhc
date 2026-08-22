package dev.kurai.uhc.game.group;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class GroupServiceImpl implements GroupService {

  private final GroupProvider provider;
  private boolean enabled;

  public GroupServiceImpl() {
    this.provider = new BuiltinGroupProvider();
  }
}
