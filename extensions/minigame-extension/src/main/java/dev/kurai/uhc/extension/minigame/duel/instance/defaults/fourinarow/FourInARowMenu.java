package dev.kurai.uhc.extension.minigame.duel.instance.defaults.fourinarow;

import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.annotation.AutoUpdate;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AutoUpdate(2)
public final class FourInARowMenu extends Menu {

    private static final int CONTROLS_ROW = FourInARowBoard.ROWS;
    private static final int TURN_COLUMN = FourInARowBoard.COLUMNS / 2;

    private final FourInARowInstance instance;

    public FourInARowMenu(final Player viewer, final FourInARowInstance instance) {
        super(instance.duelType().getName(), MenuSize.SIX, viewer);
        this.instance = instance;
    }

    @Override
    public void onClose() {
        this.instance.handleMenuClose(this.getPlayer());
    }

    @Override
    public void setup(final BackgroundLayer back, final ForegroundLayer front) {
        for (int column = 0; column < FourInARowBoard.COLUMNS; column++) {
            for (int row = 0; row < FourInARowBoard.ROWS; row++) {
                front.set(column, row, new CellButton(this.instance, column, row));
            }

            front.set(
                    column,
                    CONTROLS_ROW,
                    column == TURN_COLUMN
                            ? new TurnButton(this.instance, column)
                            : new ColumnButton(this.instance, column));
        }
    }

    private abstract static class PlayButton extends Button {

        protected final FourInARowInstance instance;
        protected final int column;

        private PlayButton(final FourInARowInstance instance, final int column) {
            this.instance = instance;
            this.column = column;
        }

        @Override
        public final void onClick(final ButtonClick click) {
            this.instance.play(click.getMenu().getPlayer(), this.column);
        }
    }

    private static final class CellButton extends PlayButton {

        private final int row;

        private CellButton(final FourInARowInstance instance, final int column, final int row) {
            super(instance, column);
            this.row = row;
        }

        @Override
        public ItemStack getIcon() {
            final UUID placer = this.instance.placer(this.column, this.row);
            final DyeColor color = placer == null ? DyeColor.GRAY : this.instance.color(placer).asDyeColor();

            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name("§0")
                    .data(color.getData())
                    .glowing(this.instance.aligned(this.column, this.row))
                    .asItemStack();
        }
    }

    private static final class ColumnButton extends PlayButton {

        private ColumnButton(final FourInARowInstance instance, final int column) {
            super(instance, column);
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .data(DyeColor.LIME.getData())
                    .name("&e&lColonne")
                    .lore("Cliquez sur une colonne pour &ajouer&f.", "", "&6» &eCliquez sur cette colonne pour jouer")
                    .asItemStack();
        }
    }

    private static final class TurnButton extends PlayButton {

        private TurnButton(final FourInARowInstance instance, final int column) {
            super(instance, column);
        }

        @Override
        public ItemStack getIcon() {
            final String name = this.instance.currentPlayer().getName();

            return new ItemBuilder(Material.SKULL_ITEM)
                    .data(3)
                    .name("&e&l" + name)
                    .lore(
                            "C'est au tour de &e" + name + "&f de",
                            "jouer.",
                            "",
                            "&6» &eCliquez sur une colonne pour jouer.")
                    .skullOwner(name)
                    .asItemStack();
        }
    }
}
