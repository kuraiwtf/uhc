package dev.kurai.uhc.menu.rules.view;

import dev.kurai.uhc.game.drop.AbstractDropRateModifier;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DropRatesViewMenu extends PaginatedMenu {

  private final DropRateService dropRateService;

  public DropRatesViewMenu(final Player player, final DropRateService dropRateService) {
    super("Taux de drop", MenuSize.FIVE, player);
    this.dropRateService = dropRateService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  @Override
  public List<Button> getEntries() {
    return this.dropRateService.getModifiers().stream().map(DropRatesViewMenu::button).toList();
  }

  private static Button button(final AbstractDropRateModifier modifier) {
    return new ItemButton(
        new ItemBuilder(modifier.getIcon())
            .name("&a&l" + modifier.getName())
            .lore(
                "",
                "&a " + CC.SQUARE + "&f Taux de drop: &a%d%%".formatted(modifier.getDropRate()),
                "")
            .amount(modifier.getDropRate())
            .asItemStack());
  }
}
