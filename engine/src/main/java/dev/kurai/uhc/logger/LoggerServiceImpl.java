package dev.kurai.uhc.logger;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Location;

@RequiredArgsConstructor
public final class LoggerServiceImpl implements LoggerService {

  private final ProfileService profileService;

  @Override
  public void broadcastSpectator(
      final LogCategory category, final String content, final Location location) {
    final Component message =
        text()
            .append(text('(', DARK_GRAY))
            .append(text(category.name(), category.color().asTextColor()))
            .append(text(')', DARK_GRAY))
            .appendSpace()
            .append(
                text(content)
                    .clickEvent(
                        ClickEvent.runCommand(
                            "tp "
                                + location.getX()
                                + " "
                                + location.getY()
                                + " "
                                + location.getZ()))
                    .hoverEvent(showText(text("Cliquez pour vous téléporter à cette action"))))
            .build();
    for (final Profile profile :
        this.profileService.getProfiles(
            profile -> profile.hasComponent(SpectatorComponent.class))) {
      profile.sendMessage(message);
    }
  }
}
