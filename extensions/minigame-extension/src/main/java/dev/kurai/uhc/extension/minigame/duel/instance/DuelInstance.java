package dev.kurai.uhc.extension.minigame.duel.instance;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import net.j4c0b3y.api.menu.Menu;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public abstract class DuelInstance implements ForwardingAudience {

    private static final long RESULT_DISPLAY_TICKS = 3 * 20L;

    protected final BukkitAudiences bukkitAudiences;

    protected final Player first;
    protected final Player second;

    protected final DuelType duelType;

    protected boolean finished;

    private @Nullable Consumer<DuelInstance> finishListener;

    protected DuelInstance(final UUID first, final UUID second, final DuelType duelType) {
        this.bukkitAudiences = UltraHardcoreAPI.getInstance().bukkitAudiences();

        this.first = Bukkit.getPlayer(first);
        this.second = Bukkit.getPlayer(second);

        this.duelType = duelType;
    }

    public void start(final Consumer<DuelInstance> finishListener) {
        this.finishListener = finishListener;
        this.onStart();
    }

    public void onStart() {}

    public final void stop() {
        if (!this.finished) {
            this.finished = true;
            this.onStop();

            if (this.finishListener != null) {
                this.finishListener.accept(this);
            }
        }
    }

    public final void forfeit(final UUID player) {
        this.forfeit(player, "s'est déconnecté");
    }

    public final void handleMenuClose(final Player player) {
        if (this.finished) {
            return;
        }

        Bukkit.getScheduler()
                .runTask(
                        UltraHardcoreAPI.getInstance().plugin(),
                        () -> this.forfeit(player.getUniqueId(), "a fermé le menu"));
    }

    private void forfeit(final UUID player, final String reason) {
        if (this.finished) {
            return;
        }

        if (player.equals(this.first.getUniqueId())) {
            this.announceForfeit(this.first, this.second, reason);
        } else if (player.equals(this.second.getUniqueId())) {
            this.announceForfeit(this.second, this.first, reason);
        } else {
            return;
        }

        this.stop();
    }

    private void announceForfeit(final Player loser, final Player winner, final String reason) {
        this.announce(
                "<red><loser></red> <reason>, <green><winner></green> remporte la partie par forfait !",
                Placeholder.unparsed("reason", reason),
                Placeholder.unparsed("loser", loser.getName()),
                Placeholder.unparsed("winner", winner.getName()));
    }

    protected final void announceResult(final @Nullable Player winner) {
        if (winner == null) {
            this.announce("<gold>Égalité</gold> ! Aucun joueur n'a remporté la partie.");
        } else {
            this.announce(
                    "Le joueur <green><player></green> a remporté la partie !",
                    Placeholder.unparsed("player", winner.getName()));
        }
    }

    private void announce(final String message, final TagResolver... placeholders) {
        this.sendMessage(miniMessage()
                .deserialize(
                        "<b><gold><game></gold></b> <b><gray>❘</gray></b> " + message,
                        TagResolver.resolver(
                                Placeholder.unparsed("game", this.duelType.getName()),
                                TagResolver.resolver(placeholders))));
    }

    protected final void showResultThenClose(final Collection<Menu> menus) {
        final List<Menu> openedMenus = List.copyOf(menus);
        for (final Menu menu : openedMenus) {
            menu.update();
        }

        Bukkit.getScheduler()
                .runTaskLater(
                        UltraHardcoreAPI.getInstance().plugin(),
                        () -> {
                            for (final Menu menu : openedMenus) {
                                menu.getHandler().ifOpen(menu.getPlayer(), open -> {
                                    if (open == menu) {
                                        menu.close();
                                    }
                                });
                            }
                        },
                        RESULT_DISPLAY_TICKS);
    }

    public void update() {

    }

    public void onStop() {}

    public Player first() {
        return first;
    }

    public Player second() {
        return second;
    }

    public DuelType duelType() {
        return duelType;
    }

    public boolean finished() {
        return finished;
    }

    public boolean involves(final UUID player) {
        return this.first.getUniqueId().equals(player) || this.second.getUniqueId().equals(player);
    }

    public Collection<Player> players() {
        return List.of(this.first, this.second);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Lists.newArrayList(this.bukkitAudiences.player(this.first), this.bukkitAudiences.player(this.second));
    }
}
