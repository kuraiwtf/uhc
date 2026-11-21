package dev.kurai.uhc.command.argument.builtin.bukkit;

import com.google.common.collect.Maps;
import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public final class GameModeArgumentResolver implements ArgumentResolver<@Nullable GameMode> {

    private static final Map<String, GameMode> GAME_MODE_MAPPING = Maps.newHashMap();

    static {
        GAME_MODE_MAPPING.put("survival", GameMode.SURVIVAL);
        GAME_MODE_MAPPING.put("s", GameMode.SURVIVAL);
        GAME_MODE_MAPPING.put("0", GameMode.SURVIVAL);
        GAME_MODE_MAPPING.put("creative", GameMode.CREATIVE);
        GAME_MODE_MAPPING.put("c", GameMode.CREATIVE);
        GAME_MODE_MAPPING.put("1", GameMode.CREATIVE);
        GAME_MODE_MAPPING.put("adventure", GameMode.ADVENTURE);
        GAME_MODE_MAPPING.put("a", GameMode.ADVENTURE);
        GAME_MODE_MAPPING.put("2", GameMode.ADVENTURE);
        GAME_MODE_MAPPING.put("spectator", GameMode.SPECTATOR);
        GAME_MODE_MAPPING.put("sp", GameMode.SPECTATOR);
        GAME_MODE_MAPPING.put("3", GameMode.SPECTATOR);
    }

    @Override
    public @Nullable GameMode resolve(final CommandSender sender, final String argument) {
        final GameMode gameMode = GAME_MODE_MAPPING.get(argument.toLowerCase(Locale.ROOT));
        if (gameMode == null) {
            sender.sendMessage(CC.prefix("§cLe mode de jeu §4" + argument + " §cn'existe pas."));
        }
        return gameMode;
    }

    @Override
    public @Unmodifiable Collection<String> complete(final CommandSender sender, final String argument) {
        return GAME_MODE_MAPPING.keySet().stream()
                .filter(s -> s.startsWith(argument.toLowerCase(Locale.ROOT)))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
