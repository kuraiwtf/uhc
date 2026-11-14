package dev.kurai.uhc.game.combat;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.CombatComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CombatServiceImpl implements CombatService {

  private long combatDuration = 15_000L;

  @Override
  public long combatDuration() {
    return this.combatDuration;
  }

  @Override
  public void combatDuration(final long millis) {
    this.combatDuration = millis;
  }

  @Override
  public void enterCombat(final Profile profile) {
    profile.removeComponent(CombatComponent.class);
    profile.addComponent(new CombatComponent(System.currentTimeMillis()));
  }

  @Override
  public void leaveCombat(final Profile profile) {
    profile.removeComponent(CombatComponent.class);
  }

  @Override
  public boolean inCombat(final Profile profile) {
    final var component = profile.getComponent(CombatComponent.class);
    return component != null && this.isValid(component);
  }

  @Override
  public void validate(final Profile profile) {
    final var component = profile.getComponent(CombatComponent.class);
    if (component != null && !this.isValid(component)) {
      this.leaveCombat(profile);
    }
  }

  private boolean isValid(final CombatComponent component) {
    return System.currentTimeMillis() - component.timestamp() < this.combatDuration;
  }
}
