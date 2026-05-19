package dev.kurai.uhc.menu.rules.drop;

import dev.kurai.uhc.game.drop.AbstractDropRateModifier;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.CC;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DropRateMenu extends PaginatedMenu {

  private final DropRateService dropRateService;

  public DropRateMenu(final Player player, final DropRateService dropRateService) {
    super("Taux de drop", MenuSize.FIVE, player);
    this.dropRateService = dropRateService;
  }

  @Override
  public List<Button> getEntries() {
    return this.dropRateService.getModifiers().stream()
        .map(DropRateButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  private static final class DropRateButton extends Button {

    private final AbstractDropRateModifier modifier;

    private DropRateButton(final AbstractDropRateModifier modifier) {
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.modifier.getIcon())
          .name("&a&l" + this.modifier.getName())
          .lore(
              "",
              "&a " + CC.SQUARE + "&f Taux de drop: &a%d%%".formatted(this.modifier.getDropRate()),
              "")
          .amount(this.modifier.getDropRate())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var dropRateMenu = new DropRateConfigurationMenu(menu.getPlayer(), this.modifier);
      dropRateMenu.setPreviousMenu(menu);
      dropRateMenu.open();
    }
  }
}
