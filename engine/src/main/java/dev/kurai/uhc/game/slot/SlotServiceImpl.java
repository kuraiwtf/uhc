package dev.kurai.uhc.game.slot;

import dev.kurai.uhc.game.slot.impl.MutableSlotProvider;
import dev.kurai.uhc.util.api.option.Option;
import lombok.Getter;
import lombok.Setter;

import static dev.kurai.uhc.util.api.option.Option.rangedOption;
import static net.kyori.adventure.key.Key.key;

@Getter
@Setter
public final class SlotServiceImpl implements SlotService {

    private static final Option<Integer> SLOTS_OPTION = rangedOption(key("slots"), 1, 500, 20);

    private SlotProvider slotProvider;

    public SlotServiceImpl() {
        this.slotProvider = new MutableSlotProvider() {
            @Override
            public int slots() {
                return SLOTS_OPTION.getValue();
            }

            @Override
            public void slots(final int slots) {
                SLOTS_OPTION.setValue(slots);
            }
        };
    }
}
