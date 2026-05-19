package dev.kurai.uhc.game.cycle;

import dev.kurai.uhc.util.api.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface Cycle extends Identifiable<@NotNull String> {

  @Override
  @NotNull
  String getId();

  default void onStart() {}

  default void onSkip() {}

  default void onStop() {}
}
