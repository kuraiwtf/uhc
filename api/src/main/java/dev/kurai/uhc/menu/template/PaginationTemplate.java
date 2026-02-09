package dev.kurai.uhc.menu.template;

import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.template.Template;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PaginationTemplate implements Template {

  @Override
  public void apply(final BackgroundLayer background, final ForegroundLayer foreground) {
    if (!(foreground.getMenu() instanceof final PaginatedMenu menu)) {
      return;
    }

    final var slots = menu.getTotalSlots();

    foreground.set(slots - 6, new PreviousPageButton(menu));
    foreground.set(slots - 4, new NextPageButton(menu));
  }

  private static final class PreviousPageButton extends Button {

    private final PaginatedMenu menu;

    public PreviousPageButton(final PaginatedMenu menu) {
      this.menu = menu;
    }

    @Override
    public ItemStack getIcon() {
      return (!this.menu.hasPreviousPage()
          ? new ItemStack(Material.AIR)
          : new ItemBuilder(Material.SKULL_ITEM)
              .data(3)
              .name(
                  "&8» &rPage &cprécédente &8(&c"
                      + (this.menu.getPage() - 1)
                      + "&4/&c"
                      + this.menu.getTotalPages()
                      + "&8)")
              .url(
                  "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc2MjMwYTBhYzUyYWYxMWU0YmM4NDAwOWM2ODkwYTQwMjk0NzJmMzk0N2I0ZjQ2NWI1YjU3MjI4ODFhYWNjNyJ9fX0=")
              .asItemStack());
    }

    @Override
    public void onClick(final ButtonClick click) {
      if (!this.menu.hasPreviousPage()) {
        return;
      }

      this.menu.previousPage();
    }
  }

  private static final class NextPageButton extends Button {

    private final PaginatedMenu menu;

    public NextPageButton(final PaginatedMenu menu) {
      this.menu = menu;
    }

    @Override
    public ItemStack getIcon() {
      return (!this.menu.hasNextPage()
          ? new ItemStack(Material.AIR)
          : new ItemBuilder(Material.SKULL_ITEM)
              .data(3)
              .name(
                  "&8» &rPage &asuivante &8(&a"
                      + (this.menu.getPage() + 1)
                      + "&2/&a"
                      + this.menu.getTotalPages()
                      + "&8)")
              .url(
                  "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJmOGI2Mjc3Y2QzNjI2NjI4M2NiNWE5ZTY5NDM5NTNjNzgzZTZmZjdkNmEyZDU5ZDE1YWQwNjk3ZTkxZDQzYyJ9fX0=")
              .asItemStack());
    }

    @Override
    public void onClick(final ButtonClick click) {
      if (!this.menu.hasNextPage()) {
        return;
      }

      this.menu.nextPage();
    }
  }
}
