package dev.kurai.uhc.extension.minigame.duel.instance.defaults.rps;

import dev.kurai.uhc.menu.template.BorderTemplate;
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

@AutoUpdate(2)
public final class RockPaperScissorsMenu extends Menu {

    private final RockPaperScissorsInstance instance;

    public RockPaperScissorsMenu(final Player viewer, final RockPaperScissorsInstance instance) {
        super(instance.duelType().getName(), MenuSize.THREE, viewer);
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
        layer.set(4, new TimeLeftButton(this.instance));

        for (final RockPaperScissorsChoice choice : RockPaperScissorsChoice.values()) {
            layer.set(12 + choice.ordinal(), new ChoiceButton(this.getPlayer(), this.instance, choice));
        }
    }

    private static final class TimeLeftButton extends Button {

        private final RockPaperScissorsInstance instance;

        private TimeLeftButton(final RockPaperScissorsInstance instance) {
            this.instance = instance;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.WATCH)
                    .name("&c&lTemps restant")
                    .lore("", "Il reste §b" + this.instance.timeLeft() + " seconde(s)", "avant la fin du jeu...", "")
                    .amount(this.instance.timeLeft())
                    .asItemStack();
        }
    }

    private static final class ChoiceButton extends Button {

        private final Player viewer;
        private final RockPaperScissorsInstance instance;
        private final RockPaperScissorsChoice choice;

        private ChoiceButton(
                final Player viewer, final RockPaperScissorsInstance instance, final RockPaperScissorsChoice choice) {
            this.viewer = viewer;
            this.instance = instance;
            this.choice = choice;
        }

        @Override
        public ItemStack getIcon() {
            final RockPaperScissorsChoice currentChoice = this.instance.choice(this.viewer.getUniqueId());
            final boolean isCurrent = currentChoice != null && currentChoice == this.choice;
            final RockPaperScissorsChoice winningChoice = this.instance.winningChoice();
            final boolean highlighted = winningChoice == null ? isCurrent : winningChoice == this.choice;
            return new ItemBuilder(this.choice.icon())
                    .name("§6§l" + this.choice.getName())
                    .lore("", "§6 » §fCliquez pour changer", "    votre choix à §6" + this.choice.getName() + "§f.", "")
                    .glowing(highlighted)
                    .amount(isCurrent ? 1 : 0)
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.instance.choice(this.viewer.getUniqueId(), this.choice);
        }
    }
}
