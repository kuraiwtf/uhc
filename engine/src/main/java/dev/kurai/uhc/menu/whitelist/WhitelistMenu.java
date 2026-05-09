package dev.kurai.uhc.menu.whitelist;

import static dev.kurai.uhc.util.CC.BAR;
import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import dev.kurai.uhc.whitelist.meta.WhitelistMeta;
import dev.kurai.uhc.whitelist.service.WhitelistService;
import java.util.Comparator;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WhitelistMenu extends PaginatedMenu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final WhitelistService whitelistService;
  private final boolean filterOfflineOnly;

  public WhitelistMenu(final Player player, final WhitelistService whitelistService) {
    this(player, whitelistService, false);
  }

  public WhitelistMenu(
      final Player player,
      final WhitelistService whitelistService,
      final boolean filterOfflineOnly) {
    super("Liste blanche", MenuSize.FIVE, player);
    this.whitelistService = whitelistService;
    this.filterOfflineOnly = filterOfflineOnly;
  }

  @Override
  public List<Button> getEntries() {
    return this.whitelistService.getWhitelistedPlayers().stream()
        .filter(meta -> !this.filterOfflineOnly || !meta.asOfflinePlayer().isOnline())
        .sorted(
            Comparator.comparing(
                meta -> {
                  final var name = meta.asOfflinePlayer().getName();
                  return name != null ? name.toLowerCase() : "";
                }))
        .map(WhitelistedPlayerButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.CYAN.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    for (final int slot : new int[] {2, 3, 5, 6, 18, 26, 38, 42}) {
      background.set(slot, GLASS);
    }

    foreground.center(new PaginationSlot(this));
    foreground.set(4, new FilterButton());

    if (this.filterOfflineOnly) {
      background.remove(18);
      foreground.set(18, new CopyOfflineNamesButton());
    }
  }

  private static final class WhitelistedPlayerButton extends Button {

    private final WhitelistMeta meta;

    private WhitelistedPlayerButton(final WhitelistMeta meta) {
      this.meta = meta;
    }

    @Override
    public ItemStack getIcon() {
      final var offlinePlayer = this.meta.asOfflinePlayer();
      final var name = offlinePlayer.getName() != null ? offlinePlayer.getName() : "Inconnu";
      final var online = offlinePlayer.isOnline();
      final var executorOffline = Bukkit.getOfflinePlayer(this.meta.executor());
      final var executorName =
          executorOffline.getName() != null ? executorOffline.getName() : "Inconnu";

      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .name((online ? "&a&l" : "&c&l") + name)
          .lore(
              "",
              "&a " + SQUARE + "&f Statut: " + (online ? "&aEn ligne" : "&cHors-ligne"),
              "&a " + SQUARE + "&f Ajouté par: &e" + executorName,
              "&a " + SQUARE + "&f Raison: &7" + this.meta.source(),
              "")
          .skullOwner(name)
          .amount(this.meta.asOfflinePlayer().isOnline() ? 1 : 0)
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }

  private final class FilterButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var filterActive = WhitelistMenu.this.filterOfflineOnly;
      return new ItemBuilder(Material.HOPPER)
          .name("&a&lFiltre")
          .lore(
              "",
              "&a " + SQUARE + "&f Affichage: " + (filterActive ? "&c&lHors-ligne" : "&a&lTous"),
              "",
              "&7" + BAR + "&f Basculer entre &atous&f les joueurs",
              "  et les joueurs &chors-ligne&f uniquement.",
              "")
          .glowing(filterActive)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = WhitelistMenu.this;
      new WhitelistMenu(menu.getPlayer(), menu.whitelistService, !menu.filterOfflineOnly).open();
    }
  }

  private final class CopyOfflineNamesButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var count =
          WhitelistMenu.this.whitelistService.getWhitelistedPlayers().stream()
              .filter(meta -> !meta.asOfflinePlayer().isOnline())
              .count();

      return new ItemBuilder(Material.PAPER)
          .name("&e&lListe hors-ligne")
          .lore(
              "",
              "&7" + BAR + "&f Envoie dans le chat la liste",
              "  de tous les joueurs whitelistés",
              "  actuellement &chors-ligne&f. &7(&e" + count + "&7)",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var player = click.getMenu().getPlayer();
      final var offlineNames =
          WhitelistMenu.this.whitelistService.getWhitelistedPlayers().stream()
              .map(WhitelistMeta::asOfflinePlayer)
              .filter(op -> !op.isOnline())
              .map(op -> op.getName() != null ? op.getName() : "Inconnu")
              .sorted(String.CASE_INSENSITIVE_ORDER)
              .toList();

      if (offlineNames.isEmpty()) {
        player.sendMessage(CC.colorize("&cAucun joueur hors-ligne dans la liste blanche."));
        return;
      }

      player.sendMessage(
          CC.colorize(
              "&fHors-ligne &7(&c"
                  + offlineNames.size()
                  + "&7)&f: &c"
                  + String.join("&f, &c", offlineNames)));
    }
  }
}
