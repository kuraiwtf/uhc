package dev.kurai.uhc.whitelist.hostmc;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.whitelist.WhitelistService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public final class HostMCWhitelistProviderTask extends BukkitRunnable {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final String BASE_URL =
      "https://api.bot-mc.fr/v1/games/%s/autowhitelist?players=%s";

  private final WhitelistService whitelistService;

  private final String authorization;
  private final String code;

  @Override
  public void run() {
    final String onlinePlayers =
        Bukkit.getOnlinePlayers().isEmpty()
            ? "null"
            : Bukkit.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .collect(Collectors.joining(","));

    try {
      final URL url = new URL(BASE_URL.formatted(this.code, onlinePlayers));
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      connection.setRequestProperty("Authorization", this.authorization);
      connection.setRequestProperty("User-Agent", "kurai-UHC-Engine/v1.0-dev");

      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);

      final int statusCode = connection.getResponseCode();
      if (statusCode == 401 || statusCode == 403 || statusCode == 404) {
        this.cancel();
        if (statusCode == 401 || statusCode == 403) {
          Bukkit.broadcastMessage(
              CC.prefix("La clé API du&6 bot discord d'Host MC&r est&c invalide&r."));
        } else {
          Bukkit.broadcastMessage(CC.prefix("La partie&6 %s&r n'existe pas.".formatted(this.code)));
        }
      } else if (statusCode == 200) {
        final BufferedReader in =
            new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder content = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
          content.append("\n");
        }

        in.close();

        final GameDataHostMC gameData = MAPPER.readValue(content.toString(), GameDataHostMC.class);
        for (final PlayerDataHostMC player : gameData.players()) {
          final UUID uniqueId = this.formatUniqueId(player.uniqueId());
          if (this.whitelistService.isWhitelisted(uniqueId)) {
            continue;
          }

          this.whitelistService.whitelist(new UUID(0, 0), uniqueId, "Bot discord d'HostMC");
        }
      }
      connection.disconnect();
    } catch (final Exception e) {
      Bukkit.broadcastMessage(
          "Impossible de retrouver la&b liste blanche&r via le système du&6 bot discord d'Host MC&r pour la partie avec le code&b %s&r."
              .formatted(this.code));
      this.cancel();
      e.printStackTrace();
    }
  }

  private UUID formatUniqueId(final String uniqueId) {
    if (uniqueId.isEmpty()) {
      return new UUID(0, 0);
    }

    return UUID.fromString(
        uniqueId.replaceFirst(
            "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})",
            "$1-$2-$3-$4-$5"));
  }
}
