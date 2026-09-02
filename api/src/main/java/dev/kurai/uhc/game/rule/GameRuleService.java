package dev.kurai.uhc.game.rule;

import java.util.Collection;
import java.util.Optional;

public interface GameRuleService {

  Collection<GameRule> gameRules();

  Collection<GameRule> enabledGameRules();

  Optional<GameRule> findRule(final String identifier);

  void addRule(final GameRule rule);

  void removeRule(final String identifier);

  boolean isRuleEnabled(final String identifier);
}
