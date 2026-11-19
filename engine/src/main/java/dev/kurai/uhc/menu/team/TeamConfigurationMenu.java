package dev.kurai.uhc.menu.team;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.team.TeamDisplay;
import dev.kurai.uhc.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static dev.kurai.uhc.util.CC.ARROW_RIGHT;
import static dev.kurai.uhc.util.CC.SQUARE;

public final class TeamConfigurationMenu extends Menu {

    private static final String CLICK = "&e" + ARROW_RIGHT + " &eClique pour y accéder";

    private final TeamModule module;

    public TeamConfigurationMenu(final Player player, final TeamModule module) {
        super("Gestion des équipes", MenuSize.THREE, player);
        this.module = module;
    }

    private static String status(final boolean enabled) {
        return "&a " + SQUARE + "&f Statut: " + (enabled ? "&aActivé" : "&cDésactivé");
    }

    @Override
    public void setup(final BackgroundLayer background, final ForegroundLayer front) {
        this.apply(new BackTemplate(this.getPreviousMenu()));
        this.apply(new BorderTemplate(DyeColor.LIME.getData()));

        front.set(11, new TeamDisplayButton(this.module));
        front.set(12, new TeamInventoryButton(this.module));
        front.set(13, new TeamSizeButton(this.module));
        front.set(14, new FriendlyFireButton(this.module));
        front.set(15, new RandomTeamButton(this.module));
    }

    @RequiredArgsConstructor
    private static final class TeamDisplayButton extends Button {

        private final TeamModule module;

        @Override
        public ItemStack getIcon() {
            final var lore = new ArrayList<String>();
            lore.add("&fPermet de &echanger");
            lore.add("&fl'affichage des &béquipes&f.");
            lore.add("");
            for (final var display : TeamDisplay.values()) {
                final var selected = display == this.module.teamDisplay();
                lore.add((selected ? "&a " : "&7 ") + SQUARE + (selected ? "&a " : "&7 ") + display.label());
            }
            lore.add("");
            lore.add(CLICK);

            return new ItemBuilder(Material.EYE_OF_ENDER)
                    .name("&6&lAffichage des équipes")
                    .lore(lore.toArray(String[]::new))
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.module.teamDisplay(this.module.teamDisplay().next());
            click.getMenu().update();
        }
    }

    @RequiredArgsConstructor
    private static final class TeamInventoryButton extends Button {

        private final TeamModule module;

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.CHEST)
                    .name("&6&lTeam Inventory")
                    .lore(
                            "&fPermet de &echanger&f si les",
                            "&fteams ont un &binventaire&f.",
                            "",
                            status(this.module.teamInventory()),
                            "",
                            CLICK)
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.module.teamInventory(!this.module.teamInventory());
            click.getMenu().update();
        }
    }

    @RequiredArgsConstructor
    private static final class TeamSizeButton extends Button {

        private final TeamModule module;

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.SADDLE)
                    .name("&6&lTaille des équipes")
                    .lore(
                            "&fPermet de &echanger&f la",
                            "&ftaille des &béquipes&f.",
                            "",
                            "&e " + SQUARE + "&f Taille: &e"
                                    + (this.module.ffa()
                                            ? "FFA"
                                            : "%dvs%d".formatted(this.module.teamSize(), this.module.teamSize())),
                            "",
                            CLICK)
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            final var type = click.getType();
            this.module.teamSize(Math.clamp(
                    this.module.teamSize() + (type.isLeftClick() ? 1 : type.isRightClick() ? -1 : 0),
                    this.module.minTeamSize(),
                    this.module.maxTeamSize()));
            click.getMenu().update();
        }
    }

    @RequiredArgsConstructor
    private static final class FriendlyFireButton extends Button {

        private final TeamModule module;

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.DIAMOND_SWORD)
                    .name("&6&lFriendly Fire")
                    .lore(
                            "&fPermet de &echanger&f le",
                            "&ffriendly fire des &béquipes&f.",
                            "",
                            status(this.module.friendlyFire()),
                            "",
                            CLICK)
                    .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.module.friendlyFire(!this.module.friendlyFire());
            click.getMenu().update();
        }
    }

    @RequiredArgsConstructor
    private static final class RandomTeamButton extends Button {

        private final TeamModule module;

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.NAME_TAG)
                    .name("&6&lÉquipes aléatoires")
                    .lore(
                            "&fPermet de &echanger&f si les",
                            "&fteams sont &baléatoires&f ou",
                            "&fnon.",
                            "",
                            "&e " + SQUARE + "&f Statut: " + (this.module.randomTeam() ? "&dRandom" : "&aChosen"),
                            "",
                            CLICK)
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            this.module.randomTeam(!this.module.randomTeam());
            click.getMenu().update();
        }
    }
}
