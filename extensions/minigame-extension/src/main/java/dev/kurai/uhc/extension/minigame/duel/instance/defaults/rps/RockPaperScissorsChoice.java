package dev.kurai.uhc.extension.minigame.duel.instance.defaults.rps;

import org.bukkit.Material;

public enum RockPaperScissorsChoice {
    ROCK(Material.STONE, "Pierre") {
        @Override
        public RockPaperScissorsChoice weakTo() {
            return PAPER;
        }
    },
    PAPER(Material.PAPER, "Feuille") {
        @Override
        public RockPaperScissorsChoice weakTo() {
            return SCISSORS;
        }
    },
    SCISSORS(Material.SHEARS, "Ciseaux") {
        @Override
        public RockPaperScissorsChoice weakTo() {
            return ROCK;
        }
    },
    ;

    private final Material icon;
    private final String name;

    RockPaperScissorsChoice(final Material icon, final String name) {
        this.icon = icon;
        this.name = name;
    }

    public Material icon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public abstract RockPaperScissorsChoice weakTo();
}
