package dev.kurai.uhc.extension.minigame.duel.instance.defaults.fourinarow;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

final class FourInARowBoard {

    static final int COLUMNS = 9;
    static final int ROWS = 5;
    static final int COLUMN_FULL = -1;

    private static final int ALIGNMENT = 4;
    private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    private final @Nullable UUID[][] cells = new UUID[COLUMNS][ROWS];

    private final boolean[][] aligned = new boolean[COLUMNS][ROWS];

    private int placed;

    int drop(final int column, final UUID player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (this.cells[column][row] == null) {
                this.cells[column][row] = player;
                this.placed++;
                return row;
            }
        }
        return COLUMN_FULL;
    }

    @Nullable UUID placer(final int column, final int row) {
        return this.cells[column][row];
    }

    boolean full() {
        return this.placed == COLUMNS * ROWS;
    }

    boolean aligned(final int column, final int row) {
        return this.aligned[column][row];
    }

    boolean markAlignments(final int column, final int row) {
        final UUID player = this.cells[column][row];
        if (player == null) {
            return false;
        }

        boolean found = false;
        for (final int[] direction : DIRECTIONS) {
            final int dx = direction[0];
            final int dy = direction[1];
            final int before = this.count(column, row, -dx, -dy, player);
            final int after = this.count(column, row, dx, dy, player);
            if (before + 1 + after < ALIGNMENT) {
                continue;
            }

            for (int step = -before; step <= after; step++) {
                this.aligned[column + step * dx][row + step * dy] = true;
            }
            found = true;
        }
        return found;
    }

    private int count(final int column, final int row, final int dx, final int dy, final UUID player) {
        int count = 0;
        int x = column + dx;
        int y = row + dy;
        while (x >= 0 && x < COLUMNS && y >= 0 && y < ROWS && player.equals(this.cells[x][y])) {
            count++;
            x += dx;
            y += dy;
        }
        return count;
    }
}
