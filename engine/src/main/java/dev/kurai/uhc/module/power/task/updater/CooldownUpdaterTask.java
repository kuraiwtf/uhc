package dev.kurai.uhc.module.power.task.updater;

import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.Color;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CooldownUpdaterTask implements Runnable {

  private final ProfileService profileService;

  public CooldownUpdaterTask(final @NotNull ProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public void run() {
    for (final var profile : this.profileService.getProfiles()) {
      if (profile.findPlayer().isEmpty()) {
        continue;
      }

      final var player = Objects.requireNonNull(profile.getPlayer());
      for (final var power : profile.getPowers()) {
        this.processPower(profile, player, power);
      }
    }
  }

  private void processPower(
      final @NotNull Profile profile,
      final @NotNull Player player,
      final @NotNull AbstractPower power) {
    if (power instanceof final AbstractItemPower itemPower) {
      this.processItemPower(profile, player, itemPower);
    } else {
      this.registerPowerEntry(profile, player, power);
    }
  }

  private void processItemPower(
      final @NotNull Profile profile,
      final @NotNull Player player,
      final @NotNull AbstractItemPower itemPower) {
    if (!itemPower.getIcon(player).isSimilar(player.getItemInHand())) {
      this.removeItemPowerEntry(profile, player, itemPower);
      return;
    }

    final var actionbar = profile.getActionbar();
    final var actionbarEntry = itemPower.provideActionbarEntry(player);
    if (actionbarEntry == null) {
      actionbar.registerEntry(
          UltraHardcoreKey.key(itemPower.getId()), this.buildStatusComponent(itemPower));
      return;
    }

    actionbar.registerEntry(actionbarEntry);
  }

  private void removeItemPowerEntry(
      final @NotNull Profile profile,
      final @NotNull Player player,
      final @NotNull AbstractItemPower itemPower) {
    final var actionbarEntry = itemPower.provideActionbarEntry(player);
    final var actionbar = profile.getActionbar();

    if (actionbarEntry == null) {
      actionbar.unregisterEntry(UltraHardcoreKey.key(itemPower.getId()));
    } else {
      actionbar.unregisterEntry(actionbarEntry.key());
    }
  }

  private void registerPowerEntry(
      final @NotNull Profile profile,
      final @NotNull Player player,
      final @NotNull AbstractPower power) {
    final var actionbarEntry = power.provideActionbarEntry(player);
    final var actionbar = profile.getActionbar();

    if (actionbarEntry == null) {
      actionbar.unregisterEntry(Key.key(power.getId()));
      return;
    }

    actionbar.registerEntry(actionbarEntry);
  }

  private @NotNull Component buildStatusComponent(final @NotNull AbstractPower power) {
    final CooldownPowerRestriction cooldown =
        power.findRestriction(CooldownPowerRestriction.class, "cooldown");
    final Color color = power.getColor();
    if (cooldown != null && cooldown.getTimeLeft() > 0) {
      return text()
          .append(text(power.getName()))
          .append(text(": "))
          .append(text(cooldown.getTimeLeft(), color.asAdventureColor(), TextDecoration.BOLD))
          .append(text("s", color.asAdventureColor()))
          .build();
    }

    return text()
        .append(text(power.getName()))
        .append(text(": "))
        .append(text("Utilisable", NamedTextColor.GREEN))
        .build();
  }
}
