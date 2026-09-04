package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import java.time.Instant;
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
    new ScatterTask(this.ultraHardcore).runTaskTimer(this.ultraHardcore.plugin(), 0, 1L);
  }

  @Override
  public void handlePlayerLateScatter(final Player player) {
    final Location location =
        this.positionProvider
            .provideLocations(BorderConfiguration.INITIAL_SIZE_OPTION.getValue() - 50, 1)
            .iterator()
            .next();

    location.getChunk().load(true);
    player.teleport(location);

    player.setGameMode(GameMode.SURVIVAL);

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    profile.setState(new PlayingProfileState());
    profile.removeComponent(SpectatorComponent.class);
    profile.addComponent(
        new DisconnectComponent(
            this.ultraHardcore.gameService().disconnectService().disconnectTime(), Instant.now()));
    profile.addDamageImmunityUntilNext(EntityDamageEvent.DamageCause.FALL);

    final var inventory = player.getInventory();
    inventory.setContents(InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue());
    inventory.setArmorContents(InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue());
  }
}
