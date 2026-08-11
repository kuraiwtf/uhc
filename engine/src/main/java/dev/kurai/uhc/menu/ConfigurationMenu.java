package dev.kurai.uhc.menu;

import static dev.kurai.uhc.game.GameService.WHITELIST_OPTION;
import static dev.kurai.uhc.util.CC.BAR;
import static dev.kurai.uhc.util.CC.SQUARE;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.game.slot.SlotService;
import dev.kurai.uhc.game.slot.impl.MutableSlotProvider;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.menu.rules.RulesMenu;
import dev.kurai.uhc.menu.scenario.ScenarioConfigurationMenu;
import dev.kurai.uhc.menu.slots.SlotsConfigurationMenu;
import dev.kurai.uhc.menu.team.TeamConfigurationMenu;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
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

public final class ConfigurationMenu extends Menu {

  private final UltraHardcoreAPI ultraHardcore;

  public ConfigurationMenu(final Player player, final UltraHardcoreAPI ultraHardcore) {
    super("Configuration de la partie", MenuSize.SIX, player);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));

    front.set(13, new SlotsButton(this.ultraHardcore.gameService().slotService()));

    front.set(
        27,
        new ScenarioButton(
            this.ultraHardcore.eventService(), this.ultraHardcore.gameService().scenarioService()));

    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    if (module instanceof final TeamModule teamModule) {
      front.set(30, new TeamButton(teamModule));
    } else {
      back.set(30, new TeamDisabledButton());
    }

    front.set(31, new SettingsButton(this.ultraHardcore));

    if (module.provideModuleMenu(this.getPlayer()) != null) {
      front.set(32, new ModuleButton(this.getPlayer(), module));
    } else {
      back.set(32, new EmptyModuleButton());
    }

    front.set(43, new AccessibilityButton());

    front.set(49, new StartButton(this.ultraHardcore.gameService().startService()));
  }

  @RequiredArgsConstructor
  private static final class SlotsButton extends Button {

    private final SlotService slotService;

    @Override
    public ItemStack getIcon() {
      final var lines = Lists.<String>newArrayList();
      final var slotProvider = this.slotService.slotProvider();
      lines.add("");
      lines.add("&a " + CC.SQUARE + "&f Slots: &a" + slotProvider.slots());
      lines.add("");

      if (slotProvider instanceof MutableSlotProvider) {
        lines.add("&7" + BAR + "&f Permet de modifier les");
        lines.add("&a  slots&f de la partie.");
      } else {
        lines.add("&7" + BAR + "&f Les&a slots&f de la");
        lines.add("&f  partie sont définis de");
        lines.add("&f  manière&d automatique&f.");
      }

      lines.add("");
      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .name("&a&lSlots")
          .lore(lines)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      if (!(this.slotService.slotProvider() instanceof final MutableSlotProvider slotProvider)) {
        return;
      }

      final var slotsConfigurationMenu =
          new SlotsConfigurationMenu(click.getMenu().getPlayer(), slotProvider);
      slotsConfigurationMenu.setPreviousMenu(click.getMenu());
      slotsConfigurationMenu.open();
    }
  }

  private static final class ScenarioButton extends Button {

    private final EventService eventService;
    private final ScenarioService scenarioService;

    private ScenarioButton(final EventService eventService, final ScenarioService scenarioService) {
      this.eventService = eventService;
      this.scenarioService = scenarioService;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BOOK_AND_QUILL)
          .name("&a&lScénarios")
          .lore(
              "",
              "&7" + BAR + "&r Permet de configurer",
              "  les&a scénarios&r de la",
              "  &dpartie&r.",
              "")
          .glowing(true)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var scenarioMenu =
          new ScenarioConfigurationMenu(
              click.getMenu().getPlayer(), this.eventService, this.scenarioService);
      scenarioMenu.setPreviousMenu(click.getMenu());
      scenarioMenu.open();
    }
  }

  @RequiredArgsConstructor
  private static final class TeamButton extends Button {

    private final TeamModule module;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BANNER)
          .data(DyeColor.RED.getDyeData())
          .name("&a&lÉquipes")
          .lore(
              "",
              "&7" + BAR + "&f Permet d'accéder",
              "&f  aux&c paramètres",
              "&f  des équipes.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var teamMenu = new TeamConfigurationMenu(click.getMenu().getPlayer(), this.module);
      teamMenu.setPreviousMenu(click.getMenu());
      teamMenu.open();
    }
  }

  private static final class TeamDisabledButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.INK_SACK)
          .data(DyeColor.RED.getDyeData())
          .name("&c&lÉquipes indisponibles")
          .lore(
              "",
              "&7" + BAR + "&f Vous ne pouvez&c pas",
              "&f  configurer les &céquipes",
              "&f  dans ce &cjeu.",
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }

  private static final class SettingsButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private SettingsButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.REDSTONE_COMPARATOR)
          .name("&c&lParamètres")
          .lore("", "&7" + BAR + "&f Permet d'accéder aux&c paramètres", "&f  de la partie.", "")
          .glowing(true)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var settingsMenu = new RulesMenu(click.getMenu().getPlayer(), this.ultraHardcore);
      settingsMenu.setPreviousMenu(click.getMenu());
      settingsMenu.open();
    }
  }

  private static final class ModuleButton extends Button {

    private final Player player;
    private final AbstractModule module;

    private ModuleButton(final Player player, final AbstractModule module) {
      this.player = player;
      this.module = module;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.module.provideModuleIcon(this.player))
          .name("&6&l" + this.module.getName())
          .lore(
              "",
              "&7" + BAR + "&f Cliquez pour &6configurer",
              "&f  le&6 module&f de la partie.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = this.module.provideModuleMenu(this.player);
      if (menu == null) {
        return;
      }

      menu.setPreviousMenu(click.getMenu());
      menu.apply(new BackTemplate(click.getMenu()));
      menu.open();
    }
  }

  private static final class EmptyModuleButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.INK_SACK)
          .data(DyeColor.RED.getDyeData())
          .name("&c&lConfiguration indisponible")
          .lore("", "&7" + BAR + "&f Vous ne pouvez&c pas", "&f  configurer ce jeu.", "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }

  private static final class AccessibilityButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.MINECART)
          .name("&6&lAccès à la partie")
          .lore(
              "",
              "&6 "
                  + SQUARE
                  + "&r Accès: "
                  + (WHITELIST_OPTION.getValue() ? "&cFermé" : "&aPublic"),
              "",
              "&7" + BAR + "&r Permet de configurer",
              "  l'accès à la&d partie&r.",
              "")
          .glowing(!WHITELIST_OPTION.getValue())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      WHITELIST_OPTION.setValue(!WHITELIST_OPTION.getValue());
      click.getMenu().update();
    }
  }

  private static final class StartButton extends Button {

    private final StartService startService;

    private StartButton(final StartService startService) {
      this.startService = startService;
    }

    @Override
    public ItemStack getIcon() {
      final var starting = this.startService.isStarting();
      return new ItemBuilder(Material.INK_SACK)
          .data(starting ? DyeColor.RED.getDyeData() : DyeColor.LIME.getDyeData())
          .name((starting ? "&c&lAnnuler" : "&a&lDémarrer") + " la partie")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      if (this.startService.isStarting()) {
        this.startService.cancelStart();
        menu.update();
        return;
      }

      this.startService.handleStart();
      menu.close();
    }
  }
}
