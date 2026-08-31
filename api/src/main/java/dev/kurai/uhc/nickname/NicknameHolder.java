package dev.kurai.uhc.nickname;

import dev.kurai.uhc.util.Color;
import java.util.UUID;

public interface NicknameHolder {

  Nickname nickname(final UUID targetId);

  boolean hasColor(final UUID targetId, final Color color);

  void applyColor(final UUID targetId, final Color color);

  void removeColor(final UUID targetId);

  void updateNickname(final UUID targetId);
}
