package dev.kurai.uhc.module.role;

import dev.kurai.uhc.profile.Profile;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class AbstractRole {

  protected final RoleMeta roleMeta;
  protected final Profile owner;

  public AbstractRole(final RoleMeta roleMeta, final Profile owner) {
    this.roleMeta = roleMeta;
    this.owner = owner;
  }

  public RoleMeta roleMeta() {
    return this.roleMeta;
  }

  public Profile owner() {
    return this.owner;
  }

  public void onDistribute(final Profile gameProfile, final Player player) {}

  public void onDescription(final Player player) {}

  public void onDay(final Player player) {}

  public void onNight(final Player player) {}

  public void onEpisode(final Player player, final int episode) {}

  public void onSecond(final Player player, final int seconds) {}

  public void onTick(final Player player) {}

  protected boolean hasSword(Player player) {
    return player.getItemInHand() != null
        && player.getItemInHand().getType().name().contains("SWORD");
  }

  public List<Component> description() {
    return List.of();
  }
}
