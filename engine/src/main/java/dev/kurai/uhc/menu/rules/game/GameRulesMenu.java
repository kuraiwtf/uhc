package dev.kurai.uhc.menu.rules.game;

import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.game.rule.GameRule;
import dev.kurai.uhc.game.rule.GameRuleService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GameRulesMenu extends PaginatedMenu {

  private final GameRuleService ruleService;

  public GameRulesMenu(final Player player, final GameRuleService ruleService) {
    super("Règles du jeu", MenuSize.FIVE, player);
    this.ruleService = ruleService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.RED.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));

    foreground.center(new PaginationSlot(this));
  }

  @Override
  public List<Button> getEntries() {
    return this.ruleService.gameRules().stream()
        .map(RuleButton::new)
        .map(Button.class::cast)
        .toList();
  }

  private static final class RuleButton extends Button {

    private final GameRule rule;

    private RuleButton(final GameRule rule) {
      this.rule = rule;
    }

    @Override
    public ItemStack getIcon() {
      final ChatColor color = this.rule.color();
      return new ItemBuilder(this.rule.icon())
          .name(color + "&l" + this.rule.getName())
          .lore(
              "",
              color + " " + SQUARE + "&r Statut: " + (this.rule.state() ? "&aOui" : "&cNon"),
              "")
          .glowing(this.rule.state())
          .amount(this.rule.state() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.rule.state(!this.rule.state());
      click.getMenu().update();
    }
  }
}
