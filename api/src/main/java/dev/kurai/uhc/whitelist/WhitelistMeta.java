package dev.kurai.uhc.whitelist;

import java.time.LocalDateTime;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record WhitelistMeta(UUID id, UUID executor, String source, LocalDateTime whitelistedAt) {

  public OfflinePlayer asOfflinePlayer() {
    return Bukkit.getOfflinePlayer(this.id);
  }
}
