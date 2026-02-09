package dev.kurai.uhc.util.api.state;

import dev.kurai.uhc.util.api.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface State extends Identifiable<@NotNull String> {

  @Override
  @NotNull
  String getId();
}
