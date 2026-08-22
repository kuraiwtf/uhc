package dev.kurai.uhc.profile;

import static dev.kurai.uhc.util.packet.PacketWrapper.createPacketWrapper;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import com.google.common.collect.Maps;
import dev.kurai.actionbar.Actionbar;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.event.defaults.player.PlayerDamageEvent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.profile.component.*;
import dev.kurai.uhc.profile.state.ProfileState;
import dev.kurai.uhc.profile.state.WaitingProfileState;
import dev.kurai.uhc.util.CC;
import java.util.*;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@Getter
@Setter
public final class ProfileImpl implements Profile {

  private final Map<Class<? extends Component>, Component> components;

  private final UltraHardcoreAPI ultraHardcore;
  private final Map<String, AbstractPower> powers;

  private int kills;
  private int assists;

  public ProfileImpl(final UUID id, final UltraHardcoreAPI ultraHardcore) {
    this.components = Maps.newHashMap();
    this.addComponent(new ProfileIdentifierComponent(id));
    this.addComponents(
        new NameComponent(this.getOfflinePlayer().getName()),
        new ClaimComponent(),
        new DamageImmunityComponent(),
        new ProfileMiningComponent(),
        new OfflineActionComponent(),
        new ProfileStateComponent(new WaitingProfileState()));

    this.ultraHardcore = ultraHardcore;
    this.powers = Maps.newHashMap();
  }

  @Override
  public Audience audience() {
    return this.ultraHardcore.bukkitAudiences().player(this.getId());
  }

  @Override
  public UUID getId() {
    return this.getComponent(ProfileIdentifierComponent.class).getIdentifier();
  }

  @Override
  public String getName() {
    final var component = this.getComponent(NameComponent.class);
    if (component == null) {
      return "";
    }

    return component.getName();
  }

  public void setName(final String name) {
    final var component = this.getComponent(NameComponent.class);
    if (component == null) {
      return;
    }

    component.setName(name);
  }

  @Override
  public Actionbar getActionbar() {
    return this.ultraHardcore.actionbarService().actionbar(this.getId());
  }

  @Override
  public void executeAction(final Consumer<Player> action) {
    this.findPlayer()
        .ifPresentOrElse(
            action,
            () -> {
              final var offlineActionComponent = this.getComponent(OfflineActionComponent.class);
              if (offlineActionComponent == null) {
                return;
              }

              offlineActionComponent.actions().add(action::accept);
            });
  }

  @Override
  public void addItem(final ItemStack item) {
    this.executeAction(player -> this.addItemInternal(player, item));
  }

  private void addItemInternal(final Player player, final ItemStack item) {
    final var left = player.getInventory().addItem(item);
    if (left.isEmpty()) {
      return;
    }

    this.getComponent(ClaimComponent.class).items().addAll(left.values());
    final var plural = left.size() > 1;
    this.sendPrefixedMessage(
        (plural ? "Plusieurs" : "Un")
            + " objet"
            + (plural ? "s" : "")
            + " "
            + (plural ? "viennent" : "vient")
            + " d'être ajouté"
            + (plural ? "s" : "")
            + " à votre&a /full&r.");
  }

  @Override
  public ProfileState getState() {
    return this.getComponent(ProfileStateComponent.class).getState();
  }

  @Override
  public void setState(final ProfileState state) {
    if (this.getState() != null) {
      this.getState().onExit(this);
    }

    this.getComponent(ProfileStateComponent.class).setState(state);
    state.onEntry(this);
  }

  @Override
  public void addPotionEffect(final PotionEffect effect) {
    this.executeAction(player -> this.addEffectInternal(player, effect));
  }

  private void addEffectInternal(final Player player, final PotionEffect effect) {
    player.addPotionEffect(effect, true);
  }

  @Override
  public void removePotionEffect(final PotionEffectType type) {
    this.executeAction(player -> this.removeEffectInternal(player, type));
  }

  private void removeEffectInternal(final Player player, final PotionEffectType type) {
    player.removePotionEffect(type);
  }

  @Override
  public boolean hasPotionEffect(final PotionEffectType type) {
    return this.findPlayer().map(player -> player.hasPotionEffect(type)).orElse(false);
  }

  @Override
  public @Nullable PotionEffect getPotionEffect(final PotionEffectType type) {
    return this.findPlayer()
        .flatMap(
            player ->
                player.getActivePotionEffects().stream()
                    .filter(effect -> effect.getType().equals(type))
                    .findFirst())
        .orElse(null);
  }

  @Override
  public Optional<PotionEffect> findPotionEffect(final PotionEffectType type) {
    return Optional.ofNullable(this.getPotionEffect(type));
  }

  @Override
  public void damage(double damage, boolean absorptionBypass, boolean visible) {
    final PlayerDamageEvent event =
        this.ultraHardcore
            .eventService()
            .dispatchEvent(new PlayerDamageEvent(this, damage, absorptionBypass, visible));

    if (event.isCancelled()) {
      return;
    }

    damage = event.damage();
    absorptionBypass = event.absorptionBypass();
    visible = event.visible();

    final boolean finalAbsorptionBypass = absorptionBypass;
    final double finalDamage = damage;
    final boolean finalVisible = visible;

    this.executeAction(
        player -> {
          if (player instanceof final CraftPlayer craftPlayer && !finalAbsorptionBypass) {
            final float absorption = craftPlayer.getHandle().getAbsorptionHearts();
            if (absorption > 0) {
              if (absorption >= finalDamage) {
                craftPlayer.getHandle().setAbsorptionHearts((float) (absorption - finalDamage));
              } else {
                craftPlayer.getHandle().setAbsorptionHearts(0f);
                player.setHealth(Math.max(0, player.getHealth() - (finalDamage - absorption)));
              }
            } else {
              player.setHealth(Math.max(0, player.getHealth() - finalDamage));
            }
          } else {
            player.setHealth(Math.max(player.getHealth() - finalDamage, 0));
          }

          if (finalVisible) {
            createPacketWrapper(
                    new WrapperPlayServerEntityAnimation(
                        player.getEntityId(),
                        WrapperPlayServerEntityAnimation.EntityAnimationType.HURT))
                .send(player.getWorld().getPlayers());

            player.getWorld().playSound(player.getLocation(), Sound.HURT_FLESH, 1f, 1f);
          }
        });
  }

  @Override
  public void addHealth(final double health) {
    this.setHealth(this.getHealth() + health);
  }

  @Override
  public void removeHealth(final double health) {
    this.setHealth(this.getHealth() - health);
  }

  @Override
  public void setHealth(final double health) {
    this.executeAction(player -> this.setHealthInternal(player, health));
  }

  private void setHealthInternal(final Player player, final double health) {
    player.setHealth(Math.min(health, player.getMaxHealth()));
  }

  @Override
  public double getHealth() {
    return this.findPlayer().map(Player::getHealth).orElse(0.0);
  }

  @Override
  public void addMaxHealth(final double maxHealth) {
    this.setMaxHealth(this.getMaxHealth() + maxHealth);
  }

  @Override
  public void removeMaxHealth(final double maxHealth) {
    this.setMaxHealth(this.getMaxHealth() - maxHealth);
  }

  @Override
  public void setMaxHealth(final double maxHealth) {
    this.executeAction(player -> this.setMaxHealthInternal(player, maxHealth));
  }

  private void setMaxHealthInternal(final Player player, final double maxHealth) {
    player.setMaxHealth(Math.max(maxHealth, player.getHealth()));
  }

  @Override
  public double getMaxHealth() {
    return this.findPlayer().map(Player::getMaxHealth).orElse(20.0);
  }

  @Override
  public void addDamageImmunity(final EntityDamageEvent.DamageCause cause, final int ticks) {
    final var component = this.getComponent(DamageImmunityComponent.class);
    if (component == null) {
      return;
    }

    component.immunities().add(new DamageImmunityComponent.DamageImmunity(cause, ticks));
  }

  @Override
  public void removeDamageImmunity(final EntityDamageEvent.DamageCause cause) {
    final var component = this.getComponent(DamageImmunityComponent.class);
    if (component == null) {
      return;
    }

    component.immunities().removeIf(immunity -> immunity.cause() == cause);
  }

  @Override
  public boolean hasDamageImmunity(final EntityDamageEvent.DamageCause cause) {
    final var component = this.getComponent(DamageImmunityComponent.class);
    if (component == null) {
      return false;
    }

    return component.immunities().stream().anyMatch(immunity -> immunity.cause() == cause);
  }

  @Override
  public int getDamageImmunityTicks(final EntityDamageEvent.DamageCause cause) {
    final var component = this.getComponent(DamageImmunityComponent.class);
    if (component == null) {
      return UNKNOWN_IMMUNITY_TICKS;
    }

    return component.immunities().stream()
        .filter(immunity -> immunity.cause() == cause)
        .findFirst()
        .map(DamageImmunityComponent.DamageImmunity::timeLeft)
        .orElse(UNKNOWN_IMMUNITY_TICKS);
  }

  @Override
  public void sendMessage(final String message) {
    this.executeAction(player -> player.sendMessage(CC.colorize(message)));
  }

  @Override
  public void sendPrefixedMessage(final String message) {
    this.sendMessage(CC.prefix(message));
  }

  @Override
  public void sendPrefixedMessage(final String message, final String prefix) {
    this.sendMessage(CC.prefix(message, prefix));
  }

  @Override
  public Collection<Component> getComponents() {
    return this.components.values();
  }

  @Override
  public <E extends Entity<UUID>> E addComponent(final Component component) {
    this.components.put(component.getClass(), component);
    return (E) this;
  }

  @Override
  public boolean removeComponent(final Class<? extends Component> componentClass) {
    return this.components.remove(componentClass) != null;
  }

  @Override
  public boolean hasComponent(final Class<? extends Component> componentClass) {
    return this.components.containsKey(componentClass);
  }

  @Override
  public <T extends Component> @Nullable T getComponent(final Class<T> componentClass) {
    return Optional.ofNullable(this.components.get(componentClass))
        .map(componentClass::cast)
        .orElse(null);
  }

  @Override
  public Collection<AbstractPower> getPowers() {
    return List.copyOf(this.powers.values());
  }

  @Override
  public <T extends AbstractPower> @Nullable T getPower(final Class<T> clazz) {
    return this.powers.values().stream()
        .filter(power -> clazz.isAssignableFrom(power.getClass()))
        .map(clazz::cast)
        .findFirst()
        .orElse(null);
  }

  @Override
  public void registerPower(final AbstractPower power) {
    this.registerPower(power, false);
  }

  @Override
  public void registerPower(final AbstractPower power, final boolean roleAttribution) {
    this.powers.put(power.getId(), power);

    if (power instanceof final Listener listener) {
      this.ultraHardcore.eventService().registerListener(listener);
    }

    if (power instanceof final AbstractItemPower itemPower) {
      final var player = this.getPlayer();
      if (player == null || !player.isOnline()) {
        return;
      }

      if ((!roleAttribution || itemPower.shouldDistributePower(player))) {
        final var icon = itemPower.getIcon(player);
        this.addItem(icon);
      }
    }

    if (power instanceof final AbstractParentItemPower parentPower) {
      for (final AbstractItemPower child : parentPower.getChildren()) {
        this.registerChildPower(child);
      }
    }
  }

  private void registerChildPower(final AbstractPower power) {
    this.powers.put(power.getId(), power);

    if (power instanceof final Listener listener) {
      this.ultraHardcore.eventService().registerListener(listener);
    }
  }

  @Override
  public void unregisterPower(final String id) {
    this.powers.remove(id);
  }

  @Override
  public void sendPacket(final PacketWrapper<?> wrapper) {
    this.executeAction(player -> this.sendPacketInternal(player, wrapper));
  }

  @Override
  public void sendPacket(final Packet<?> packet) {
    this.executeAction(player -> this.sendPacketInternal(player, packet));
  }

  private void sendPacketInternal(final Player player, final PacketWrapper<?> wrapper) {
    PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);
  }

  private void sendPacketInternal(final Player player, final Packet<?> packet) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }
}
