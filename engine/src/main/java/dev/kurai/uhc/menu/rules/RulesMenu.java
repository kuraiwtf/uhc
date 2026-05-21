package dev.kurai.uhc.menu.rules;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static dev.kurai.uhc.game.configuration.game.GameConfiguration.*;
import static dev.kurai.uhc.game.configuration.game.GameConfiguration.BOW_HEALTH_VIEW_OPTION;
import static dev.kurai.uhc.game.configuration.ore.OreConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.rules.border.BorderConfigurationMenu;
import dev.kurai.uhc.menu.rules.drop.DropRateMenu;
import dev.kurai.uhc.menu.rules.inventory.StartInventoryMenu;
import dev.kurai.uhc.menu.rules.ore.OreLimitMenu;
import dev.kurai.uhc.menu.rules.timer.TimerDurationMenu;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.ModernBorderTemplate;
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
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RulesMenu extends Menu {

  private final UltraHardcoreAPI ultraHardcore;

  public RulesMenu(final Player player, final UltraHardcoreAPI ultraHardcore) {
    super("Règles de la partie", MenuSize.FIVE, player);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new ModernBorderTemplate(DyeColor.RED.getData(), DyeColor.GRAY.getData()));

    final var gameService = this.ultraHardcore.gameService();
    final var cycleService = gameService.cycleService();
    if (cycleService.enabled()) {
      front.set(
          4,
          new ItemButton(
              new ItemBuilder(Material.WATCH)
                  .name("&6&lDurée d'un cycle")
                  .lore(
                      "",
                      "&6 " + SQUARE + "&f Temps:&b %d",
                      "",
                      "&7" + BAR + "&f Permet de modifier la",
                      "  durée d'un&e cycle complet",
                      "  au cours de la partie.",
                      "")
                  .amount(cycleService.totalCycleDuration() / 60)
                  .asItemStack()));
    } else {
      front.set(
          4,
          new ItemButton(
              new ItemBuilder(Material.WATCH)
                  .name("&6&lCycle")
                  .lore(
                      "",
                      "&7" + BAR + "&f Il est impossible de",
                      "&d  configurer&f la durée",
                      "&f  du&e cycle&f de la partie",
                      "&f  car il est&c désactivé&f.",
                      "")
                  .amount(0)
                  .asItemStack()));
    }

    front.set(11, new BowHealthViewButton());
    front.set(12, new SpectatorButton());
    front.set(14, new BorderTimerButton(this.ultraHardcore));
    front.set(15, new BorderButton());

    front.set(18, new DropRateButton(gameService.dropRateService()));
    front.set(19, new StartInventoryButton(this.ultraHardcore));
    front.set(26, new InvincibilityTimerButton(this.ultraHardcore));

    front.set(29, new OreLimitButton());
    front.set(
        30,
        new ItemButton(
            new ItemBuilder(Material.ENCHANTED_BOOK)
                .name("&6&lLimites des enchantements")
                .lore(
                    "",
                    "&7" + BAR + "&f Permet de modifier",
                    "  les limites des&d enchantements",
                    "  de la partie.",
                    "")
                .asItemStack()));
    front.set(32, new PvPTimerButton(this.ultraHardcore));
    front.set(
        33,
        new ItemButton(
            new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .name("&6&lTemps de Déconnexion")
                .lore(
                    "",
                    "&6 " + SQUARE + "&f Temps:&b 05:00",
                    "",
                    "&7" + BAR + "&f Permet de modifier",
                    "  la limite du temps de",
                    "  &cdéconnexion&f de la partie.",
                    "")
                .amount(5)
                .asItemStack()));
  }

  private static final class BowHealthViewButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BOW)
          .name("&c&lVie en touchant une flèche")
          .lore(
              "",
              "&c "
                  + SQUARE
                  + "&f Statut: "
                  + (BOW_HEALTH_VIEW_OPTION.getValue() ? "&a&lOui" : "&c&lNon"),
              "")
          .amount(BOW_HEALTH_VIEW_OPTION.getValue() ? 1 : 0)
          .glowing(BOW_HEALTH_VIEW_OPTION.getValue())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      BOW_HEALTH_VIEW_OPTION.setValue(!BOW_HEALTH_VIEW_OPTION.getValue());
      click.getMenu().update();
    }
  }

  private static final class SpectatorButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.EYE_OF_ENDER)
          .name("&6&lSpectateurs")
          .lore(
              "",
              "&6 "
                  + SQUARE
                  + "&f Statut: "
                  + (SPECTATOR_OPTION.getValue() ? "&a&lOui" : "&c&lNon"),
              "")
          .amount(SPECTATOR_OPTION.getValue() ? 1 : 0)
          .glowing(SPECTATOR_OPTION.getValue())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      SPECTATOR_OPTION.setValue(!SPECTATOR_OPTION.getValue());
      click.getMenu().update();
    }
  }

  private static final class DropRateButton extends Button {

    private final DropRateService dropRateService;

    private DropRateButton(final DropRateService dropRateService) {
      this.dropRateService = dropRateService;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.APPLE)
          .name("&a&lTaux de drop")
          .lore(
              "",
              "&7" + BAR + "&f Permet de modifier les",
              "  &ataux de drop&f de certains",
              "  objets de la partie.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var dropRateMenu = new DropRateMenu(menu.getPlayer(), this.dropRateService);
      dropRateMenu.setPreviousMenu(menu);
      dropRateMenu.open();
    }
  }

  private static final class BorderButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.STAINED_GLASS)
          .data(DyeColor.CYAN.getData())
          .name("&3&lBordure")
          .lore(
              "",
              "&3 " + SQUARE + "&f Taille initiale: &a" + INITIAL_SIZE_OPTION.getValue(),
              "&3 " + SQUARE + "&f Taille finale: &c" + FINAL_SIZE_OPTION.getValue(),
              "",
              "&3 " + SQUARE + "&f Vitesse: &b" + "%.1f".formatted(SHRINK_SPEED_OPTION.getValue()),
              "&3 " + SQUARE + "&f Type: &c" + BORDER_TYPE_OPTION.getValue().getName(),
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var borderMenu = new BorderConfigurationMenu(menu.getPlayer());
      borderMenu.setPreviousMenu(menu);
      borderMenu.open();
    }
  }

  private static final class PvPTimerButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private PvPTimerButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var pvpTimerOpt = timerService.getTimer("pvp");

      if (pvpTimerOpt.isEmpty()) {
        return new ItemBuilder(Material.BARRIER).name("&cTimer PvP non trouvé").asItemStack();
      }

      final var timer = pvpTimerOpt.get();
      final var timeLeft = timer.getTimeLeft() * 1000L;

      return new ItemBuilder(Material.DIAMOND_SWORD)
          .name("&6&lActivation du PvP")
          .lore(
              "",
              "&6 " + SQUARE + "&f Temps: &b" + TimeUtil.formatDuration(timeLeft),
              "",
              "&7" + BAR + "&f Permet de modifier le",
              "  temps avant l'activation",
              "  du &cPvP&f dans la partie.",
              "")
          .amount(Math.min(Math.max(1, (int) (timeLeft / 60000)), 64))
          .addFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var timerOpt = timerService.getTimer("pvp");

      if (timerOpt.isEmpty()) {
        return;
      }

      final var configMenu = new TimerDurationMenu(menu.getPlayer(), timerOpt.get());
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }

  private static final class BorderTimerButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private BorderTimerButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var borderTimerOpt = timerService.getTimer("border");

      if (borderTimerOpt.isEmpty()) {
        return new ItemBuilder(Material.BARRIER).name("&cTimer Bordure non trouvé").asItemStack();
      }

      final var timer = borderTimerOpt.get();
      final var timeLeft = timer.getTimeLeft() * 1000L;

      return new ItemBuilder(Material.IRON_FENCE)
          .name("&6&lRéduction de la bordure")
          .lore(
              "",
              "&6 " + SQUARE + "&f Temps: &b" + TimeUtil.formatDuration(timeLeft),
              "",
              "&7" + BAR + "&f Permet de modifier le temps",
              "  avant la &créduction&f de la",
              "  &cbordure&f dans la partie.",
              "")
          .amount(Math.min(Math.max(1, (int) (timeLeft / 60000)), 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var timerOpt = timerService.getTimer("border");

      if (timerOpt.isEmpty()) {
        return;
      }

      final var configMenu = new TimerDurationMenu(menu.getPlayer(), timerOpt.get());
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }

  private static final class InvincibilityTimerButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private InvincibilityTimerButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var invincibilityTimerOpt = timerService.getTimer("invincibility");

      if (invincibilityTimerOpt.isEmpty()) {
        return new ItemBuilder(Material.BARRIER)
            .name("&cTimer Invincibilité non trouvé")
            .asItemStack();
      }

      final var timer = invincibilityTimerOpt.get();
      final var timeLeft = timer.getTimeLeft() * 1000L;

      return new ItemBuilder(Material.GOLDEN_APPLE)
          .name("&a&lInvincibilité")
          .lore(
              "",
              "&a " + SQUARE + "&f Temps: &b" + TimeUtil.formatDuration(timeLeft),
              "",
              "&7" + BAR + "&f Permet de modifier le temps",
              "  d'&ainvincibilité&f au début",
              "  de la partie.",
              "")
          .amount(Math.min(Math.max(1, (int) (timeLeft / 1000)), 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var timerService = this.ultraHardcore.gameService().timerService();
      final var timerOpt = timerService.getTimer("invincibility");

      if (timerOpt.isEmpty()) {
        return;
      }

      final var configMenu = new TimerDurationMenu(menu.getPlayer(), timerOpt.get());
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }

  private static final class StartInventoryButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private StartInventoryButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.CHEST)
          .name("&6&lInventaire de départ")
          .lore(
              "",
              "&7" + BAR + "&f Permet de modifier l'&ainventaire",
              "  de&a départ&f de la partie.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var inventoryMenu = new StartInventoryMenu(menu.getPlayer(), this.ultraHardcore);
      inventoryMenu.setPreviousMenu(menu);
      inventoryMenu.open();
    }
  }

  private static final class OreLimitButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var ironLimit = IRON_LIMIT_OPTION.getValue();
      final var goldLimit = GOLD_LIMIT_OPTION.getValue();
      final var diamondLimit = DIAMOND_LIMIT_OPTION.getValue();

      return new ItemBuilder(Material.DIAMOND_ORE)
          .name("&6&lLimites des minerais")
          .lore(
              "",
              "&7 " + SQUARE + "&f Fer: " + (ironLimit == 0 ? "&cAucune" : "&7" + ironLimit),
              "&e " + SQUARE + "&f Or: " + (goldLimit == 0 ? "&cAucune" : "&e" + goldLimit),
              "&b "
                  + SQUARE
                  + "&f Diamant: "
                  + (diamondLimit == 0 ? "&cAucune" : "&b" + diamondLimit),
              "",
              "&7" + BAR + "&f Permet de modifier les",
              "  limites des&a minerais&f de la partie.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var oreLimitMenu = new OreLimitMenu(menu.getPlayer());
      oreLimitMenu.setPreviousMenu(menu);
      oreLimitMenu.open();
    }
  }
}
