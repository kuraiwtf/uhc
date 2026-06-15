package dev.kurai.uhc.menu.scenario;

import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Comparator;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ScenarioViewMenu extends PaginatedMenu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final ScenarioService scenarioService;

  public ScenarioViewMenu(final Player player, final ScenarioService scenarioService) {
    super("Scénarios", MenuSize.FIVE, player);
    this.scenarioService = scenarioService;
  }

  @Override
  public List<Button> getEntries() {
    return this.scenarioService.getEnabledScenarios().stream()
        .sorted(Comparator.comparing(AbstractScenario::getName))
        .map(ScenarioButton::new)
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

    if (this.getEntries().isEmpty()) {
      foreground.set(22, new EmptyButton());
    }
  }

  private static final class EmptyButton extends Button {
    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.INK_SACK)
          .data(DyeColor.RED.getDyeData())
          .name("&c&lAucun scénario activé")
          .lore("", "&7" + CC.BAR + "&f Aucun scénario n'est actif.", "")
          .lunarTag("unclickable", true)
          .asItemStack();
    }
  }

  private static final class ScenarioButton extends Button {

    private final AbstractScenario scenario;

    private ScenarioButton(final  AbstractScenario scenario) {
      this.scenario = scenario;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.scenario.provideIcon())
          .name("&a&l" + this.scenario.getName())
          .glowing(this.scenario.isEnabled())
          .lunarTag("unclickable", true)
          .asItemStack();
    }
  }
}
