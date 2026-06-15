package dev.kurai.uhc.util.api.state;

import dev.kurai.uhc.util.api.Identifiable;

public interface State extends Identifiable<String> {

  @Override
  String getId();
}
