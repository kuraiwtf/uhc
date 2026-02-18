package dev.kurai.uhc.menu.claim;

import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.profile.component.ClaimComponent;
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
public final class ClaimMenu extends PaginatedMenu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final ClaimComponent component;

  public ClaimMenu(final Player player, final ClaimComponent component) {
    super("Claim", MenuSize.FIVE, player);
    this.component = component;
  }

  @Override
  public List<Button> getEntries() {
    return this.component.getItems().stream()
        .map(itemStack -> new ClaimButton(this.component, itemStack))
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));
    this.apply(new PaginationTemplate());

    for (final int i : new int[] {2, 3, 5, 6, 18, 26, 38, 39, 41, 42}) {
      background.set(i, GLASS);
    }

    foreground.center(new PaginationSlot(this));
  }

  private static final class ClaimButton extends Button {

    private final ClaimComponent component;
    private final ItemStack item;

    private ClaimButton(final ClaimComponent component, final ItemStack item) {
      this.component = component;
      this.item = item;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.item)
          .lore("", "&7" + CC.BAR + "&f Cliquez pour récupérer", "&f  cet objet.", "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var player = click.getMenu().getPlayer();
      final var left = player.getInventory().addItem(this.item);
      if (!left.isEmpty()) {
        return;
      }

      this.component.getItems().remove(this.item);
      click.getMenu().update();
    }
  }
}
