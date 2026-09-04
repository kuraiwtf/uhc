package dev.kurai.uhc.listener.game;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Material.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.event.defaults.game.GameTickEvent;
import dev.kurai.uhc.event.defaults.player.PlayerDamageByPlayerEvent;
import dev.kurai.uhc.game.configuration.game.GameConfiguration;
import dev.kurai.uhc.game.configuration.ore.OreConfiguration;
import dev.kurai.uhc.game.death.DeathContext;
import dev.kurai.uhc.game.rule.GameRuleService;
import dev.kurai.uhc.menu.rules.ore.OreType;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.*;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.SitUtil;
import dev.kurai.uhc.util.TimeUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class PlayingListener implements Listener {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayingListener(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @EventHandler
  public void onPreLogin(final AsyncPlayerPreLoginEvent event) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(event.getUniqueId());
    final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(event.getUniqueId());
    if (!(profile.getState() instanceof PlayingProfileState)
        && !offlinePlayer.isOp()
        && !offlinePlayer.isWhitelisted()
        && !GameConfiguration.SPECTATOR_OPTION.getValue()) {
      event.disallow(
          AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
          "Les spectateurs sont interdits dans la partie.");
    }
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    event.setJoinMessage(null);

    final Player player = event.getPlayer();

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (!(profile.getState() instanceof PlayingProfileState)) {
      player.teleport(this.ultraHardcore.worldService().getWorld().getSpawnLocation());
      player.setGameMode(GameMode.SPECTATOR);
      return;
    }

    this.ultraHardcore
        .gameService()
        .sendMessage(
            prefix()
                .append(text("Le joueur "))
                .append(text(player.getName(), NamedTextColor.AQUA))
                .append(text(" s'est "))
                .append(text("reconnecté", NamedTextColor.GREEN))
                .append(text('.'))
                .build());

    final PlayerInformationComponent informationComponent =
        profile.getComponent(PlayerInformationComponent.class);
    if (informationComponent != null) {
      player.teleport(informationComponent.lastLocation());

      final PlayerInventory inventory = player.getInventory();
      inventory.setContents(informationComponent.inventory());
      inventory.setArmorContents(informationComponent.armor());

      player.setFireTicks(informationComponent.fireTicks());
      player.setFallDistance(informationComponent.fallDistance());

      profile.removeComponent(PlayerInformationComponent.class);
    }

    final DisconnectComponent component = profile.getComponent(DisconnectComponent.class);
    if (component == null) {
      return;
    }

    component.timeLeft(
        component.timeLeft() - Duration.between(component.lastLogin(), Instant.now()).toMillis());
    component.lastLogin(Instant.now());
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    event.setQuitMessage(null);

    final Player player = event.getPlayer();
    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (!(profile.getState() instanceof PlayingProfileState)) {
      return;
    }

    final DisconnectComponent disconnectComponent = profile.getComponent(DisconnectComponent.class);
    if (disconnectComponent == null) {
      return;
    }

    Bukkit.broadcastMessage(
        CC.prefix(
                "Le joueur&6 %s&r vient de se&c déconnecter&r. Il dispose de&6 &l%s&r pour se reconnecter avant d'être&c éliminé&r.")
            .formatted(player.getName(), TimeUtil.formatDuration(disconnectComponent.timeLeft())));

    final PlayerInventory inventory = player.getInventory();
    profile.addComponent(
        new PlayerInformationComponent(
            player.getLocation(),
            inventory.getContents(),
            inventory.getArmorContents(),
            Lists.newArrayList(player.getActivePotionEffects()),
            player.getFireTicks(),
            player.getFallDistance()));
  }

  @EventHandler
  public void onSitKick(final PlayerKickEvent event) {
    if (event.getReason().equals("Flying is not enabled on this server")
        && SitUtil.isSit(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onSuffocationDamage(final EntityDamageEvent event) {
    final GameRuleService ruleService = this.ultraHardcore.gameService().ruleService();
    final Block block = event.getEntity().getLocation().clone().add(0, 1, 0).getBlock();
    if (event.getCause() == SUFFOCATION
        && block.getType() != AIR
        && !ruleService.isRuleEnabled("obsidian_trap")) {
      block.setType(AIR);
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onLavaPickup(final PlayerBucketFillEvent event) {
    final GameRuleService ruleService = this.ultraHardcore.gameService().ruleService();
    final Material bucket = event.getItemStack().getType();
    if (bucket == LAVA_BUCKET && !ruleService.isRuleEnabled("lava")) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onLavaPlace(final PlayerBucketEmptyEvent event) {
    final GameRuleService ruleService = this.ultraHardcore.gameService().ruleService();
    final Material bucket = event.getBucket();
    if (bucket == LAVA_BUCKET && !ruleService.isRuleEnabled("lava")) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onDamage(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof final Player player)
        || !(event.getEntity() instanceof final Player victim)
        || event.isCancelled()
        || victim.getNoDamageTicks() > 10) {
      return;
    }

    final var profileService = this.ultraHardcore.profileService();
    final var attackerProfile = profileService.getOrCreateProfile(player);
    final var victimProfile = profileService.getOrCreateProfile(victim);

    this.ultraHardcore
        .eventService()
        .dispatchEvent(new PlayerDamageByPlayerEvent(attackerProfile, victimProfile, event));
  }

  @EventHandler
  public void processOfflineActions(final GameTickEvent event) {
    for (final var profile :
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> profile.findPlayer().isPresent())) {
      final var component = profile.getComponent(OfflineActionComponent.class);
      if (component == null) {
        continue;
      }

      final var poll = component.actions().poll();
      if (poll == null) {
        continue;
      }

      final var player = profile.getPlayer();
      poll.onJoin(player);
    }
  }

  @EventHandler
  public void processDamageImmunities(final GameTickEvent event) {
    for (final var profile :
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> profile.findPlayer().isPresent())) {
      final var component = profile.getComponent(DamageImmunityComponent.class);
      if (component == null) {
        continue;
      }

      final var iterator = component.immunities().iterator();
      while (iterator.hasNext()) {
        final var next = iterator.next();
        if (next.timeLeft() < 0) {
          return;
        }

        next.timeLeft(next.timeLeft() - 1);

        if (next.timeLeft() > 0) {
          return;
        }

        iterator.remove();
      }
    }
  }

  @EventHandler
  public void processDamageImmunity(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof final Player player)) {
      return;
    }

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (!profile.hasDamageImmunity(event.getCause())) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void processImmunityUntilNextDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof final Player player)) {
      return;
    }

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    final var cause = event.getCause();
    if (profile.getDamageImmunityTicks(cause) != Profile.IMMUNITY_UNTIL_NEXT_DAMAGE_TICKS) {
      return;
    }

    profile.removeDamageImmunity(cause);
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerDeath(final PlayerDeathEvent event) {
    event.setDeathMessage(null);
    event.setKeepInventory(true);
    event.setKeepLevel(true);

    final Player player = event.getEntity();

    final ProfileService profileService = this.ultraHardcore.profileService();
    final Profile profile = profileService.getOrCreateProfile(player.getUniqueId());
    if (!(profile.getState() instanceof PlayingProfileState)) {
      player.spigot().respawn();
      player.teleport(this.ultraHardcore.worldService().getWorld().getSpawnLocation());
      return;
    }

    final PlayerInventory inventory = player.getInventory();
    profile.addComponent(
        new PlayerInformationComponent(
            player.getLocation(),
            inventory.getContents(),
            inventory.getArmorContents(),
            Lists.newArrayList(player.getActivePotionEffects()),
            player.getFireTicks(),
            player.getFallDistance()));

    final Player killer = player.getKiller();
    profile.addComponent(new ProcessingDeathComponent());
    this.ultraHardcore
        .gameService()
        .deathService()
        .processDeath(
            new DeathContext(
                profile,
                (killer == null ? null : profileService.getOrCreateProfile(killer)),
                event,
                false));
  }

  @EventHandler
  public void onDeadDamage(final EntityDamageEvent event) {
    if (event.getEntity() instanceof final Player player) {
      final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
      if (profile.hasComponent(ProcessingDeathComponent.class)) {
        event.setCancelled(true);
      }
    }
  }

  private static final Map<Material, Consumer<ProfileMiningComponent>> MINING_STATISTICS =
      Maps.newHashMap();

  static {
    MINING_STATISTICS.put(
        STONE, component -> component.setStoneMined(component.getStoneMined() + 1));
    MINING_STATISTICS.put(
        IRON_ORE, component -> component.setIronMined(component.getIronMined() + 1));
    MINING_STATISTICS.put(
        GOLD_ORE, component -> component.setGoldMined(component.getGoldMined() + 1));
    MINING_STATISTICS.put(
        DIAMOND_ORE, component -> component.setDiamondMined(component.getDiamondMined() + 1));
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final var block = event.getBlock();
    if (!MINING_STATISTICS.containsKey(block.getType())) {
      return;
    }

    final var profile =
        this.ultraHardcore.profileService().getOrCreateProfile(event.getPlayer().getUniqueId());
    if (profile == null) {
      return;
    }

    final var miningComponent = profile.getComponent(ProfileMiningComponent.class);
    final var blockType = block.getType();

    boolean limitReached = false;
    if (blockType == IRON_ORE) {
      final var ironLimit = OreConfiguration.IRON_LIMIT_OPTION.getValue();
      if (ironLimit > 0 && miningComponent.getIronMined() >= ironLimit) {
        limitReached = true;
      }
    } else if (blockType == GOLD_ORE) {
      final var goldLimit = OreConfiguration.GOLD_LIMIT_OPTION.getValue();
      if (goldLimit > 0 && miningComponent.getGoldMined() >= goldLimit) {
        limitReached = true;
      }
    } else if (blockType == DIAMOND_ORE) {
      final var diamondLimit = OreConfiguration.DIAMOND_LIMIT_OPTION.getValue();
      if (diamondLimit > 0 && miningComponent.getDiamondMined() >= diamondLimit) {
        limitReached = true;
      }
    }

    if (limitReached) {
      if (blockType == DIAMOND_ORE) {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 2));
      }

      event.getBlock().setType(AIR);
      return;
    }

    MINING_STATISTICS.get(blockType).accept(miningComponent);

    final var oreName = ORE_NAMES.get(blockType);
    if (oreName != null) {
      final var limit = ORE_TYPES.get(blockType).getOption().getValue();
      if (limit > 0) {
        final var oreColor = oreName.color();
        profile
            .getActionbar()
            .registerActionbarEntry(
                UltraHardcoreKey.key("ore_mined"),
                text("Vous venez de miner ")
                    .append(text(ARTICLES.get(blockType)))
                    .append(oreName)
                    .append(text("."))
                    .appendSpace()
                    .append(text('(', NamedTextColor.GRAY))
                    .append(text(this.getMinedAmount(miningComponent, blockType), oreColor))
                    .append(text('/', NamedTextColor.GRAY))
                    .append(text(limit, oreColor))
                    .append(text(')', NamedTextColor.GRAY)),
                Duration.ofSeconds(3L));
      }
    }
  }

  private static final Map<Material, OreType> ORE_TYPES =
      Map.of(
          IRON_ORE, OreType.IRON,
          GOLD_ORE, OreType.GOLD,
          DIAMOND_ORE, OreType.DIAMOND);

  private static final Map<Material, Component> ORE_NAMES =
      Map.of(
          IRON_ORE, text("fer", NamedTextColor.GRAY),
          GOLD_ORE, text("or", NamedTextColor.YELLOW),
          DIAMOND_ORE, text("diamant", NamedTextColor.AQUA));

  private static final Map<Material, String> ARTICLES =
      Map.of(
          IRON_ORE, "du ",
          GOLD_ORE, "de l'",
          DIAMOND_ORE, "du ");

  private int getMinedAmount(final ProfileMiningComponent component, final Material blockType) {
    if (blockType == IRON_ORE) {
      return component.getIronMined();
    } else if (blockType == GOLD_ORE) {
      return component.getGoldMined();
    } else {
      return component.getDiamondMined();
    }
  }
}
