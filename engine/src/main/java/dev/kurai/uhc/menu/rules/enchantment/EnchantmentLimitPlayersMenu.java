package dev.kurai.uhc.menu.rules.enchantment;

import dev.kurai.uhc.game.enchantment.EnchantmentLimitService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.annotation.AutoUpdate;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/** Lists the players still in the game, to edit their own enchantment limits. */
@NullMarked
@AutoUpdate(20)
public final class EnchantmentLimitPlayersMenu extends PaginatedMenu {

    private final ProfileService profileService;
    private final EnchantmentLimitService limitService;

    public EnchantmentLimitPlayersMenu(
            final Player player, final ProfileService profileService, final EnchantmentLimitService limitService) {
        super("Joueurs", MenuSize.FIVE, player);
        this.profileService = profileService;
        this.limitService = limitService;
    }

    @Override
    public @Unmodifiable List<Button> getEntries() {
        return this.profileService
                .getProfiles(profile -> !profile.dead() && !profile.spectating())
                .stream()
                .map(PlayerButton::new)
                .map(Button.class::cast)
                .toList();
    }

    @Override
    public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
        this.apply(new BackTemplate(this.getPreviousMenu()));
        this.apply(new BorderTemplate(DyeColor.PURPLE.getData()));
        this.apply(new PaginationTemplate());

        foreground.center(new PaginationSlot(this));
    }

    private final class PlayerButton extends Button {

        private final Profile profile;

        private PlayerButton(final Profile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.SKULL_ITEM)
                    .data(3)
                    .name("&6&l" + this.profile.getName())
                    .lore("", "&e&l»&r Cliquez-ici pour modifier ses limites", "  d'enchantement.", "")
                    .skullOwner(this.profile.getName())
                    .asItemStack();
        }

        @Override
        public void onClick(final ButtonClick click) {
            final var menu = new EnchantmentLimitMenu(
                    click.getMenu().getPlayer(),
                    EnchantmentLimitPlayersMenu.this.limitService,
                    this.profile.getId(),
                    this.profile.getName());
            menu.setPreviousMenu(click.getMenu());
            menu.open();
        }
    }
}
