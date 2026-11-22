package dev.kurai.uhc.extension.minigame.duel.instance.defaults.tictactoe;

import java.util.Objects;

public record Point(int x, int y) {

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Point(int x1, int y1))) return false;
        return this.x == x1 && this.y == y1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
