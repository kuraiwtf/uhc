package dev.kurai.uhc.profile;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.kurai.actionbar.Actionbar;
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
import java.util.function.Consumer;
import net.kyori.adventure.audience.ForwardingAudience;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Profile
    extends Nameable<String>,
        Stateful<ProfileState>,
        Entity<UUID>,
        PowerHolder,
        ForwardingAudience.Single {

  int UNKNOWN_IMMUNITY_TICKS = -Integer.MAX_VALUE;
  int INFINITE_IMMUNITY_TICKS = -1;
  int IMMUNITY_UNTIL_NEXT_DAMAGE_TICKS = -2;

  @Override
  UUID getId();

  @Override
  String getName();

  Actionbar getActionbar();

  void executeAction(Consumer<Player> action);

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

  void addPotionEffect(final PotionEffect effect);

  void removePotionEffect(final PotionEffectType type);

  boolean hasPotionEffect(final PotionEffectType type);

  @Nullable PotionEffect getPotionEffect(final PotionEffectType type);

  Optional<PotionEffect> findPotionEffect(final PotionEffectType type);

  void addHealth(final double health);

  void removeHealth(final double health);

  void setHealth(final double health);

  double getHealth();

  void addMaxHealth(final double maxHealth);

  void removeMaxHealth(final double maxHealth);

  void setMaxHealth(final double maxHealth);

  double getMaxHealth();

  default void addDamageImmunity(final EntityDamageEvent.DamageCause cause) {
    this.addDamageImmunity(cause, INFINITE_IMMUNITY_TICKS);
  }

  default void addDamageImmunityUntilNext(final EntityDamageEvent.DamageCause cause) {
    this.addDamageImmunity(cause, IMMUNITY_UNTIL_NEXT_DAMAGE_TICKS);
  }

  void addDamageImmunity(final EntityDamageEvent.DamageCause cause, final int ticks);

  void removeDamageImmunity(final EntityDamageEvent.DamageCause cause);

  boolean hasDamageImmunity(final EntityDamageEvent.DamageCause cause);

  int getDamageImmunityTicks(final EntityDamageEvent.DamageCause cause);

  void sendMessage(final String message);

  void sendPrefixedMessage(final String message);

  void sendPrefixedMessage(final String message, final String prefix);

  @Override
  Collection<Component> getComponents();

  @Override
  <E extends Entity<UUID>> E addComponent(final Component component);

  @Override
  boolean removeComponent(final Class<? extends Component> componentClass);

  @Override
  boolean hasComponent(final Class<? extends Component> componentClass);

  @Override
  <T extends Component> @Nullable T getComponent(final Class<T> componentClass);

  @Override
  Collection<AbstractPower> getPowers();

  @Override
  <T extends AbstractPower> @Nullable T getPower(final Class<T> clazz);

  @Override
  void registerPower(final AbstractPower power);

  void registerPower(AbstractPower power, boolean roleAttribution);

  @Override
  void unregisterPower(final String id);

  void sendPacket(final PacketWrapper<?> wrapper);

  void sendPacket(final Packet<?> packet);

  default Optional<Player> findPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getId()));
  }

  default @Nullable Player getPlayer() {
    return this.findPlayer().orElse(null);
  }

  default OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(this.getId());
  }
}
