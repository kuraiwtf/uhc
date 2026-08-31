package dev.kurai.uhc.nickname;

import dev.kurai.uhc.util.Color;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface Nickname {

  Color getColor();

  void setColor(final Color color);

  Set<NicknameStyle> getDecorations();

  void addStyle(final NicknameStyle style);

  void removeStyle(final NicknameStyle style);

  @Nullable
  String getGroup();

  void setGroup(@Nullable final String group);

  List<NicknameValue> getValues();

  void addValue(final NicknameValue value);

  void removeValue(final String id);

  void clearValues(final NicknameValueType type);

  Component getPrefix();

  Component getSuffix();

  Component render(final String baseName);
}
