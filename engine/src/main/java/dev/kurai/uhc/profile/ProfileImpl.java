package dev.kurai.uhc.profile;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import dev.kurai.actionbar.Actionbar;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.effect.EffectHolderImpl;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.event.defaults.player.PlayerDamageEvent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.profile.component.ClaimComponent;
import dev.kurai.uhc.profile.component.DamageImmunityComponent;
import dev.kurai.uhc.profile.component.OfflineActionComponent;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import dev.kurai.uhc.util.CC;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static dev.kurai.uhc.util.packet.PacketWrapper.createPacketWrapper;

@NullMarked
@Getter
@Setter
final class ProfileImpl implements Profile {

    private final Entity<UUID> entity;

    private final UltraHardcoreAPI ultraHardcore;
    private final Map<String, AbstractPower> powers;

    private int kills;
    private int assists;

    ProfileImpl(final UUID id, final UltraHardcoreAPI ultraHardcore) {
        this.entity = new Entity<>(id)
                .addComponents(
                        new NameComponent(Bukkit.getOfflinePlayer(id).getName()),
                        new ClaimComponent(),
                        new DamageImmunityComponent(),
                        new ProfileMiningComponent(),
                        new OfflineActionComponent(),
                        new EffectHoldingComponent(new EffectHolderImpl(id)));

        this.ultraHardcore = ultraHardcore;
        this.powers = new ConcurrentHashMap<>();
    }

    @Override
    public Audience audience() {
        return this.ultraHardcore.bukkitAudiences().player(this.getId());
    }

    @Override
    public UUID getId() {
        return this.entity.getId();
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
        this.findPlayer().ifPresentOrElse(action, () -> {
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
        this.sendPrefixedMessage((plural ? "Plusieurs" : "Un")
                + " objet"
                + (plural ? "s" : "")
                + " "
                + (plural ? "viennent" : "vient")
                + " d'être ajouté"
                + (plural ? "s" : "")
                + " à votre&a /full&r.");
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
                .flatMap(player -> player.getActivePotionEffects().stream()
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
        final PlayerDamageEvent event = this.ultraHardcore
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

        this.executeAction(player -> {
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
                createPacketWrapper(new WrapperPlayServerEntityAnimation(
                                player.getEntityId(), WrapperPlayServerEntityAnimation.EntityAnimationType.HURT))
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

    private void setHealthInternal(final Player player, final double health) {
        player.setHealth(Math.min(health, player.getMaxHealth()));
    }

    @Override
    public double getHealth() {
        return this.findPlayer().map(Player::getHealth).orElse(0.0);
    }

    @Override
    public void setHealth(final double health) {
        this.executeAction(player -> this.setHealthInternal(player, health));
    }

    @Override
    public void addMaxHealth(final double maxHealth) {
        this.setMaxHealth(this.getMaxHealth() + maxHealth);
    }

    @Override
    public void removeMaxHealth(final double maxHealth) {
        this.setMaxHealth(this.getMaxHealth() - maxHealth);
    }

    private void setMaxHealthInternal(final Player player, final double maxHealth) {
        final double newMax = Math.max(1.0, maxHealth);
        player.setMaxHealth(newMax);
        if (player.getHealth() > newMax) {
            player.setHealth(newMax);
        }
    }

    @Override
    public double getMaxHealth() {
        return this.findPlayer().map(Player::getMaxHealth).orElse(20.0);
    }

    @Override
    public void setMaxHealth(final double maxHealth) {
        this.executeAction(player -> this.setMaxHealthInternal(player, maxHealth));
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
    public Entity<UUID> asEntity() {
        return this.entity;
    }

    @Override
    public Collection<AbstractPower> getPowers() {
        return List.copyOf(this.powers.values());
    }

    @Override
    public @Nullable AbstractPower getPower(final String id) {
        return this.powers.get(id);
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
        final AbstractPower power = this.powers.remove(id);
        if (power == null) {
            return;
        }

        if (power instanceof final AbstractItemPower itemPower) {
            this.executeAction(player -> player.getInventory().remove(itemPower.getIcon(player)));
        }

        if (power instanceof final Listener listener) {
            this.ultraHardcore.eventService().unregisterListener(listener);
        }
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
