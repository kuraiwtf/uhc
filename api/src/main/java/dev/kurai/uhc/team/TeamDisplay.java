package dev.kurai.uhc.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamDisplay {
    NONE("Aucun", false, false),
    ACTION_BAR("Action Bar", true, false),
    LUNAR_CLIENT("Lunar Client", false, true),
    LUNAR_CLIENT_AND_ACTION_BAR("Lunar Client & Action Bar", true, true);

    private final String label;
    private final boolean actionBar;
    private final boolean lunarClient;

    public TeamDisplay next() {
        final var values = values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
