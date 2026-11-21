package dev.kurai.uhc.module.command;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.module.camp.Camp;
import dev.kurai.uhc.module.camp.CampModule;
import dev.kurai.uhc.module.role.RoleComponent;
import dev.kurai.uhc.module.role.RoleMeta;
import dev.kurai.uhc.module.role.RoleModule;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.Color;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static dev.kurai.uhc.util.CC.MISSING_PERMISSION;

@NullMarked
public final class RoleColorModuleCommands {

    private final UltraHardcoreAPI ultraHardcore;

    private final RoleModule module;
    private final RoleModuleCommands helpers;

    public RoleColorModuleCommands(final UltraHardcoreAPI ultraHardcore, final RoleModule module) {
        this.ultraHardcore = ultraHardcore;
        this.module = module;
        this.helpers = new RoleModuleCommands(ultraHardcore, module);
    }

    @SubCommand(@CommandMeta(name = "add", description = "Assigner un rôle à un joueur"))
    public void add(
            final Player player,
            final @Argument(name = "joueur") Player target,
            final @Argument(name = "role") RoleMeta roleData) {
        this.helpers.mark(player, target, roleData);
        player.sendMessage(CC.prefix("Vous avez marqué que le joueur &6%s&r incarnait le rôle &6%s&r."
                .formatted(target.getName(), roleData.getDefaultName())));
    }

    @SubCommand(@CommandMeta(name = "reset", description = "Retirer le rôle assigné à un joueur"))
    public void reset(final Player player, final @Argument(name = "joueur") Player target) {
        final var holder = this.ultraHardcore.nicknameService().holder(player.getUniqueId());
        holder.nickname(target.getUniqueId()).removeValue(RoleModuleCommands.MARK_KEY);
        holder.updateNickname(target.getUniqueId());

        player.sendMessage(
                CC.prefix("Vous avez retiré le potentiel rôle qu'incarne &6%s&r".formatted(target.getName())));
    }

    @SubCommand(@CommandMeta(name = "auto-color", description = "Colorer automatiquement"))
    public void autoColor(final Player player) {
        final var profileService = this.ultraHardcore.profileService();
        if (!profileService.getOrCreateProfile(player).spectating() && !player.isOp()) {
            player.sendMessage(MISSING_PERMISSION);
            return;
        }

        final var holder = this.ultraHardcore.nicknameService().holder(player.getUniqueId());
        int colored = 0;
        for (final var profile : profileService.getPlayingProfiles()) {
            final RoleComponent component = profile.getComponent(RoleComponent.class);
            if (component == null) {
                continue;
            }

            final var roleData = component.role().roleMeta();
            final var camp = this.module instanceof final CampModule campModule
                    ? campModule.campOf(profile.getId()).<Camp>map(c -> c).orElse(roleData.camp())
                    : roleData.camp();
            holder.applyColor(profile.getId(), Color.of(camp.primaryColor()));

            profile.findPlayer().ifPresent(target -> this.helpers.mark(player, target, roleData, camp));
            colored++;
        }

        player.sendMessage(CC.prefix("&6%d&f joueurs ont été automatiquement &acolorés&f.".formatted(colored)));
    }
}
