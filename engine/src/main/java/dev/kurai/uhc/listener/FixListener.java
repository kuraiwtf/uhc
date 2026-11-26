package dev.kurai.uhc.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Collections;
import java.util.List;

/**
 * @author Super_Crafting
 */
public final class FixListener extends PacketListenerAbstract implements Listener {

    private static final Enchantment FAKE_ENCHANTMENT = new Enchantment(EnchantmentTypes.UNBREAKING, 67);

    private static void refreshWorld(final World world, final Player player) {
        for (final var worldPlayer : world.getPlayers()) {
            if (worldPlayer == player) {
                continue;
            }

            worldPlayer.hidePlayer(player);
            worldPlayer.showPlayer(player);
        }
    }

    private static void updateEntity(final Entity entity, final List<Player> observers) {
        final var world = entity.getWorld();
        final var worldServer = ((CraftWorld) world).getHandle();

        final var tracker = worldServer.tracker;
        final var entry = tracker.trackedEntities.get(entity.getEntityId());

        final var nmsPlayers = getNmsPlayers(observers);

        // Force Minecraft to resend packets to the affected clients
        if (entry != null && entry.trackedPlayers != null) {
            nmsPlayers.forEach(nmsPlayer -> entry.trackedPlayers.remove(nmsPlayer));
            for (final EntityPlayer nmsPlayer : nmsPlayers) {
                entry.updatePlayer(nmsPlayer);
            }
        }
    }

    private static List<EntityPlayer> getNmsPlayers(final List<Player> players) {
        return players.stream()
                .map(CraftPlayer.class::cast)
                .map(CraftPlayer::getHandle)
                .toList();
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Player player = event.getPlayer();
        final PacketTypeCommon packetType = event.getPacketType();
        if (packetType == PacketType.Play.Server.ENTITY_EFFECT) {
            final var packet = new WrapperPlayServerEntityEffect(event);
            if (player.getEntityId() != packet.getEntityId() && packet.getPotionType() != PotionTypes.INVISIBILITY) {
                event.setCancelled(true);
            }
        }

        if (packetType == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            final var packet = new WrapperPlayServerRemoveEntityEffect(event);
            if (player.getEntityId() != packet.getEntityId() && packet.getPotionType() != PotionTypes.INVISIBILITY) {
                event.setCancelled(true);
            }
        }

        if (packetType == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            final WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(event);
            if (player.getEntityId() != packet.getEntityId()) {
                for (final Equipment equipment : packet.getEquipment()) {
                    if (equipment == null || equipment.getItem() == null) {
                        continue;
                    }

                    final ItemStack item = equipment.getItem();
                    if (item.isEnchanted()) {
                        item.setEnchantments(Collections.singletonList(FAKE_ENCHANTMENT));
                        event.markForReEncode(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final var player = event.getPlayer();
        updateEntity(player, player.getWorld().getPlayers());
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        final var player = event.getPlayer();
        refreshWorld(event.getFrom(), player);
        refreshWorld(player.getWorld(), player);
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent event) {
        event.getWorld().setWeatherDuration(0);
        event.setCancelled(true);
    }
}
