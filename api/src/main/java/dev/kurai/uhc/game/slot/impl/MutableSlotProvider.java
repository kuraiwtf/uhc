package dev.kurai.uhc.game.slot.impl;

import dev.kurai.uhc.game.slot.SlotProvider;

public interface MutableSlotProvider extends SlotProvider {

  @Override
  int slots();

  void slots(final int slots);
}
