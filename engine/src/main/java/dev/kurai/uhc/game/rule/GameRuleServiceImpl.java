package dev.kurai.uhc.game.rule;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GameRuleServiceImpl implements GameRuleService {

  private final Map<String, GameRule> rules;
  private final Collection<GameRule> rulesView;

  public GameRuleServiceImpl() {
    this.rules = Maps.newHashMap();
    this.rulesView = Collections.unmodifiableCollection(this.rules.values());

    this.addRule(
        new GameRule(
            "obsidian_trap",
            "Obsi Trap",
            ChatColor.LIGHT_PURPLE,
            new ItemStack(Material.OBSIDIAN)));
    this.addRule(new GameRule("lava", "Lave", ChatColor.GOLD, new ItemStack(Material.LAVA_BUCKET)));
  }

  @Override
  public Collection<GameRule> gameRules() {
    return this.rulesView;
  }

  @Override
  public Collection<GameRule> enabledGameRules() {
    return this.rulesView.stream().filter(GameRule::state).toList();
  }

  @Override
  public Optional<GameRule> findRule(final String identifier) {
    return Optional.ofNullable(this.rules.get(identifier));
  }

  @Override
  public void addRule(final GameRule rule) {
    this.rules.put(rule.getId(), rule);
  }

  @Override
  public void removeRule(final String identifier) {
    this.rules.remove(identifier);
  }

  @Override
  public boolean isRuleEnabled(final String identifier) {
    return this.rules.containsKey(identifier) && this.rules.get(identifier).state();
  }
}
