package dev.kurai.uhc.menu.rules.disconnect;

import static dev.kurai.uhc.util.CC.BAR;
import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.game.disconnect.DisconnectService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import dev.kurai.uhc.util.TimeUtil;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class DisconnectMenu extends Menu {

  private final DisconnectService disconnectService;

  public DisconnectMenu(final Player player, final DisconnectService disconnectService) {
    super("Déconnexion", MenuSize.THREE, player);
    this.disconnectService = disconnectService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.RED.getData()));

    front.set(12, new TimeButton(this.disconnectService));
    front.set(14, new StrategyButton(this.disconnectService));
  }

  private static final class TimeButton extends Button {

    private final DisconnectService disconnectService;

    private TimeButton(final DisconnectService disconnectService) {
      this.disconnectService = disconnectService;
    }

    @Override
    public ItemStack getIcon() {
      final int time = this.disconnectService.disconnectTime();
      return new ItemBuilder(Material.WATCH)
          .name("&6&lTemps de déconnexion")
          .lore(
              "",
              "&6 " + SQUARE + "&f Temps: &b" + TimeUtil.formatDuration(time * 1000L),
              "",
              "&7" + BAR + "&f Permet de modifier le",
              "  temps avant l'élimination",
              "  automatique d'un joueur",
              "  &chors-ligne&r.",
              "")
          .amount(Math.clamp(time / 60, 1, 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final Menu menu = click.getMenu();
      final Menu editMenu = new DisconnectTimerEditMenu(menu.getPlayer(), this.disconnectService);
      editMenu.setPreviousMenu(menu);
      editMenu.open();
    }
  }

  private static final class StrategyButton extends Button {

    private final DisconnectService disconnectService;

    private StrategyButton(final DisconnectService disconnectService) {
      this.disconnectService = disconnectService;
    }

    private static String label(final DisconnectService.Strategy strategy) {
      return switch (strategy) {
        case DISABLED -> "&cDésactivée";
        case ONCE -> "&eUne fois";
        case CUMULATIVE -> "&aCumulative";
      };
    }

    @Override
    public ItemStack getIcon() {
      final DisconnectService.Strategy current = this.disconnectService.strategy();
      return new ItemBuilder(Material.REDSTONE_COMPARATOR)
          .name("&6&lStratégie de déconnexion")
          .lore(
              "",
              "&6 " + SQUARE + "&f Stratégie: " + label(current),
              "",
              "&7" + BAR + "&f Désactivée: aucune élimination.",
              "&7" + BAR + "&f Une fois: le temps est remis à",
              "  zéro à chaque reconnexion.",
              "&7" + BAR + "&f Cumulative: le temps est",
              "  conservé entre les connexions.",
              "",
              "&eClique pour changer.")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final DisconnectService.Strategy[] values = DisconnectService.Strategy.values();
      this.disconnectService.strategy(
          values[(this.disconnectService.strategy().ordinal() + 1) % values.length]);
      click.getMenu().update();
    }
  }
}
