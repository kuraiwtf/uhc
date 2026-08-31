package dev.kurai.uhc.nickname;

import com.google.common.collect.Lists;
import dev.kurai.uhc.util.Color;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

final class NicknameImpl implements Nickname {

  private final List<NicknameValue> values;
  private Color color;
  private final Set<NicknameStyle> decorations;
  private @Nullable String group;

  NicknameImpl(final Color color) {
    this.color = color;
    this.decorations = new CopyOnWriteArraySet<>();
    this.values = Lists.newArrayList();
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public void setColor(final Color color) {
    this.color = color;
  }

  @Override
  public Set<NicknameStyle> getDecorations() {
    return this.decorations;
  }

  @Override
  public void addStyle(final NicknameStyle style) {
    this.decorations.add(style);
  }

  @Override
  public void removeStyle(final NicknameStyle style) {
    this.decorations.remove(style);
  }

  @Override
  public @Nullable String getGroup() {
    return this.group;
  }

  @Override
  public void setGroup(@Nullable final String group) {
    this.group = group;
  }

  @Override
  public List<NicknameValue> getValues() {
    return this.values;
  }

  @Override
  public void addValue(final NicknameValue value) {
    this.values.add(value);
  }

  @Override
  public void removeValue(final String id) {
    this.values.removeIf(value -> value.id().equals(id));
  }

  @Override
  public void clearValues(final NicknameValueType type) {
    this.values.removeIf(value -> value.type() == type);
  }

  @Override
  public Component getPrefix() {
    return this.join(NicknameValueType.PREFIX);
  }

  @Override
  public Component getSuffix() {
    return this.join(NicknameValueType.SUFFIX);
  }

  private Component join(final NicknameValueType type) {
    var component = Component.empty();
    for (final var value : this.values) {
      if (value.type() != type) {
        continue;
      }

      component = component.append(value.value());
    }

    return component;
  }

  @Override
  public Component render(final String baseName) {
    return this.getPrefix()
        .append(
            Component.text(
                baseName,
                this.color.asTextColor(),
                this.decorations.stream()
                    .map(NicknameStyle::decoration)
                    .collect(Collectors.toSet())))
        .append(this.getSuffix());
  }
}
