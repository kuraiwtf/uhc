package dev.kurai.uhc.game.cycle;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.util.api.Identifiable;
import lombok.Getter;

@Getter
public abstract class AbstractCycle implements Identifiable<String> {

  protected final String id;
  protected final UltraHardcoreAPI ultraHardcore;

  protected AbstractCycle(final String id, final UltraHardcoreAPI ultraHardcore) {
    this.id = id;
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public final String getId() {
    return this.id;
  }

  public void onStart() {}

  public void onSkip() {}

  public void onStop() {}
}
