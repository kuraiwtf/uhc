package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.menu.ConfigurationMenu;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DeadComponent;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.InventoryEditorComponent;
import dev.kurai.uhc.profile.state.DeadProfileState;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.util.CC;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@RequiredArgsConstructor
@Command(
    @CommandMeta(name = "host", aliases = "h", description = "Commande de gestion de la partie"))
public final class HostCommand {

  private final BukkitAudiences bukkitAudiences;
  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(@CommandMeta(name = "add", description = "Ajouter un co-hôte"))
  public void add(final Player player, final @Argument(name = "joueur") Player target) {
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    if (hostService.coHost(target)) {
      player.sendMessage(CC.prefix("Ce joueur est déjà un co-hôte de la partie."));
      return;
    }

    hostService.addCoHost(target.getUniqueId());
    player.sendMessage(
        CC.prefix(
            "Vous avez ajouté&6 %s&f comme co-hôte de la partie.".formatted(target.getName())));
  }

  @SubCommand(@CommandMeta(name = "remove", description = "Retirer un co-hôte"))
  public void remove(final Player player, final @Argument(name = "joueur") Player target) {
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    if (!hostService.coHost(target)) {
      player.sendMessage(CC.prefix("Ce joueur n'est pas un co-hôte de la partie."));
      return;
    }

    hostService.removeCoHost(target.getUniqueId());
    player.sendMessage(
        CC.prefix(
            "Vous avez retiré&6 %s&f des co-hôtes de la partie.".formatted(target.getName())));
  }

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
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    final Profile hostProfile = hostService.hostProfile();

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);

    profile.sendMessage("");
    profile.sendMessage("Voici les informations de la partie:");
    profile.sendMessage("");
    profile.sendMessage(
        "&8»&r Hôte principal:&6 %s"
            .formatted(hostProfile == null ? "Aucun" : hostProfile.getName()));

    profile.sendMessage("");
    final var coHosts = hostService.coHosts();
    if (coHosts.isEmpty()) {
      profile.sendMessage("&8»&c Aucun co-hôte.");
    } else {
      profile.sendMessage("&8»&r Co-hôtes&8 (&6%d&8):".formatted(coHosts.size()));
      for (final UUID coHost : coHosts) {
        final Profile coHostProfile =
            this.ultraHardcore.profileService().getOrCreateProfile(coHost);
        profile.sendMessage("   &8»&r %s".formatted(coHostProfile.getName()));
      }
    }
    profile.sendMessage("");
  }

  @SubCommand(
      @CommandMeta(name = "refill", description = "Redonner les objets de pouvoir à un joueur"))
  public void refill(
      final Player player, final @Argument(name = "joueur", defaultValue = "self") Player target) {
    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
    if (!this.ultraHardcore.gameService().hostService().isHost(player)
        && !player.getUniqueId().equals(target.getUniqueId())) {
      player.sendMessage(CC.prefix("Vous n'avez pas la permission d'effectuer cette action."));
      return;
    }

    for (final AbstractPower power : profile.getPowers()) {
      if (!(power instanceof final AbstractItemPower itemPower)) {
        continue;
      }

      target.getInventory().addItem(itemPower.getIcon(target));
    }

    player.sendMessage(
        CC.prefix("Vous avez redonné les pouvoirs à&6 %s&r.".formatted(target.getName())));
  }

  @SubCommand(
      @CommandMeta(
          name = "killoffline",
          aliases = "ko",
          description = "Éliminer un joueur hors-ligne"))
  public void killOffline(
      final Player player, final @Argument(name = "joueur") OfflinePlayer target) {
    final GameService gameService = this.ultraHardcore.gameService();
    if (gameService.startTime() == 0L) {
      player.sendMessage(CC.prefix("La partie n'est pas en cours de jeu."));
      return;
    }

    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(target.getUniqueId());
    if (target.isOnline()
        || !profile.hasComponent(DisconnectComponent.class)
        || profile.hasComponent(DeadComponent.class)) {
      player.sendMessage(
          CC.prefix("Le joueur&6 %s&r est&c mort&r ou&a en ligne&r.".formatted(target.getName())));
      return;
    }

    profile.setState(new DeadProfileState());
    gameService.deathService().eliminate(profile, null, true);

    player.sendMessage(
        CC.prefix("Vous venez d'&céliminer&f le joueur&6 %s&r.".formatted(target.getName())));
  }

  @SubCommand(@CommandMeta(name = "set", description = "Définir le joueur hôte de la partie"))
  public void set(final Player player, final @Argument(name = "joueur") Player target) {
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    final UUID host = hostService.host();
    if (host != null && host.equals(target.getUniqueId())) {
      player.sendMessage(CC.prefix("Ce joueur est déjà l'hôte principal de la partie."));
      return;
    }

    hostService.host(target.getUniqueId());
    player.sendMessage(
        CC.prefix(
            "Vous avez défini&6 %s&f comme hôte principal de la partie."
                .formatted(target.getName())));
  }

  @SubCommand(@CommandMeta(name = "revive", description = "Ressusciter un joueur"))
  public void revive(final Player player, final @Argument(name = "joueur") Player target) {
    final GameService gameService = this.ultraHardcore.gameService();
    if (gameService.startTime() == 0L) {
      player.sendMessage(CC.prefix("La partie n'est pas en cours de jeu."));
      return;
    }

    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(target.getUniqueId());
    final DeadComponent component = profile.getComponent(DeadComponent.class);
    if (component == null) {
      player.sendMessage(CC.prefix("Le joueur&6 %s&r n'est pas mort.".formatted(target.getName())));
      return;
    }

    target.setGameMode(GameMode.SURVIVAL);

    target.setHealth(target.getMaxHealth());

    target.setFoodLevel(20);
    target.setSaturation(20.0f);
    target.setExhaustion(0.0f);

    target.teleport(component.location());

    final PlayerInventory inventory = target.getInventory();
    inventory.setContents(component.inventory());
    inventory.setArmorContents(component.armor());

    for (final Player receiver : Bukkit.getOnlinePlayers()) {
      receiver.playSound(receiver.getLocation(), Sound.ZOMBIE_UNFECT, 1f, 2f);
    }

    profile.setState(new PlayingProfileState());
    profile.removeComponent(DeadComponent.class);

    player.sendMessage(
        CC.prefix("Vous venez de&d ressusciter&f le joueur&6 %s&r.".formatted(target.getName())));
  }

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
    inventory.setContents(editorComponent.savedInventory());
    inventory.setArmorContents(editorComponent.savedArmor());

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
