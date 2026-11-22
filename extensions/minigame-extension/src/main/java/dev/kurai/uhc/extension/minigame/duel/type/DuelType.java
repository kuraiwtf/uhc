package dev.kurai.uhc.extension.minigame.duel.type;

import dev.kurai.uhc.extension.minigame.duel.DuelRequest;
import dev.kurai.uhc.extension.minigame.duel.instance.DuelInstance;
import dev.kurai.uhc.extension.minigame.duel.instance.defaults.fourinarow.FourInARowInstance;
import dev.kurai.uhc.extension.minigame.duel.instance.defaults.rps.RockPaperScissorsInstance;
import dev.kurai.uhc.extension.minigame.duel.instance.defaults.tictactoe.TicTacToeInstance;
import org.bukkit.Material;

import java.util.function.Function;

public enum DuelType {
    FOUR_IN_A_ROW(
            "Puissance 4",
            Material.DIAMOND,
            duelRequest -> new FourInARowInstance(duelRequest.sender(), duelRequest.receiver())),
    TIC_TAC_TOE(
            "Morpion",
            Material.IRON_INGOT,
            duelRequest -> new TicTacToeInstance(duelRequest.sender(), duelRequest.receiver())),
    ROCK_PAPER_SCISSORS(
            "Pierre Feuille Ciseaux",
            Material.SHEARS,
            duelRequest -> new RockPaperScissorsInstance(duelRequest.sender(), duelRequest.receiver())),
    ;

    private final String name;
    private final Material icon;
    private final Function<DuelRequest, DuelInstance> instanceFactory;

    DuelType(final String name, final Material icon, final Function<DuelRequest, DuelInstance> instanceFactory) {
        this.name = name;
        this.icon = icon;
        this.instanceFactory = instanceFactory;
    }

    public String getName() {
        return name;
    }

    public Material icon() {
        return icon;
    }

    public Function<DuelRequest, DuelInstance> instanceFactory() {
        return instanceFactory;
    }
}
