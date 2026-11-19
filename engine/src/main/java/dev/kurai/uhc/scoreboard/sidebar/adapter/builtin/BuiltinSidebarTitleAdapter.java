package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.scoreboard.sidebar.SidebarTitleAdapter;
import dev.kurai.uhc.util.CC;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@RequiredArgsConstructor
public final class BuiltinSidebarTitleAdapter implements SidebarTitleAdapter {

    private final UltraHardcoreAPI ultraHardcore;

    @Override
    public Component provideTitle(final Player player) {
        if (this.ultraHardcore.gameService().testMode()) {
            return text().append(text(CC.DANGER, DARK_RED))
                    .appendSpace()
                    .append(text("DEVELOPPEMENT", RED, BOLD))
                    .appendSpace()
                    .append(text(CC.DANGER, DARK_RED))
                    .build();
        }

        final AbstractModule module = this.ultraHardcore.moduleService().getCurrentModule();
        return text(module.getName(), module.color().asTextColor(), BOLD);
    }
}
