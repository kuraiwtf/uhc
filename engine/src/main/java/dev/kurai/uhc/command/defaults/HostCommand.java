package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.menu.ConfigurationMenu;
import dev.kurai.uhc.profile.component.InventoryEditorComponent;
import dev.kurai.uhc.timer.AbstractTimer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(
    @CommandMeta(name = "host", aliases = "h", description = "Commande de gestion de la partie"))
public final class HostCommand {

  private final BukkitAudiences bukkitAudiences;
  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(@CommandMeta(name = "add", description = "Ajouter un co-hôte"))
  public void add(final Player player, final @Argument(name = "joueur") Player target) {}

  @SubCommand(@CommandMeta(name = "remove", description = "Retirer un co-hôte"))
  public void remove(final Player player, final @Argument(name = "joueur") Player target) {}

  @SubCommand(@CommandMeta(name = "config", description = "Configurer la partie"))
  public void config(final Player player) {
    new ConfigurationMenu(player, this.ultraHardcore).open();
  }

  @SubCommand(@CommandMeta(name = "force", description = "Forcer un timer"))
  public void force(final Player player, final @Argument(name = "timer") AbstractTimer timer) {
    timer.setTimeLeft(5);
    this.bukkitAudiences
        .player(player)
        .sendMessage(
            prefix()
                .append(text("Vous venez de forcer le timer "))
                .append(text(timer.getName(), NamedTextColor.GOLD))
                .append(text(" à "))
                .append(text(5, NamedTextColor.AQUA, TextDecoration.BOLD))
                .append(text('s', NamedTextColor.AQUA))
                .append(text('.'))
                .build());
  }

  @SubCommand(@CommandMeta(name = "info", description = "Afficher les informations de la partie"))
  public void info(final Player player) {
    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    profile.sendMessage("");
    profile.sendMessage("Voici les informations de la partie:");
    profile.sendMessage("");
    profile.sendMessage("&8»&r Hôte principal:&6 %s");
    profile.sendMessage("&8»&r Co-hôtes&8 (&60&8)&r:");
    profile.sendMessage("&8 -&r %s");
    profile.sendMessage("");
  }

  @SubCommand(@CommandMeta(name = "set", description = "Définir le joueur hôte de la partie"))
  public void set(final Player player, final @Argument(name = "joueur") Player target) {}

  @Command(@CommandMeta(name = "save", description = "Sauvegarder l'inventaire de départ"))
  public void saveInventory(final Player player) {
    final var profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    final var editorComponent = profile.getComponent(InventoryEditorComponent.class);
    if (editorComponent == null) {
      this.bukkitAudiences
          .player(player)
          .sendMessage(
              prefix()
                  .append(text("Vous n'êtes pas en mode édition d'inventaire.", NamedTextColor.RED))
                  .build());
      return;
    }

    final var inventory = player.getInventory();
    InventoryConfiguration.INVENTORY_CONTENT_OPTION.setValue(inventory.getContents().clone());
    InventoryConfiguration.INVENTORY_ARMOR_OPTION.setValue(inventory.getArmorContents().clone());

    profile.removeComponent(InventoryEditorComponent.class);

    inventory.clear();
    inventory.setArmorContents(null);
    inventory.setContents(editorComponent.getSavedInventory());
    inventory.setArmorContents(editorComponent.getSavedArmor());

    this.bukkitAudiences
        .player(player)
        .sendMessage(
            prefix()
                .append(text("Inventaire de départ ", NamedTextColor.GRAY))
                .append(text("sauvegardé", NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(text(" avec succès!", NamedTextColor.GRAY))
                .build());
  }
}
