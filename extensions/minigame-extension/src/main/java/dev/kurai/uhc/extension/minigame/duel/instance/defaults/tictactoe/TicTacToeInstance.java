package dev.kurai.uhc.extension.minigame.duel.instance.defaults.tictactoe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.kurai.uhc.extension.minigame.duel.instance.DuelInstance;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import dev.kurai.uhc.util.Color;
import net.j4c0b3y.api.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TicTacToeInstance extends DuelInstance {

    private static final int CELL_COUNT = 9;

    private static final List<Color> TOKEN_COLORS =
            List.of(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.AQUA, Color.LIGHT_PURPLE, Color.WHITE);

    private static final Point[][] POSSIBLE_WINS = {
        {new Point(0, 0), new Point(0, 1), new Point(0, 2)},
        {new Point(1, 0), new Point(1, 1), new Point(1, 2)},
        {new Point(2, 0), new Point(2, 1), new Point(2, 2)},
        {new Point(0, 0), new Point(1, 0), new Point(2, 0)},
        {new Point(0, 1), new Point(1, 1), new Point(2, 1)},
        {new Point(0, 2), new Point(1, 2), new Point(2, 2)},
        {new Point(0, 0), new Point(1, 1), new Point(2, 2)},
        {new Point(2, 0), new Point(1, 1), new Point(0, 2)},
    };

    private final Map<UUID, Menu> menus;
    private final Map<UUID, Color> assignedColors;
    private final Map<Point, UUID> points;
    private final Set<Point> alignedPoints;

    private Player currentPlayer;

    public TicTacToeInstance(final UUID first, final UUID second) {
        super(first, second, DuelType.TIC_TAC_TOE);
        this.menus = Maps.newHashMap();
        this.assignedColors = Maps.newHashMap();
        this.points = Maps.newHashMap();
        this.alignedPoints = Sets.newHashSet();
    }

    @Override
    public void onStart() {
        this.currentPlayer = ThreadLocalRandom.current().nextBoolean() ? this.first : this.second;

        final List<Color> colors = Lists.newArrayList(TOKEN_COLORS);
        Collections.shuffle(colors);
        this.assignedColors.put(this.first.getUniqueId(), colors.get(0));
        this.assignedColors.put(this.second.getUniqueId(), colors.get(1));

        this.menus.put(this.first.getUniqueId(), new TicTacToeMenu(this.first, this));
        this.menus.put(this.second.getUniqueId(), new TicTacToeMenu(this.second, this));

        for (final Menu menu : this.menus.values()) {
            menu.open();
        }
    }

    @Override
    public void onStop() {
        this.showResultThenClose(this.menus.values());
        this.menus.clear();
    }

    private void validate(final Player player) {
        for (final Point[] winPoints : POSSIBLE_WINS) {
            boolean win = false;
            UUID currentId = null;
            for (final Point point : winPoints) {
                final UUID placer = this.placer(point);
                if (placer == null) {
                    win = false;
                    break;
                }

                if (currentId != null && !currentId.equals(placer)) {
                    currentId = null;
                    win = false;
                    break;
                }

                currentId = placer;
                win = true;
            }

            if (win) {
                Collections.addAll(this.alignedPoints, winPoints);
            }
        }

        if (!this.alignedPoints.isEmpty()) {
            this.announceResult(player);
            this.stop();
        } else if (this.points.size() == CELL_COUNT) {
            this.announceResult(null);
            this.stop();
        }
    }

    @Override
    public void update() {
        for (final Menu menu : this.menus.values()) {
            menu.update();
        }
    }

    public void placePoint(final Player player, final int i, final int j) {
        if (this.finished || !player.getUniqueId().equals(this.currentPlayer.getUniqueId())) {
            return;
        }

        final Point coordinate = coordinate(i, j);
        if (this.points.containsKey(coordinate)) {
            return;
        }

        this.points.put(coordinate, player.getUniqueId());
        this.validate(player);
        if (this.finished) {
            return;
        }

        this.currentPlayer = player.getUniqueId().equals(this.first.getUniqueId()) ? this.second : this.first;
        this.update();
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public UUID placer(final Point point) {
        return this.points.get(point);
    }

    public boolean aligned(final Point point) {
        return this.alignedPoints.contains(point);
    }

    public Color color(final UUID uniqueId) {
        return this.assignedColors.getOrDefault(uniqueId, Color.WHITE);
    }

    public static Point coordinate(final int i, final int j) {
        return new Point(i, j);
    }
}
