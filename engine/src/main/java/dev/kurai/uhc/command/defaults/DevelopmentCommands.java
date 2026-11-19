package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class DevelopmentCommands {

    private static final String TEST_DISABLED = CC.prefix("§cLa partie n'est pas en mode test.");

    private final UltraHardcoreAPI ultraHardcore;

    @Command(@CommandMeta(name = "test", permission = "uhc.command.test"))
    public void test(final Player player) {
        final GameService gameService = this.ultraHardcore.gameService();
        gameService.testMode(!gameService.testMode());

        player.sendMessage(CC.prefix(
                "Vous venez " + (gameService.testMode() ? "d'§aactiver" : "de§c désactiver") + "§f le mode test."));
    }

    @Command(@CommandMeta(name = "resetcd"))
    public void resetCooldown(
            final Player player, final @Argument(name = "joueur", defaultValue = "self") Player target) {
        if (!this.ultraHardcore.gameService().testMode()) {
            player.sendMessage(TEST_DISABLED);
            return;
        }

        final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
        for (final AbstractPower power : profile.getPowers()) {
            power.findOptionalRestriction(CooldownPowerRestriction.class, "cooldown")
                    .ifPresent(restriction -> restriction.timeLeft(0));
        }

        player.sendMessage(
                CC.prefix("Vous venez de&c réinitialiser&r les&b délais&r de&6 %s&r.".formatted(target.getName())));
    }
}
