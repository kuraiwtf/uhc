package dev.kurai.uhc.whitelist.hostmc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.SpectatorVisibility;
import dev.kurai.uhc.whitelist.WhitelistService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

@NullMarked
@RequiredArgsConstructor
public final class HostMCWhitelistProviderTask extends BukkitRunnable {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String BASE_URL = "https://api.bot-mc.fr/v1/games/%s/autowhitelist?players=%s";

    private final UltraHardcoreAPI ultraHardcore;
    private final WhitelistService whitelistService;

    private final String authorization;
    private final String code;

    @Override
    public void run() {
        final String onlinePlayers = this.fetchOnlinePlayers();

        HttpURLConnection connection = null;
        try {
            final String encoded = URLEncoder.encode(onlinePlayers, StandardCharsets.UTF_8);
            final String url = BASE_URL.formatted(URLEncoder.encode(this.code, StandardCharsets.UTF_8), encoded);
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", this.authorization);
            connection.setRequestProperty("User-Agent", "kurai-UHC-Engine/v1.0-dev");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            final int statusCode = connection.getResponseCode();
            if (statusCode == 401 || statusCode == 403 || statusCode == 404) {
                this.cancel();
                final String message = statusCode == 404
                        ? CC.prefix("La partie&6 %s&r n'existe pas.".formatted(this.code))
                        : CC.prefix("La clé API du&6 bot discord d'Host MC&r est&c invalide&r.");
                this.sync(() -> Bukkit.broadcastMessage(message));
                return;
            }

            if (statusCode != 200) {
                return;
            }

            final GameDataHostMC gameData;
            try (final var stream = connection.getInputStream()) {
                gameData = MAPPER.readValue(stream, GameDataHostMC.class);
            }

            this.sync(() -> this.apply(gameData));
        } catch (final IOException e) {
            this.cancel();
            this.sync(() -> Bukkit.broadcastMessage(CC.prefix(
                    "Impossible de retrouver la&b liste blanche&r via le système du&6 bot discord d'Host MC&r.")));
            Bukkit.getLogger().log(Level.WARNING, "HostMC whitelist request failed", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String fetchOnlinePlayers() {
        try {
            return Bukkit.getScheduler()
                    .callSyncMethod(this.ultraHardcore.plugin(), () -> {
                        final var players = Bukkit.getOnlinePlayers();
                        return players.isEmpty()
                                ? "null"
                                : players.stream().map(HumanEntity::getName).collect(Collectors.joining(","));
                    })
                    .get();
        } catch (final Exception e) {
            return "null";
        }
    }

    private void sync(final Runnable runnable) {
        Bukkit.getScheduler().runTask(this.ultraHardcore.plugin(), runnable);
    }

    private void apply(final GameDataHostMC gameData) {
        if (gameData.players() == null) {
            return;
        }

        for (final PlayerDataHostMC player : gameData.players()) {
            final UUID uniqueId = this.formatUniqueId(player.uniqueId());
            if (uniqueId == null) {
                continue;
            }

            if (!this.whitelistService.isWhitelisted(uniqueId)) {
                this.whitelistService.whitelist(new UUID(0, 0), uniqueId, "Bot discord d'HostMC");
            }

            if (!player.moderator()) {
                continue;
            }

            final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(uniqueId);
            if (profile.hasComponent(SpectatorComponent.class)) {
                continue;
            }

            profile.addComponent(new SpectatorComponent());
            profile.findPlayer().ifPresent(spectatorPlayer -> spectatorPlayer.setGameMode(GameMode.SPECTATOR));
        }

        SpectatorVisibility.refresh(this.ultraHardcore);
    }

    private UUID formatUniqueId(final String uniqueId) {
        if (uniqueId == null || uniqueId.isEmpty()) {
            return null;
        }

        try {
            return UUID.fromString(uniqueId.replaceFirst(
                    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})",
                    "$1-$2-$3-$4-$5"));
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
