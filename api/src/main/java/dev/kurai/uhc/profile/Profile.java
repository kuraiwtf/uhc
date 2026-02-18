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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Profile
    extends Nameable<String>,
        Stateful<ProfileState>,
        Entity<UUID>,
        PowerHolder,
        ForwardingAudience.Single {

  @Override
  UUID getId();

  @Override
  String getName();

  Actionbar getActionbar();

  void addItem(final ItemStack item);

  default void addItems(final ItemStack... items) {
    for (final var item : items) {
      this.addItem(item);
    }
  }

  @Override
  ProfileState getState();

  @Override
  void setState(final ProfileState state);

  @Override
  Collection<Component> getComponents();

  @Override
  <E extends Entity<UUID>> E addComponent(final Component component);

  @Override
  boolean removeComponent(final Class<? extends Component> componentClass);

  @Override
  boolean hasComponent(final Class<? extends Component> componentClass);

  @Override
  <T extends Component> T getComponent(final Class<T> componentClass);

  @Override
  Collection<AbstractPower> getPowers();

  @Override
  <T extends AbstractPower> T getPower(final Class<T> clazz);

  @Override
  void registerPower(final AbstractPower power);

  @Override
  void unregisterPower(final String id);

  default Optional<Player> findPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getId()));
  }

  default @Nullable Player getPlayer() {
    return this.findPlayer().orElse(null);
  }
}
