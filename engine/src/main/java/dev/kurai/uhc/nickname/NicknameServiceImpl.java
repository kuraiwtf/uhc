package dev.kurai.uhc.nickname;

import com.google.common.collect.Maps;
import dev.kurai.uhc.util.Color;
import java.util.Map;
import java.util.UUID;
import org.bukkit.plugin.Plugin;

public final class NicknameServiceImpl implements NicknameService {

  private final Map<UUID, Nickname> nicknames;
  private final Map<UUID, NicknameHolder> holders;

  public NicknameServiceImpl(final Plugin plugin) {
    this.nicknames = Maps.newHashMap();
    this.holders = Maps.newHashMap();
    plugin.getServer().getPluginManager().registerEvents(new NicknameListener(this), plugin);
  }

  @Override
  public Nickname nickname(final UUID uniqueId) {
    return this.nicknames.computeIfAbsent(uniqueId, id -> new NicknameImpl(Color.WHITE));
  }

  @Override
  public NicknameHolder holder(final UUID uniqueId) {
    return this.holders.computeIfAbsent(uniqueId, id -> new NicknameHolderImpl(id, this));
  }
}
