package dev.kurai.uhc.profile;

import dev.kurai.uhc.actionbar.Actionbar;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.holder.PowerHolder;
import dev.kurai.uhc.profile.state.ProfileState;
import dev.kurai.uhc.util.api.name.Nameable;
import dev.kurai.uhc.util.api.state.Stateful;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Profile
    extends Nameable<@NotNull String>,
        Stateful<ProfileState>,
        Entity<@NotNull UUID>,
        PowerHolder,
        ForwardingAudience.Single {

  @Override
  @NotNull
  UUID getId();

  @Override
  @NotNull
  String getName();

  @NotNull
  Actionbar getActionbar();

  @Override
  ProfileState getState();

  @Override
  void setState(@NotNull final ProfileState state);

  @Override
  <E extends Entity<@NotNull UUID>> E addComponent(final @NotNull Component component);

  @Override
  boolean removeComponent(final @NotNull Class<? extends Component> componentClass);

  @Override
  boolean hasComponent(final @NotNull Class<? extends Component> componentClass);

  @Override
  <T extends Component> T getComponent(final @NotNull Class<T> componentClass);

  @Override
  @NotNull
  Collection<@NotNull AbstractPower> getPowers();

  @Override
  <T extends AbstractPower> T getPower(final Class<T> clazz);

  @Override
  void registerPower(final @NotNull AbstractPower power);

  @Override
  void unregisterPower(final @NotNull String id);

  default @NotNull Optional<Player> findPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getId()));
  }

  default @Nullable Player getPlayer() {
    return this.findPlayer().orElse(null);
  }
}
