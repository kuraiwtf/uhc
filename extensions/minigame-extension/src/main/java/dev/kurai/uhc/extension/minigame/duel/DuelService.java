package dev.kurai.uhc.extension.minigame.duel;

import com.google.common.collect.Lists;
import dev.kurai.uhc.extension.minigame.duel.instance.DuelInstance;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static dev.kurai.uhc.util.CC.prefix;

public final class DuelService {

    private final BukkitAudiences audiences;
    private final Collection<DuelRequest> requests = Lists.newArrayList();
    private final Collection<DuelInstance> instances = Lists.newArrayList();

    public DuelService(final BukkitAudiences audiences) {
        this.audiences = audiences;
    }

    public void sendRequest(final Player sender, final Player receiver, final DuelType duelType) {
        this.requests.add(new DuelRequest(sender.getUniqueId(), receiver.getUniqueId(), duelType, Instant.now()));

        sender.sendMessage(prefix("Vous avez envoyé une demande de duel à §b%s §fen §6%s§f."
                .formatted(receiver.getName(), duelType.getName())));
        receiver.sendMessage(prefix("Vous avez reçu une demande de la part de §b%s §fen §6%s§f."
                .formatted(sender.getName(), duelType.getName())));

        final Audience audience = this.audiences.player(receiver);
        audience.sendMessage(Component.text()
                .append(Component.text("UHC", NamedTextColor.YELLOW, TextDecoration.BOLD))
                .appendSpace()
                .append(Component.text(CC.BAR, NamedTextColor.GRAY, TextDecoration.BOLD))
                .appendSpace()
                .append(Component.text(
                                "Cliquez-ici pour accepter la demande.",
                                NamedTextColor.GREEN,
                                TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/duel-accept %s".formatted(sender.getName())))
                        .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour accepter la demande de duel."))))
                .build());
    }

    public void acceptRequest(final Player sender, final Player receiver) {
        final DuelRequest request = this.findRequest(sender.getUniqueId(), receiver.getUniqueId());
        if (request == null) {
            return;
        }

        if (this.instances.stream().anyMatch(instance -> instance.involves(sender.getUniqueId()))) {
            receiver.sendMessage(prefix("§4" + sender.getName() + "§c est déjà en duel."));
            return;
        }

        this.requests.remove(request);

        sender.sendMessage(prefix("§6" + receiver.getName() + "§f a accepté votre demande."));
        receiver.sendMessage(prefix("Vous avez accepté la demande de §6" + sender.getName() + "§f."));

        final DuelType duelType = request.duelType();
        final DuelInstance instance = duelType.instanceFactory().apply(request);
        if (instance == null) {
            final String message = prefix("Impossible de lancer une partie en §6 " + duelType.getName() + "§f.");
            sender.sendMessage(message);
            receiver.sendMessage(message);
            return;
        }

        this.instances.add(instance);
        instance.start(this.instances::remove);
    }

    public void forfeit(final UUID player) {
        for (final DuelInstance instance : List.copyOf(this.instances)) {
            instance.forfeit(player);
        }
    }

    private @Nullable DuelRequest findRequest(final UUID sender, final UUID receiver) {
        this.invalidate();
        return this.requests.stream()
                .filter(duelRequest -> duelRequest.sender().equals(sender)
                        && duelRequest.receiver().equals(receiver)
                        && !duelRequest.expired())
                .findFirst()
                .orElse(null);
    }

    public boolean hasRequest(final UUID sender, final UUID receiver) {
        this.invalidate();
        return this.findRequest(sender, receiver) != null;
    }

    private void invalidate() {
        this.requests.removeIf(DuelRequest::expired);
    }
}
