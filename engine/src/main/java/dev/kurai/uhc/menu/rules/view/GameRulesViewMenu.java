package dev.kurai.uhc.menu.rules.view;

import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.game.rule.GameRule;
import dev.kurai.uhc.game.rule.GameRuleService;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GameRulesViewMenu extends PaginatedMenu {

  private final GameRuleService ruleService;

  public GameRulesViewMenu(final Player player, final GameRuleService ruleService) {
    super("Règles du jeu", MenuSize.FIVE, player);
    this.ruleService = ruleService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.RED.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  @Override
  public List<Button> getEntries() {
    return this.ruleService.gameRules().stream().map(GameRulesViewMenu::button).toList();
  }

  private static Button button(final GameRule rule) {
    final ChatColor color = rule.color();
    return new ItemButton(
        new ItemBuilder(rule.icon())
            .name(color + "&l" + rule.getName())
            .lore(
                "",
                color + " " + SQUARE + "&r Statut: " + (rule.state() ? "&aOui" : "&cNon"),
                "")
            .glowing(rule.state())
            .amount(rule.state() ? 1 : 0)
            .asItemStack());
  }
}
