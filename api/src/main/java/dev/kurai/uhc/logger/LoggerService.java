package dev.kurai.uhc.logger;

import org.bukkit.Location;

public interface LoggerService {

  void broadcastSpectator(
      final LogCategory category, final String content, final Location location);
}
