package dev.kurai.uhc.extension.minigame.duel.instance.defaults.tictactoe;

import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.Color;
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
public final class TicTacToeMenu extends Menu {

    private final TicTacToeInstance instance;

    public TicTacToeMenu(final Player viewer, final TicTacToeInstance instance) {
        super(instance.duelType().getName(), MenuSize.FIVE, viewer);
        this.instance = instance;
    }

    @Override
    public void onClose() {
        this.instance.handleMenuClose(this.getPlayer());
    }

    @Override
    public void setup(final BackgroundLayer back, final ForegroundLayer front) {
        this.apply(new BorderTemplate(DyeColor.YELLOW.getData()));

        this.update(front);
    }

    @Override
    public void update() {
        this.update(this.getForeground());
        super.update();
    }

    private void update(final ForegroundLayer layer) {
        layer.set(19, new TurnButton(this.instance));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                layer.set(3 + i, 1 + j, new PointButton(this.instance, new Point(i, j)));
            }
        }
    }

    private static final class TurnButton extends Button {

        private final TicTacToeInstance instance;

        private TurnButton(final TicTacToeInstance instance) {
            this.instance = instance;
        }

        @Override
        public ItemStack getIcon() {
            final Player player = this.instance.currentPlayer();
            return new ItemBuilder(Material.SKULL_ITEM)
                    .data(3)
                    .name("§6§l" + player.getName())
                    .lore("", "C'est à §b" + player.getName() + "§f de jouer !", "")
                    .skullOwner(player.getName())
                    .asItemStack();
        }
    }

    private static final class PointButton extends Button {

        private final TicTacToeInstance instance;
        private final Point point;

        private PointButton(final TicTacToeInstance instance, final Point point) {
            this.instance = instance;
            this.point = point;
        }

        @Override
        public ItemStack getIcon() {
            final UUID placer = this.instance.placer(this.point);
            final Color color = placer == null ? Color.BLACK : this.instance.color(placer);

            return new ItemBuilder(Material.STAINED_GLASS)
                    .name("§0")
                    .data(color.asDyeColor().getData())
                    .glowing(this.instance.aligned(this.point))
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.instance.placePoint(click.getMenu().getPlayer(), this.point.x(), this.point.y());
            this.instance.update();
        }
    }
}
