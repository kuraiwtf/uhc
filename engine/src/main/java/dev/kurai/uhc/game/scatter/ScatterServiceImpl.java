package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import java.time.Instant;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ScatterServiceImpl implements ScatterService {

  private final UltraHardcoreAPI ultraHardcore;

  private ScatterPositionProvider positionProvider;

  public ScatterServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.positionProvider =
        (radius, players) -> {
          final var locations = Lists.<Location>newArrayListWithCapacity(players);

          final var world = ultraHardcore.worldService().getWorld();
          final var center = world.getSpawnLocation();

          final double angleStep = (2 * Math.PI) / players;

          for (int i = 0; i < players; i++) {
            final double angle = angleStep * i;

            final int x = (int) (center.getX() + radius * Math.cos(angle));
            final int z = (int) (center.getZ() + radius * Math.sin(angle));

            locations.add(new Location(world, x, 200, z));
          }

          return locations;
        };
  }

  @Override
  public ScatterPositionProvider getPositionProvider() {
    return this.positionProvider;
  }

  @Override
  public void setPositionProvider(final ScatterPositionProvider positionProvider) {
    this.positionProvider = positionProvider;
  }

  @Override
  public void handleScatter() {
    final var teamService = this.ultraHardcore.teamService();
    final var profiles =
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> !profile.hasComponent(SpectatorComponent.class));
    profiles.stream()
        .filter(profile -> profile.findPlayer().isEmpty())
        .forEach(profile -> teamService.leave(profile.getId()));

    final var players =
        profiles.stream()
            .filter(profile -> profile.findPlayer().isPresent())
            .map(Profile::getId)
            .toList();

    if (this.ultraHardcore.moduleService().getCurrentModule() instanceof final TeamModule module
        && module.randomTeam()) {
      teamService.randomize(players);
    } else {
      teamService.assignUnteamed(players);
    }

    new ScatterTask(this.ultraHardcore).runTaskTimer(this.ultraHardcore.plugin(), 0, 1L);
  }

  @Override
  public void handlePlayerLateScatter(final Player player) {
    final Location location =
        this.findTeammateLocation(player)
            .orElseGet(
                () ->
                    this.positionProvider
                        .provideLocations(BorderConfiguration.INITIAL_SIZE_OPTION.getValue() - 50, 1)
                        .iterator()
                        .next());

    location.getChunk().load(true);
    player.teleport(location);

    player.setGameMode(GameMode.SURVIVAL);

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    profile.setState(new PlayingProfileState());
    profile.removeComponent(SpectatorComponent.class);

    profile.addComponent(
        new DisconnectComponent(
            this.ultraHardcore.gameService().disconnectService().disconnectTime(), Instant.now()));
    profile.addComponent(new NameComponent(player.getName()));

    profile.addDamageImmunityUntilNext(EntityDamageEvent.DamageCause.FALL);

    final var inventory = player.getInventory();
    inventory.setContents(InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue());
    inventory.setArmorContents(InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue());
  }

  private Optional<Location> findTeammateLocation(final Player player) {
    return this.ultraHardcore
        .teamService()
        .teamOf(player.getUniqueId())
        .flatMap(
            team ->
                team.members().stream()
                    .filter(id -> !id.equals(player.getUniqueId()))
                    .map(Bukkit::getPlayer)
                    .filter(
                        mate ->
                            mate != null
                                && this.ultraHardcore.profileService().getOrCreateProfile(mate).getState()
                                    instanceof PlayingProfileState)
                    .findFirst()
                    .map(Player::getLocation));
  }
}
