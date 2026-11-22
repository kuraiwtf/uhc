package dev.kurai.uhc.extension.minigame.menu;

import dev.kurai.uhc.extension.minigame.duel.DuelService;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class DuelTypeSelectionMenu extends PaginatedMenu {

    private final Player receiver;
    private final DuelService duelService;

    public DuelTypeSelectionMenu(final Player player, final Player receiver, final DuelService duelService) {
        super("Duel - " + receiver.getName(), MenuSize.THREE, player);
        this.receiver = receiver;
        this.duelService = duelService;
    }

    @Override
    public List<Button> getEntries() {
        return Arrays.stream(DuelType.values())
                .map(duelType -> new DuelTypeButton(this.receiver, duelType, this.duelService))
                .map(Button.class::cast)
                .toList();
    }

    @Override
    public void setup(final BackgroundLayer back, final ForegroundLayer front) {
        this.apply(new BorderTemplate(DyeColor.YELLOW.getData()));
        this.apply(new PaginationTemplate());

        front.center(new PaginationSlot(this));
    }

    private static final class DuelTypeButton extends Button {

        private final Player receiver;

        private final DuelType duelType;
        private final DuelService duelService;

        private DuelTypeButton(final Player receiver, final DuelType duelType, final DuelService duelService) {
            this.receiver = receiver;
            this.duelType = duelType;
            this.duelService = duelService;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(this.duelType.icon())
                    .name("§6§l" + this.duelType.getName())
                    .lore(
                            "",
                            "§6 » §fCliquez pour défier le",
                            "    joueur §b" + this.receiver.getName() + " §fen duel",
                            "    en §6" + this.duelType.getName() + "§f.",
                            "")
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            final Menu menu = click.getMenu();
            this.duelService.sendRequest(menu.getPlayer(), this.receiver, this.duelType);
            menu.close();
        }
    }
}
