package dev.kurai.uhc.extension.minigame.duel.instance.defaults.rps;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.extension.minigame.duel.instance.DuelInstance;
import dev.kurai.uhc.extension.minigame.duel.type.DuelType;
import net.j4c0b3y.api.menu.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class RockPaperScissorsInstance extends DuelInstance {

    private static final int TIME_LEFT = 10;

    private final Map<UUID, RockPaperScissorsChoice> choices;
    private final Map<UUID, Menu> menus;

    private int timeLeft;
    private BukkitTask countdown;
    private @Nullable RockPaperScissorsChoice winningChoice;

    public RockPaperScissorsInstance(final UUID first, final UUID second) {
        super(first, second, DuelType.ROCK_PAPER_SCISSORS);
        this.choices = Maps.newHashMap();
        this.menus = Maps.newHashMap();
    }

    @Override
    public void onStart() {
        this.menus.put(this.first.getUniqueId(), new RockPaperScissorsMenu(this.first, this));
        this.menus.put(this.second.getUniqueId(), new RockPaperScissorsMenu(this.second, this));

        this.timeLeft = TIME_LEFT;

        for (final Menu menu : this.menus.values()) {
            menu.open();
        }

        this.countdown = new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft--;
                for (final Player player : players()) {
                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
                }

                if (timeLeft == 0) {
                    announceWinner();
                    stop();
                }
            }
        }.runTaskTimer(UltraHardcoreAPI.getInstance().plugin(), 20, 20);
    }

    @Override
    public void update() {
        for (final Menu menu : this.menus.values()) {
            menu.update();
        }
    }

    @Override
    public void onStop() {
        this.countdown.cancel();

        this.showResultThenClose(this.menus.values());
        this.menus.clear();
    }

    private void announceWinner() {
        final UUID winner = this.determinateWinner();
        if (winner == null) {
            this.sendMessage(miniMessage()
                    .deserialize("<b><gold>PFC</gold></b> <b><gray>❘</gray></b> <gold>Égalité</gold> ! Aucun joueur"
                            + " n'a remporté la partie."));
        } else {
            this.winningChoice = this.choices.get(winner);
            this.sendMessage(miniMessage()
                    .deserialize(
                            "<b><gold>PFC</gold></b> <b><gray>❘</gray></b> Le joueur <green><player></green> a"
                                    + " remporté la partie !",
                            Placeholder.component(
                                    "player",
                                    Component.text(Bukkit.getPlayer(winner).getName()))));
        }
    }

    private UUID determinateWinner() {
        final RockPaperScissorsChoice firstChoice = this.choices.get(this.first.getUniqueId());
        final RockPaperScissorsChoice secondChoice = this.choices.get(this.second.getUniqueId());

        if (firstChoice == null) {
            if (secondChoice != null) {
                return this.second.getUniqueId();
            }
            return null;
        } else if (secondChoice == null) {
            return this.first.getUniqueId();
        }

        if (firstChoice.weakTo() == secondChoice) {
            return this.second.getUniqueId();
        }

        if (secondChoice.weakTo() == firstChoice) {
            return this.first.getUniqueId();
        }

        return null;
    }

    public int timeLeft() {
        return this.timeLeft;
    }

    public void choice(final UUID uniqueId, final RockPaperScissorsChoice choice) {
        if (!this.finished) {
            this.choices.put(uniqueId, choice);
        }
    }

    public @Nullable RockPaperScissorsChoice winningChoice() {
        return this.winningChoice;
    }

    public @Nullable RockPaperScissorsChoice choice(final UUID uniqueId) {
        return this.choices.get(uniqueId);
    }
}
