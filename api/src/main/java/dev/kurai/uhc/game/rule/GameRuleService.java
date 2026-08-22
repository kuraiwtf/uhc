package dev.kurai.uhc.game.rule;

import java.util.Collection;

public interface GameRuleService {

  Collection<GameRule> gameRules();

  Collection<GameRule> enabledGameRules();

  void addRule(final GameRule rule);

  void removeRule(final String identifier);

  boolean isRuleEnabled(final String identifier);
}
