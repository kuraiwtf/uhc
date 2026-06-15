package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public final class ScatterTask extends BukkitRunnable {

  private final UltraHardcoreAPI ultraHardcore;

  private final List<UUID> players;
  private final List<Location> positions;

  public ScatterTask(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.players =
        Lists.newArrayList(
            ultraHardcore
                .profileService()
                .getProfiles(profile -> !profile.hasComponent(SpectatorComponent.class))
                .stream()
                .map(Profile::getId)
                .toList());
    this.positions =
        Lists.newArrayList(
            ultraHardcore
                .gameService()
                .scatterService()
                .getPositionProvider()
                .provideLocations(
                    BorderConfiguration.INITIAL_SIZE_OPTION.getValue() - 50, this.players.size()));
  }

  @Override
  public void run() {
    if (this.players.isEmpty()) {
      this.ultraHardcore.gameService().startService().handleFinalStart();
      this.cancel();
      return;
    }

    final var found = this.players.removeFirst();
    final var location = this.positions.removeFirst();
    location.getChunk().load(true);

    final var player = Bukkit.getPlayer(found);
    player.teleport(location);
    player.sendMessage("Téléportation");
  }
}
