package dev.kurai.uhc.menu.list;

import com.google.common.collect.Lists;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
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

@NullMarked
@AutoUpdate(20)
public final class PlayerListMenu extends PaginatedMenu {

  private final ProfileService profileService;

  public PlayerListMenu(final Player player, final ProfileService profileService) {
    super("Liste des joueurs", MenuSize.FIVE, player);
    this.profileService = profileService;
  }

  @Override
  public @Unmodifiable List<Button> getEntries() {
    return this.profileService.getProfiles().stream()
        .map(ProfileButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  private static final class ProfileButton extends Button {

    private final Profile profile;

    private ProfileButton(final Profile profile) {
      this.profile = profile;
    }

    @Override
    public ItemStack getIcon() {
      final var lines = Lists.<String>newArrayList();

      lines.add("");
      lines.add("Components:&a %d".formatted(this.profile.getComponents().size()));
      lines.add("");

      final var powers = this.profile.getPowers();
      if (!powers.isEmpty()) {
        lines.add("Powers:");
        for (final var power : powers) {
          lines.add("&8 -&r " + power.getId());
        }
        lines.add("");
      }

      lines.add("&e&l»&r Cliquez-ici pour accéder aux composants.");
      lines.add("");

      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .name("&6&l" + this.profile.getName())
          .lore(lines)
          .skullOwner(this.profile.getName())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = new ComponentListMenu(click.getMenu().getPlayer(), this.profile);
      menu.setPreviousMenu(click.getMenu());
      menu.open();
    }
  }
}
