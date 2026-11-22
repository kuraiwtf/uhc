package dev.kurai.uhc.extension.minigame.duel.instance.defaults.fourinarow;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kurai.uhc.extension.minigame.duel.instance.DuelInstance;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import dev.kurai.uhc.util.Color;
import net.j4c0b3y.api.menu.Menu;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class FourInARowInstance extends DuelInstance {

    private static final List<Color> TOKEN_COLORS =
            List.of(Color.RED, Color.YELLOW, Color.BLUE, Color.AQUA, Color.GOLD, Color.LIGHT_PURPLE, Color.WHITE);

    private final FourInARowBoard board;
    private final Map<UUID, Menu> menus;
    private final Map<UUID, Color> assignedColors;

    private Player currentPlayer;

    public FourInARowInstance(final UUID first, final UUID second) {
        super(first, second, DuelType.FOUR_IN_A_ROW);
        this.board = new FourInARowBoard();
        this.menus = Maps.newHashMap();
        this.assignedColors = Maps.newHashMap();
    }

    @Override
    public void onStart() {
        this.currentPlayer = ThreadLocalRandom.current().nextBoolean() ? this.first : this.second;

        final List<Color> colors = Lists.newArrayList(TOKEN_COLORS);
        Collections.shuffle(colors);
        this.assignedColors.put(this.first.getUniqueId(), colors.get(0));
        this.assignedColors.put(this.second.getUniqueId(), colors.get(1));

        for (final Player player : this.players()) {
            this.menus.put(player.getUniqueId(), new FourInARowMenu(player, this));
        }

        for (final Menu menu : this.menus.values()) {
            menu.open();
        }
    }

    @Override
    public void update() {
        for (final Menu menu : this.menus.values()) {
            menu.update();
        }
    }

    @Override
    public void onStop() {
        this.showResultThenClose(this.menus.values());
        this.menus.clear();
    }

    void play(final Player player, final int column) {
        if (this.finished || !player.getUniqueId().equals(this.currentPlayer.getUniqueId())) {
            return;
        }

        final int row = this.board.drop(column, player.getUniqueId());
        if (row == FourInARowBoard.COLUMN_FULL) {
            return;
        }

        if (this.board.markAlignments(column, row)) {
            this.end(player);
        } else if (this.board.full()) {
            this.end(null);
        } else {
            this.currentPlayer = player.getUniqueId().equals(this.first.getUniqueId()) ? this.second : this.first;
            this.update();
        }
    }

    private void end(final @Nullable Player winner) {
        this.announceResult(winner);
        this.stop();
    }

    Player currentPlayer() {
        return this.currentPlayer;
    }

    @Nullable UUID placer(final int column, final int row) {
        return this.board.placer(column, row);
    }

    boolean aligned(final int column, final int row) {
        return this.board.aligned(column, row);
    }

    Color color(final UUID uniqueId) {
        return this.assignedColors.getOrDefault(uniqueId, Color.WHITE);
    }
}
