package dev.kurai.uhc.game.group;

import lombok.Getter;
import lombok.Setter;

final class BuiltinGroupProvider implements GroupProvider {

  @Getter @Setter private int groups = 5;
}
