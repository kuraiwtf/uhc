package dev.kurai.uhc.profile;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.actionbar.Actionbar;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.component.ProfileIdentifierComponent;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import dev.kurai.uhc.profile.state.ProfileState;
import dev.kurai.uhc.profile.state.WaitingProfileState;
import java.util.*;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ProfileImpl implements Profile {

  private final Map<@NotNull Class<? extends Component>, @NotNull Component> components;

  private final UltraHardcoreAPI ultraHardcore;
  private final Map<@NotNull String, @NotNull AbstractPower> powers;

  private ProfileState state;

  public ProfileImpl(final @NotNull Player player, final UltraHardcoreAPI ultraHardcore) {
    this(player.getUniqueId(), player.getName(), ultraHardcore);
  }

  public ProfileImpl(
      final @NotNull UUID id,
      final @NotNull String name,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.components = Maps.newHashMap();
    this.addComponents(
        new ProfileIdentifierComponent(id), new NameComponent(name), new ProfileMiningComponent());

    this.setState(new WaitingProfileState());

    this.ultraHardcore = ultraHardcore;
    this.powers = Maps.newHashMap();
  }

  @Override
  public @NotNull Audience audience() {
    return this.ultraHardcore.getBukkitAudiences().player(this.getId());
  }

  @Override
  public @NotNull UUID getId() {
    return this.getComponent(ProfileIdentifierComponent.class).getIdentifier();
  }

  @Override
  public @NotNull String getName() {
    return this.getComponent(NameComponent.class).getName();
  }

  public void setName(final @NotNull String name) {
    this.getComponent(NameComponent.class).setName(name);
  }

  @Override
  public @NotNull Actionbar getActionbar() {
    return this.ultraHardcore.getActionbarService().getActionbar(this.getId());
  }

  @Override
  public ProfileState getState() {
    return this.state;
  }

  @Override
  public void setState(@NotNull final ProfileState state) {
    if (this.state != null) {
      this.state.onExit(this);
    }

    this.state = state;
    this.state.onEntry(this);
  }

  @Override
  public <E extends Entity<@NotNull UUID>> E addComponent(final @NotNull Component component) {
    this.components.put(component.getClass(), component);
    return (E) this;
  }

  @Override
  public boolean removeComponent(final @NotNull Class<? extends Component> componentClass) {
    return this.components.remove(componentClass) != null;
  }

  @Override
  public boolean hasComponent(final @NotNull Class<? extends Component> componentClass) {
    return this.components.containsKey(componentClass);
  }

  @Override
  public <T extends Component> T getComponent(final @NotNull Class<T> componentClass) {
    return Optional.ofNullable(this.components.get(componentClass))
        .map(componentClass::cast)
        .orElse(null);
  }

  @Override
  public @NotNull Collection<AbstractPower> getPowers() {
    return List.copyOf(this.powers.values());
  }

  @Override
  public void registerPower(final @NotNull AbstractPower power) {
    this.powers.put(power.getId(), power);
  }

  @Override
  public void unregisterPower(final @NotNull String id) {
    this.powers.remove(id);
  }
}
