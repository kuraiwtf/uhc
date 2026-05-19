package dev.kurai.uhc.actionbar;

import dev.kurai.uhc.util.api.Identifiable;
import net.kyori.adventure.text.Component;

public record ActionbarEntry(String id, Component content) implements Identifiable<String> {

  @Override
  public String getId() {
    return this.id;
  }
}
