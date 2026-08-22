package dev.kurai.uhc.menu.list;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
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

public final class ComponentListMenu extends PaginatedMenu {

  private final Profile profile;

  public ComponentListMenu(final Player player, final Profile profile) {
    super("Composants", MenuSize.FIVE, player);
    this.profile = profile;
  }

  @Override
  public List<Button> getEntries() {
    return this.profile.getComponents().stream()
        .map(ComponentButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  private static final class ComponentButton extends Button {

    private final Component component;

    private ComponentButton(final Component component) {
      this.component = component;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.NETHER_STAR)
          .name("&e&l%s".formatted(this.component.getClass().getSimpleName()))
          .lore("", "&e&l»&r Cliquez-ici pour accéder aux détails.", "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = new ComponentDetailsMenu(click.getMenu().getPlayer(), this.component);
      menu.setPreviousMenu(click.getMenu());
      menu.open();
    }
  }
}
