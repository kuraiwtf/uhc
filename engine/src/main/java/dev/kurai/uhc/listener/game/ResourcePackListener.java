package dev.kurai.uhc.listener.game;

import static dev.kurai.uhc.util.CC.prefix;
import static dev.kurai.uhc.util.packet.PacketWrapper.createPacketWrapper;
import static net.kyori.adventure.text.Component.text;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import dev.kurai.uhc.module.component.ModuleResourcePackComponent;
import dev.kurai.uhc.module.component.ModuleResourcePackComponent.ResourcePack;
import dev.kurai.uhc.module.service.ModuleService;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public final class ResourcePackListener extends PacketListenerAbstract implements Listener {

  private final ModuleService moduleService;

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final ModuleResourcePackComponent component =
        this.moduleService.getCurrentModule().getComponent(ModuleResourcePackComponent.class);
    if (component != null) {
      final Player player = event.getPlayer();
      for (final ResourcePack pack : component.packs()) {
        createPacketWrapper(new PacketPlayOutResourcePackSend(pack.url(), pack.hash()))
            .send(player);
      }
    }
  }

  @Override
  public void onPacketReceive(final PacketReceiveEvent event) {
    if (event.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS) {
      final WrapperPlayClientResourcePackStatus packet =
          new WrapperPlayClientResourcePackStatus(event);
      final Player player = event.getPlayer();
      if (packet.getResult() == WrapperPlayClientResourcePackStatus.Result.ACCEPTED) {
        player.sendMessage(
            prefix("Vous avez téléchargé les&d packs de ressource&r avec&a succès&r."));
      }

      if (packet.getResult() == WrapperPlayClientResourcePackStatus.Result.DECLINED) {
        player.kick(
            text(
                "Vous avez refusé le pack de ressources, vous avez alors été expulsé de la partie."));
      }
    }
  }
}
