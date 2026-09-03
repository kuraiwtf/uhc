package dev.kurai.uhc.menu.scenario.button;

import static dev.kurai.uhc.util.CC.BAR_2;
import static dev.kurai.uhc.util.CC.SQUARE;

import com.google.common.collect.Lists;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public final class ScenarioButton extends Button {

  private final @Nullable EventService eventService;
  private final AbstractScenario scenario;
  private final boolean editable;

  public ScenarioButton(final AbstractScenario scenario) {
    this(null, scenario, false);
  }

  public ScenarioButton(
      final @Nullable EventService eventService,
      final AbstractScenario scenario,
      final boolean editable) {
    this.eventService = eventService;
    this.scenario = scenario;
    this.editable = editable;
  }

  @Override
  public ItemStack getIcon() {
    final ScenarioCategory category = this.scenario.getCategory();
    final var enabled = this.scenario.isEnabled();
    final var lore = Lists.<String>newArrayList();
    lore.add("");
    lore.add("§a" + BAR_2 + "&f &lInformations");
    lore.add(
        "§a " + SQUARE + "§f Catégorie: " + category.color().asBukkitColor() + category.name());
    lore.add("§a " + SQUARE + "§f Statut: " + (enabled ? "§aActivé" : "§cDésactivé"));
    lore.add("");
    lore.add("§a" + BAR_2 + "&f &lDescription");
    for (final String line : this.scenario.provideLore()) {
      lore.add("§8 " + SQUARE + "§r " + line);
    }
    lore.add("");

    if (this.editable) {
      lore.add("§a" + BAR_2 + "§f §lActions");
      lore.add(
          "§a "
              + SQUARE
              + "§f Clic: "
              + (enabled ? "§cDésactiver" : "§aActiver")
              + "§f le scénario.");
      lore.add("");
    }

    return new ItemBuilder(this.scenario.provideIcon())
        .name("&a&l" + this.scenario.getName())
        .lore(lore)
        .glowing(enabled)
        .amount(enabled ? 1 : 0)
        .asItemStack();
  }

  @Override
  public void onClick(final ButtonClick click) {
    if (!this.editable || this.eventService == null) {
      return;
    }

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
