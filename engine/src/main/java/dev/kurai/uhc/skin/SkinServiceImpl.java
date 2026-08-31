package dev.kurai.uhc.skin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.google.common.collect.Maps;
import dev.kurai.uhc.profile.ProfileService;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class SkinServiceImpl implements SkinService {

  private static final EventManager EVENT_MANAGER = PacketEvents.getAPI().getEventManager();

  private final ProfileService profileService;
  private final Map<UUID, SkinHolder> skinHolders;

  public SkinServiceImpl(final Plugin plugin, final ProfileService profileService) {
    this.profileService = profileService;
    this.skinHolders = Maps.newHashMap();
    EVENT_MANAGER.registerListener(
        new SkinPacketListener(profileService, this), PacketListenerPriority.NORMAL);
    Bukkit.getScheduler().runTaskTimer(plugin, new SkinUpdaterTask(profileService, this), 0, 1L);
  }

  @Override
  public SkinHolder holder(final UUID uniqueId) {
    return this.skinHolders.computeIfAbsent(
        uniqueId, _ -> new SkinHolderImpl(this.profileService.getOrCreateProfile(uniqueId)));
  }
}
