package dev.kurai.uhc.menu.module;

import static dev.kurai.uhc.util.CC.BAR_2;
import static dev.kurai.uhc.util.CC.SQUARE;

import com.google.common.collect.Lists;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.module.event.ModuleEvent;
import dev.kurai.uhc.module.event.ModuleEventHolder;
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
import org.bukkit.inventory.ItemStack;

public final class ModuleRandomEventMenu extends PaginatedMenu {

  private final ModuleEventHolder eventHolder;

  public ModuleRandomEventMenu(final Player player, final ModuleEventHolder eventHolder) {
    super("Évènements aléatoires", MenuSize.FIVE, player);
    this.eventHolder = eventHolder;
  }

  @Override
  public List<Button> getEntries() {
    return this.eventHolder.events().stream()
        .map(EventButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer backgroundLayer, final ForegroundLayer front) {
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));

    front.center(new PaginationSlot(this));
  }

  private static final class EventButton extends Button {

    private final ModuleEvent<?> event;

    private EventButton(final ModuleEvent<?> event) {
      this.event = event;
    }

    @Override
    public ItemStack getIcon() {
      final var lore = Lists.<String>newArrayList();
      lore.add("");
      lore.add("§a" + BAR_2 + "§f §lInformations");
      lore.add("§a " + SQUARE + "§f Taux d'activation: §a" + this.event.rate() + "%");
      lore.add("§a " + SQUARE + "§f Statut: " + (this.event.active() ? "§aActivé" : "§cDésactivé"));
      lore.add("");
      lore.add("§a" + BAR_2 + "§f §lDescription");
      for (final String line : this.event.lore()) {
        lore.add("§a " + SQUARE + "§r " + line);
      }
      lore.add("");

      return new ItemBuilder(this.event.provideIcon())
          .name("§a§l" + this.event.getName())
          .lore(lore)
          .asItemStack();
    }
  }
}
