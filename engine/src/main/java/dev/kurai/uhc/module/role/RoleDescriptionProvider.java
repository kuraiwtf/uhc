package dev.kurai.uhc.module.role;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.camp.Camp;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.command.AbstractCommandPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.module.power.defaults.passive.AbstractPassivePower;
import dev.kurai.uhc.profile.Profile;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static dev.kurai.uhc.util.CC.center;
import static dev.kurai.uhc.util.CC.line;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@RequiredArgsConstructor
public final class RoleDescriptionProvider {

    private final UltraHardcoreAPI ultraHardcore;

    public void sendDescription(final AbstractRole role) {
        final Profile profile = role.profile;
        final TextComponent.Builder component = text();
        final AbstractModule module = this.ultraHardcore.moduleService().getCurrentModule();
        final Camp camp = role.roleMeta.camp();
        component
                .append(line(GOLD, YELLOW))
                .appendNewline()
                .appendNewline()
                .append(center(text(
                        module.getName().toUpperCase(Locale.ROOT),
                        module.color().asTextColor(),
                        BOLD)))
                .appendNewline()
                .appendNewline()
                .append(this.section("Informations"))
                .appendNewline()
                .appendNewline()
                .append(this.entry("Votre rôle", camp.primaryColor() + role.roleMeta.getName()))
                .appendNewline()
                .append(this.entry("Objectif", camp.objective()))
                .appendNewline()
                .appendNewline();

        final List<AbstractPower> itemPowers = new ArrayList<>();
        final List<AbstractPower> particularPowers = new ArrayList<>();
        for (final AbstractPower power : role.getPowers()) {
            if (!power.roleDescriptionDisplay()) {
                continue;
            }

            if (power instanceof AbstractItemPower) {
                itemPowers.add(power);
            } else if (power instanceof AbstractPassivePower || power instanceof AbstractCommandPower) {
                particularPowers.add(power);
            }
        }

        if (!role.getPassives().isEmpty()
                || !role.getParticularities().isEmpty()
                || !particularPowers.isEmpty()
                || !role.getOtherParticularities().isEmpty()) {
            component.append(this.section("Particularités")).appendNewline().appendNewline();
        }

        if (!role.getPassives().isEmpty()) {
            component
                    .append(this.entry("Passifs", role.getPassives()))
                    .appendNewline()
                    .appendNewline();
        }

        for (final var entry : role.getParticularities().entrySet()) {
            component
                    .append(this.entry(entry.getKey(), String.join("\n", entry.getValue())))
                    .appendNewline();
        }

        for (final AbstractPower power : particularPowers) {
            if (power instanceof final AbstractCommandPower commandPower) {
                final String moduleCommand = module.getCommandName();
                final String command = moduleCommand == null
                        ? commandPower.getCommandName()
                        : moduleCommand + " " + commandPower.getCommandName();
                component
                        .appendSpace()
                        .appendSpace()
                        .append(text('▪', DARK_GRAY))
                        .appendSpace()
                        .append(text(power.getName(), GRAY))
                        .appendSpace()
                        .append(text('(', DARK_GRAY))
                        .append(text('/', GOLD))
                        .append(text(command, YELLOW))
                        .append(text(')', DARK_GRAY));
            } else {
                component
                        .appendSpace()
                        .appendSpace()
                        .append(text('▪', DARK_GRAY))
                        .appendSpace()
                        .append(text(power.getName(), GRAY));
            }

            final var rawLore = power.lore().stream().map(Component::text).toList();
            final TextComponent.Builder powerDescription = text();
            for (final TextComponent textComponent : rawLore) {
                powerDescription.append(textComponent).appendNewline();
            }

            component
                    .append(text(':', GRAY))
                    .appendSpace()
                    .append(powerDescription)
                    .appendNewline();
        }

        for (final var entry : role.getOtherParticularities().entrySet()) {
            component
                    .append(this.hoverableLine(entry.getKey(), entry.getValue()))
                    .appendNewline();
        }

        if (!itemPowers.isEmpty()) {
            component
                    .appendNewline()
                    .append(this.section("Pouvoirs"))
                    .appendNewline()
                    .appendNewline();
        }

        for (final AbstractPower power : itemPowers) {
            component.append(this.powerLine(power, power.getName(), '▪', 2)).appendNewline();

            if (power instanceof final AbstractParentItemPower parentPower) {
                for (final AbstractItemPower itemPower : parentPower.getChildren()) {
                    component
                            .append(this.powerLine(itemPower, itemPower.getName(), '•', 3))
                            .appendNewline();
                }
            }
        }

        component.appendNewline().append(line(GOLD, YELLOW));
        profile.sendMessage(component.build());
    }

    private Component hoverableLine(final String name, final List<String> entries) {
        final TextComponent.Builder line = text();
        for (int i = 0; i < 2; i++) {
            line.appendSpace();
        }

        final TextComponent.Builder powerDescription = text();
        powerDescription
                .append(text('»', DARK_GRAY))
                .appendSpace()
                .append(text(name))
                .appendSpace()
                .append(text('«', DARK_GRAY))
                .appendNewline()
                .appendNewline();

        final var rawLore = entries.stream().map(Component::text).toList();
        for (final TextComponent textComponent : rawLore) {
            powerDescription.append(textComponent).appendNewline();
        }

        return line.append(text('▪', DARK_GRAY))
                .appendSpace()
                .append(text('[', DARK_GRAY))
                .append(text('☰', YELLOW))
                .appendSpace()
                .append(text(name).hoverEvent(showText(powerDescription)))
                .append(text(']', DARK_GRAY))
                .build();
    }

    private Component powerLine(final AbstractPower power, final String label, final char bullet, final int indent) {
        final TextComponent.Builder line = text();
        for (int i = 0; i < indent; i++) {
            line.appendSpace();
        }

        return line.append(text(bullet, DARK_GRAY))
                .appendSpace()
                .append(text('[', DARK_GRAY))
                .append(text('☰', YELLOW))
                .appendSpace()
                .append(text(label, power.getColor().asTextColor(), BOLD)
                        .hoverEvent(showText(this.powerDescription(power))))
                .append(text(']', DARK_GRAY))
                .build();
    }

    private Component powerDescription(final AbstractPower power) {
        final TextComponent.Builder powerDescription = text();
        powerDescription
                .append(text('»', DARK_GRAY))
                .appendSpace()
                .append(text(power.getName(), power.getColor().asTextColor(), BOLD))
                .appendSpace()
                .append(text('«', DARK_GRAY))
                .appendNewline()
                .appendNewline();

        final var rawLore = power.lore().stream().map(Component::text).toList();
        for (final TextComponent textComponent : rawLore) {
            powerDescription.append(textComponent).appendNewline();
        }

        return powerDescription.build();
    }

    private Component section(final String title) {
        return text().appendSpace()
                .append(text('┃', GOLD, BOLD))
                .appendSpace()
                .append(text(title, WHITE, BOLD))
                .build();
    }

    private Component entry(final String key, final String value) {
        return text().appendSpace()
                .appendSpace()
                .append(text('▪', DARK_GRAY))
                .appendSpace()
                .append(text(key, GRAY))
                .append(text(':', GRAY))
                .appendSpace()
                .append(text(value == null ? "Aucun" : value))
                .build();
    }
}
