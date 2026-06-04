package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.module.component.ModuleDocumentationComponent;
import dev.kurai.uhc.module.component.ModuleResourcePackComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ModuleCommands {

  private final UltraHardcoreAPI ultraHardcore;

  public ModuleCommands(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Command(@CommandMeta(name = "documentation", aliases = "doc"))
  public void documentation(final Player player) {
    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    final var component = module.getComponent(ModuleDocumentationComponent.class);

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (component == null) {
      profile.sendPrefixedMessage("Le jeu de la partie ne possède pas de documentation.");
      return;
    }

    profile.sendPrefixedMessage(
        "Voici le lien de la documentation du jeu de la partie:&e %s&f."
            .formatted(component.documentation()));
  }

  @Command(@CommandMeta(name = "pack"))
  public void pack(final Player player) {
    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    final var component = module.getComponent(ModuleResourcePackComponent.class);

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (component == null || component.packs().isEmpty()) {
      profile.sendPrefixedMessage("Le jeu de la partie ne possède pas de pack de ressources.");
      return;
    }

    for (final var pack : component.packs()) {
      ((CraftPlayer) player)
          .getHandle()
          .playerConnection
          .sendPacket(new PacketPlayOutResourcePackSend(pack.url(), pack.hash()));
    }

    profile.sendPrefixedMessage(
        "Vous avez bien reçu les&a packs de ressources&f du jeu de la partie.");
  }
}
