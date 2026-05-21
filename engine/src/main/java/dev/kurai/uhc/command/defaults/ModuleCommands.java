package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.module.component.ModuleDocumentationComponent;
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
}
