package dev.kurai.uhc.tablist.part;

import static dev.kurai.uhc.effect.EffectType.RESISTANCE;
import static dev.kurai.uhc.effect.EffectType.SPEED;
import static dev.kurai.uhc.effect.EffectType.STRENGTH;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;

import dev.kurai.uhc.effect.Effect;
import dev.kurai.uhc.effect.EffectHolder;
import dev.kurai.uhc.effect.EffectType;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.tablist.TabListPart;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

public final class EffectTabListPart implements TabListPart {

  private static final Component SEPARATOR =
      text().appendSpace().append(text(CC.SQUARE, DARK_GRAY)).appendSpace().build();

  private final ProfileService participantService;

  @Contract(pure = true)
  public EffectTabListPart(final ProfileService participantService) {
    this.participantService = participantService;
  }

  @Contract(pure = true)
  @Override
  public String key() {
    return "effect";
  }

  @Override
  public int priority() {
    return -50;
  }

  @Contract("_ -> new")
  @Override
  public @Unmodifiable Component render(final Player player) {
    final Profile profile = this.participantService.getOrCreateProfile(player.getUniqueId());
    final EffectHoldingComponent effectComponent =
        profile.getComponent(EffectHoldingComponent.class);
    assert effectComponent != null;
    final EffectHolder effectHolder = effectComponent.holder();
    return text()
        .append(
            this.provideEffectComponent(
                this.provideEffectPercentage(effectHolder, STRENGTH), RED, '⚔'))
        .append(SEPARATOR)
        .append(
            this.provideEffectComponent(
                this.provideEffectPercentage(effectHolder, RESISTANCE), GREEN, '☘'))
        .append(SEPARATOR)
        .append(
            this.provideEffectComponent(
                this.provideEffectPercentage(effectHolder, SPEED), AQUA, '✪'))
        .appendNewline()
        .build();
  }

  @Contract(pure = true)
  @Override
  public Position position() {
    return Position.BOTTOM;
  }

  private double provideEffectPercentage(
      final EffectHolder effectHolder, final EffectType effectType) {
    return effectHolder.effects().stream()
        .filter(effect -> effect.type() == effectType)
        .mapToDouble(Effect::value)
        .sum();
  }

  @Contract("_, _, _ -> new")
  private Component provideEffectComponent(
      final double value, final TextColor color, final char symbol) {
    return text()
        .append(text("%.1f".formatted(value * 100), color))
        .append(text('%', color))
        .appendSpace()
        .append(text(symbol, color))
        .build();
  }
}
