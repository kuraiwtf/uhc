package dev.kurai.uhc.menu.rules.view;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static dev.kurai.uhc.game.configuration.ore.OreConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.game.disconnect.DisconnectService;
import dev.kurai.uhc.game.enchantment.EnchantmentLimitType;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.rules.enchantment.EnchantmentLimitListMenu;
import dev.kurai.uhc.menu.rules.enchantment.EnchantmentLimitViewMenu;
import dev.kurai.uhc.menu.rules.inventory.StartInventoryViewMenu;
import dev.kurai.uhc.menu.scenario.ScenarioViewMenu;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.ModernBorderTemplate;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.util.ItemBuilder;
import dev.kurai.uhc.util.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RulesViewMenu extends Menu {

  private static final int MAX_ENTRIES = 5;

  private final UltraHardcoreAPI ultraHardcore;

  public RulesViewMenu(final Player player, final UltraHardcoreAPI ultraHardcore) {
    super("Règles de la partie", MenuSize.FIVE, player);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new ModernBorderTemplate(DyeColor.RED.getData(), DyeColor.GRAY.getData()));

    final var gameService = this.ultraHardcore.gameService();

    front.set(4, new ItemButton(this.timersIcon()));

    front.set(
        11,
        new OpenButton(
            new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .name("&c&lRègles de la partie")
                .lore(
                    detailLore(
                        "&fDésactivées:",
                        gameService.ruleService().gameRules().stream()
                            .filter(rule -> !rule.state())
                            .map(rule -> "&c " + SQUARE + "&f " + rule.getName())
                            .toList(),
                        "&a " + SQUARE + "&f Toutes activées",
                        "&7" + BAR + "&r Clic pour voir",
                        "  les&c règles&r de la&d partie&r."))
                .glowing(true)
                .asItemStack(),
            player -> new GameRulesViewMenu(player, gameService.ruleService())));
    front.set(
        12,
        new OpenButton(
            new ItemBuilder(Material.WORKBENCH)
                .name("&6&lRecettes personnalisées")
                .lore(
                    detailLore(
                        "&fFabriquables:",
                        gameService.recipeService().recipes().stream()
                            .filter(recipe -> recipe.isEnabled())
                            .map(recipe -> "&6 " + SQUARE + "&f " + recipe.getName())
                            .toList(),
                        "&c " + SQUARE + "&f Aucune",
                        "&7" + BAR + "&r Clic pour voir",
                        "  les&6 recettes&r de la&d partie&r."))
                .asItemStack(),
            player -> new RecipesViewMenu(player, gameService.recipeService())));

    final var enabledScenarios =
        gameService.scenarioService().getEnabledScenarios().stream()
            .map(AbstractScenario::getName)
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();
    front.set(
        14,
        new OpenButton(
            new ItemBuilder(Material.NETHER_STAR)
                .name("&e&lScénarios")
                .lore(
                    detailLore(
                        "&fActivés:",
                        enabledScenarios.stream()
                            .map(name -> "&e " + SQUARE + "&f " + name)
                            .toList(),
                        "&c " + SQUARE + "&f Aucun",
                        "&7" + BAR + "&f Clic pour voir les",
                        "  &escénarios&f activés."))
                .amount(Math.clamp(enabledScenarios.size(), 1, 64))
                .glowing(!enabledScenarios.isEmpty())
                .asItemStack(),
            player -> new ScenarioViewMenu(player, gameService.scenarioService())));
    front.set(15, new ItemButton(this.borderIcon()));

    front.set(
        20,
        new OpenButton(
            new ItemBuilder(Material.APPLE)
                .name("&a&lTaux de drop")
                .lore(
                    detailLore(
                        "&fTaux:",
                        gameService.dropRateService().getModifiers().stream()
                            .map(
                                modifier ->
                                    "&a "
                                        + SQUARE
                                        + "&f "
                                        + modifier.getName()
                                        + ": &a"
                                        + modifier.getDropRate()
                                        + "%")
                            .toList(),
                        "&7 " + SQUARE + "&f Aucun",
                        "&7" + BAR + "&f Clic pour voir les",
                        "  &ataux de drop&f de certains",
                        "  objets de la partie."))
                .asItemStack(),
            player -> new DropRatesViewMenu(player, gameService.dropRateService())));
    front.set(
        21,
        new OpenButton(
            new ItemBuilder(Material.CHEST)
                .name("&6&lInventaire de départ")
                .lore(
                    detailLore(
                        "&fContenu:",
                        this.startInventoryEntries(),
                        "&c " + SQUARE + "&f Vide",
                        "&7" + BAR + "&f Clic pour voir l'&ainventaire",
                        "  de&a départ&f de la partie."))
                .asItemStack(),
            player -> new StartInventoryViewMenu(player, this.ultraHardcore)));
    front.set(23, new ItemButton(this.oreLimitIcon()));
    front.set(
        24,
        new OpenButton(
            new ItemBuilder(Material.ENCHANTED_BOOK)
                .name("&6&lLimites des enchantements")
                .lore(
                    detailLore(
                        "&fLimites:",
                        this.enchantmentLimitEntries(),
                        "&a " + SQUARE + "&f Aucune (vanilla)",
                        "&7" + BAR + "&f Clic pour voir les limites",
                        "  des&d enchantements&f."))
                .asItemStack(),
            player ->
                new EnchantmentLimitViewMenu(player, gameService.enchantmentLimitService())));

    front.set(31, new ItemButton(this.disconnectIcon(gameService.disconnectService())));
  }

  private static List<String> detailLore(
      final String header,
      final List<String> entries,
      final String emptyLine,
      final String... hint) {
    final var lore = new ArrayList<String>();
    lore.add("");
    lore.add(header);

    if (entries.isEmpty()) {
      lore.add(emptyLine);
    } else if (entries.size() > MAX_ENTRIES) {
      lore.addAll(entries.subList(0, MAX_ENTRIES - 1));
      lore.add("&7   +" + (entries.size() - (MAX_ENTRIES - 1)) + " autres");
    } else {
      lore.addAll(entries);
    }

    lore.add("");
    lore.addAll(Arrays.asList(hint));
    lore.add("");
    return lore;
  }

  private List<String> startInventoryEntries() {
    final var content = InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue();
    final var armor = InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue();

    return Stream.concat(Arrays.stream(content), Arrays.stream(armor))
        .filter(item -> item != null && item.getType() != Material.AIR)
        .map(item -> "&6 " + SQUARE + "&f " + itemName(item))
        .toList();
  }

  private static String itemName(final ItemStack item) {
    final var meta = item.getItemMeta();
    final String name =
        meta != null && meta.hasDisplayName() ? meta.getDisplayName() : prettify(item.getType());
    return item.getAmount() > 1 ? name + " &7x" + item.getAmount() : name;
  }

  private static String prettify(final Material material) {
    return Arrays.stream(material.name().toLowerCase(Locale.ROOT).split("_"))
        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
        .reduce((left, right) -> left + " " + right)
        .orElse(material.name());
  }

  private List<String> enchantmentLimitEntries() {
    final var limitService = this.ultraHardcore.gameService().enchantmentLimitService();
    final var entries = new ArrayList<String>();

    for (final Enchantment enchantment : Enchantment.values()) {
      final String name =
          EnchantmentLimitListMenu.NAMES.getOrDefault(enchantment.getName(), enchantment.getName());

      for (final EnchantmentLimitType type : EnchantmentLimitType.values()) {
        final var limit = limitService.limit(type, enchantment);
        if (limit.isEmpty()) {
          continue;
        }

        final String typeSuffix =
            type == EnchantmentLimitType.GLOBAL
                ? ""
                : " (" + type.alias().substring(0, 1).toUpperCase(Locale.ROOT)
                    + type.alias().substring(1)
                    + ")";
        entries.add(
            type.color()
                + " "
                + SQUARE
                + "&f "
                + name
                + typeSuffix
                + ": "
                + (limit.getAsInt() == 0 ? "&cInterdit" : "&e" + limit.getAsInt()));
      }
    }

    return entries;
  }

  private ItemStack timersIcon() {
    final var gameService = this.ultraHardcore.gameService();
    final var lore = new ArrayList<String>();
    lore.add("");
    lore.add(
        "&6 "
            + SQUARE
            + "&f Épisode: &b"
            + TimeUtil.formatDuration(gameService.episodeService().getTimePerEpisode() * 1000L));

    final var cycleService = gameService.cycleService();
    lore.add(
        "&6 "
            + SQUARE
            + "&f Cycle: "
            + (cycleService.enabled()
                ? "&b" + TimeUtil.formatDuration(cycleService.totalCycleDuration() * 1000L)
                : "&cDésactivé"));

    lore.add("");
    gameService.timerService().getTimers().stream()
        .sorted(Comparator.comparing(AbstractTimer::getName))
        .forEach(
            timer ->
                lore.add(
                    "&6 "
                        + SQUARE
                        + "&f "
                        + timer.getName()
                        + ": &b"
                        + TimeUtil.formatDuration(timer.getTimeLeft() * 1000L)));
    lore.add("");

    return new ItemBuilder(Material.WATCH).name("&6&lTimers").lore(lore).asItemStack();
  }

  private ItemStack borderIcon() {
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

  private ItemStack oreLimitIcon() {
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
            "")
        .asItemStack();
  }

  private ItemStack disconnectIcon(final DisconnectService disconnectService) {
    final int time = disconnectService.disconnectTime();
    return new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
        .name("&6&lDéconnexion")
        .lore(
            "",
            "&6 " + SQUARE + "&f Temps: &b" + TimeUtil.formatDuration(time * 1000L),
            "&6 " + SQUARE + "&f Stratégie: " + strategyLabel(disconnectService.strategy()),
            "")
        .amount(Math.clamp(time / 60, 1, 64))
        .asItemStack();
  }

  private static String strategyLabel(final DisconnectService.Strategy strategy) {
    return switch (strategy) {
      case DISABLED -> "&cDésactivée";
      case ONCE -> "&eUne fois";
      case CUMULATIVE -> "&aCumulative";
    };
  }

  private static final class OpenButton extends Button {

    private final ItemStack icon;
    private final Function<Player, Menu> menuFactory;

    private OpenButton(final ItemStack icon, final Function<Player, Menu> menuFactory) {
      this.icon = icon;
      this.menuFactory = menuFactory;
    }

    @Override
    public ItemStack getIcon() {
      return this.icon.clone();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final Menu menu = click.getMenu();
      final Menu next = this.menuFactory.apply(menu.getPlayer());
      next.setPreviousMenu(menu);
      next.open();
    }
  }
}
