package dev.kurai.uhc.module.role;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.kurai.uhc.game.cycle.AbstractCycle;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.RoleUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public abstract class AbstractRole {

  protected final RoleMeta roleMeta;
  protected final Profile profile;

  protected final Collection<AbstractPower> powers;
  protected final Set<RoleMeta> knownRoles;

  protected boolean disabledPassives;

  public AbstractRole(
      final RoleMeta roleMeta, final Profile profile, final AbstractPower... powers) {
    this.roleMeta = roleMeta;
    this.profile = profile;

    this.powers = Lists.newArrayList();
    this.knownRoles = Sets.newHashSet();

    this.powers.addAll(Arrays.asList(powers));
    this.disabledPassives = false;
  }

  public RoleMeta roleMeta() {
    return this.roleMeta;
  }

  public Profile owner() {
    return this.profile;
  }

  @Contract(pure = true)
  public final @Unmodifiable Collection<AbstractPower> getPowers() {
    return List.copyOf(this.powers);
  }

  public final <T extends AbstractPower> @Nullable T getPower(final Class<T> clazz) {
    return this.powers.stream()
        .filter(power -> clazz.isAssignableFrom(power.getClass()))
        .map(clazz::cast)
        .findFirst()
        .orElse(null);
  }

  public final void addPower(final AbstractPower power) {
    this.powers.add(power);
  }

  public final void removePower(final AbstractPower power) {
    this.powers.remove(power);
  }

  public void onDistribute(final Profile gameProfile, final Player player) {}

  public void onDescription(final Player player) {}

  public void onCycle(final Player player, final AbstractCycle cycle) {}

  public void onDay(final Player player) {}

  public void onNight(final Player player) {}

  public void onEpisode(final Player player, final int episode) {}

  public void onSecond(final Player player, final int seconds) {}

  public void onTick(final Player player) {}

  protected boolean hasSword(Player player) {
    return player.getItemInHand() != null
        && player.getItemInHand().getType().name().contains("SWORD");
  }

  protected Player getPlayer() {
    return this.profile.getPlayer();
  }

  public void knowsRoles(final boolean list, final ChatColor color, final RoleMeta... info) {
    Player player = this.getPlayer();
    if (player != null) {
      if (list) {
        final var players = Lists.<Profile>newArrayList();

        for (final RoleMeta quickRoleInfo : info) {
          players.addAll(RoleUtils.findPlayersWithRole(quickRoleInfo));
        }

        if (players.isEmpty()) {
          return;
        }

        final var foundPlayers = players.stream().map(Profile::getName).toList();
        player.sendMessage(" §8» " + color + String.join("§f, " + color, foundPlayers));
      } else {
        for (final RoleMeta quickRoleInfo : info) {
          this.knowsRole(quickRoleInfo);
        }
      }
    }
  }

  public final @Unmodifiable Set<RoleMeta> knownRoles() {
    return Set.copyOf(this.knownRoles);
  }

  public void knowsRole(final RoleMeta info) {
    final Player player = this.getPlayer();
    if (player != null) {
      final List<String> foundPlayers = Lists.newArrayList();
      for (final Profile gameProfile : RoleUtils.findPlayersWithRole(info)) {
        foundPlayers.add(gameProfile.getName());
      }

      if (!foundPlayers.isEmpty()) {
        final String nameDisplay = String.join("§f, §a", foundPlayers);
        final ChatColor color = info.camp().primaryColor();
        if (foundPlayers.size() == 1) {
          player.sendMessage(
              CC.prefix(
                  "§fLe §c"
                      + color
                      + info.getName()
                      + " §fde la partie est "
                      + color
                      + nameDisplay));
        } else {
          player.sendMessage(
              CC.prefix(
                  "§fLes §c"
                      + color
                      + info.getName()
                      + "s §fde la partie sont §a"
                      + color
                      + nameDisplay));
        }

        this.knownRoles.add(info);
      }
    }
  }

  public boolean disabledPassives() {
    return this.disabledPassives;
  }

  public void disabledPassives(final boolean disabledPassives) {
    this.disabledPassives = disabledPassives;
  }

  public List<Component> description() {
    return List.of();
  }
}
