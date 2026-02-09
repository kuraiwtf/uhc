package dev.kurai.uhc.module.power.task.updater;

import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.profile.service.ProfileService;
import java.util.Objects;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public final class CooldownUpdaterTask implements Runnable {

  private final ProfileService profileService;

  public CooldownUpdaterTask(final @NotNull ProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public void run() {
    for (final var profile : this.profileService.getProfiles()) {
      final var player = Objects.requireNonNull(profile.getPlayer());
      for (final var itemPower :
          profile.getPowers().stream()
              .filter(AbstractItemPower.class::isInstance)
              .map(AbstractItemPower.class::cast)
              .toList()) {
        final var actionbarEntry = itemPower.provideActionbarEntry(player);
        final var actionbar = profile.getActionbar();
        if (!itemPower.provideIcon(player).isSimilar(player.getItemInHand())) {
          if (actionbarEntry == null) {
            actionbar.removeEntry(itemPower.getId());
            continue;
          }

          actionbar.removeEntry(actionbarEntry.getId());
          continue;
        }

        if (actionbarEntry == null) {
          final var cooldown =
              itemPower.findRestriction(CooldownPowerRestriction.class, "cooldown");
          if (cooldown != null && cooldown.getTimeLeft() > 0) {
            actionbar.registerEntry(
                itemPower.getId(),
                text()
                    .append(text(itemPower.getName()))
                    .append(text(": "))
                    .append(text(cooldown.getTimeLeft(), NamedTextColor.RED, TextDecoration.BOLD))
                    .append(text("s", NamedTextColor.RED))
                    .build());
            continue;
          }

          actionbar.registerEntry(
              itemPower.getId(),
              text()
                  .append(text(itemPower.getName()))
                  .append(text(": "))
                  .append(text("Utilisable", NamedTextColor.GREEN))
                  .build());
          continue;
        }

        actionbar.registerEntry(actionbarEntry);
      }
    }
  }
}
