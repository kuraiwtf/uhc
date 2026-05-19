package dev.kurai.uhc.menu.scenario;

import static dev.kurai.uhc.util.CC.BAR;
import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Comparator;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ScenarioConfigurationMenu extends PaginatedMenu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final EventService eventService;
  private final ScenarioService scenarioService;
  private boolean filterActiveOnly = false;

  public ScenarioConfigurationMenu(
      final @NotNull Player player,
      final @NotNull EventService eventService,
      final @NotNull ScenarioService scenarioService) {
    super("Scénarios", MenuSize.FIVE, player);
    this.eventService = eventService;
    this.scenarioService = scenarioService;
  }

  @Override
  public List<Button> getEntries() {
    final var scenarios =
        this.filterActiveOnly
            ? this.scenarioService.getEnabledScenarios()
            : this.scenarioService.getScenarios();

    return scenarios.stream()
        .sorted(Comparator.comparing(AbstractScenario::getName))
        .map(scenario -> new ScenarioButton(this.eventService, scenario))
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    for (final int i : new int[] {2, 3, 5, 6, 18, 26, 38, 39, 41, 42}) {
      background.set(i, GLASS);
    }

    foreground.center(new PaginationSlot(this));
    foreground.set(4, new FilterButton());

    if (this.filterActiveOnly && this.scenarioService.getEnabledScenarios().isEmpty()) {
      foreground.set(22, new NoActiveScenarioButton());
    }
  }

  private static final class ScenarioButton extends Button {

    private final EventService eventService;
    private final AbstractScenario scenario;

    private ScenarioButton(
        final @NotNull EventService eventService, final @NotNull AbstractScenario scenario) {
      this.eventService = eventService;
      this.scenario = scenario;
    }

    @Override
    public ItemStack getIcon() {
      final var enabled = this.scenario.isEnabled();
      return new ItemBuilder(this.scenario.provideIcon())
          .name("&a&l" + this.scenario.getName())
          .glowing(enabled)
          .amount(enabled ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.scenario.setEnabled(!this.scenario.isEnabled());
      click.getMenu().update();

      if (this.scenario.isEnabled()) {
        this.scenario.onEnable();

        if (this.scenario instanceof final Listener listener) {
          this.eventService.registerListener(listener);
        }
      } else {
        this.scenario.onDisable();

        if (this.scenario instanceof final Listener listener) {
          this.eventService.unregisterListener(listener);
        }
      }
    }
  }

  private final class FilterButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var filterActive = ScenarioConfigurationMenu.this.filterActiveOnly;
      return new ItemBuilder(Material.HOPPER)
          .name("&a&lFiltre")
          .lore(
              "",
              "&a "
                  + SQUARE
                  + "&f Statut: "
                  + (filterActive ? "&a&lActifs uniquement" : "&7&lTous"),
              "",
              "&7" + BAR + "&f Permet de basculer l'affichage",
              "  entre &atous&f les scénarios et",
              "  uniquement les &ascénarios actifs&f.",
              "")
          .glowing(filterActive)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      ScenarioConfigurationMenu.this.filterActiveOnly =
          !ScenarioConfigurationMenu.this.filterActiveOnly;
      click.getMenu().update();
    }
  }

  private static final class NoActiveScenarioButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.INK_SACK)
          .data(DyeColor.RED.getDyeData())
          .name("&c&lAucun scénario actif")
          .lore(
              "",
              "&7" + BAR + "&f Aucun scénario n'est",
              "  actuellement &cactif&f.",
              "",
              "&7" + BAR + "&f Désactivez le filtre pour",
              "  voir tous les scénarios.",
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
