package dev.kurai.uhc.module.command;

import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.module.camp.module.CampModule;
import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.module.RoleModule;
import dev.kurai.uhc.nickname.NicknameValue;
import dev.kurai.uhc.nickname.NicknameValueType;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class RoleModuleCommands {

  static final String MARK_KEY = "role_add";

  private static final int SUFFIX_LIMIT = 16;

  private record Entry(String name, @Nullable Class<? extends AbstractCamp<?>> camp) {}

  private record Group(String title, TextColor color, List<String> entries) {}

  private final UltraHardcoreAPI ultraHardcore;
  private final RoleModule<?, ?, ?> module;

  public RoleModuleCommands(
      final UltraHardcoreAPI ultraHardcore, final RoleModule<?, ?, ?> module) {
    this.ultraHardcore = ultraHardcore;
    this.module = module;
  }

  @SubCommand(@CommandMeta(name = "me", aliases = "role", description = "Voir votre rôle"))
  public void role(final Player player) {
    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    this.module
        .roleOf(player.getUniqueId())
        .ifPresentOrElse(
            role -> role.description().forEach(profile::sendMessage),
            () -> profile.sendPrefixedMessage("§cVous n'avez pas de rôle."));
  }

  @SubCommand(@CommandMeta(name = "roles", description = "Voir les rôles en vie"))
  public void roles(final Player player) {
    final var gameService = this.ultraHardcore.gameService();
    if (gameService.startTime() <= 0) {
      this.compo(player);
      return;
    }

    if (this.module.hiddenComposition()) {
      player.sendMessage(CC.prefix("&cLa composition de la partie est cachée."));
      return;
    }

    final var timer = gameService.timerService().getTimer("roles").orElse(null);
    if (timer == null || timer.isRunning()) {
      this.compo(player);
      return;
    }

    final var entries = new ArrayList<Entry>();
    final var playing = this.ultraHardcore.profileService().getPlayingProfiles();
    for (final var profile : playing) {
      this.module
          .roleOf(profile.getId())
          .ifPresent(
              role ->
                  entries.add(
                      this.roleData(role)
                          .map(data -> new Entry(data.getName(), data.getCampClass()))
                          .orElseGet(() -> new Entry(role.getName(), null))));
    }

    this.send(player, "Rôles en vie", playing.size(), entries);
  }

  @SubCommand(@CommandMeta(name = "compo", description = "Voir la composition de la partie"))
  public void compo(final Player player) {
    if (this.module.hiddenComposition()) {
      player.sendMessage(CC.prefix("&cLa composition de la partie est cachée."));
      return;
    }

    final var entries =
        this.roleDatas().stream()
            .map(data -> new Entry(data.getName(), data.getCampClass()))
            .toList();
    this.send(player, "Composition de la partie", entries.size(), entries);
  }

  @SubCommand(
      @CommandMeta(
          name = "set-role",
          aliases = {"setrole", "forcerole", "meta"},
          description = "Définir le rôle d'un joueur",
          permission = "uhc.command.setrole"))
  public void setRole(
      final Player player,
      final @Argument(name = "rôle") AbstractRoleData<?> roleData,
      final @Argument(name = "joueur", defaultValue = "self") Player target) {
    if (!this.module.forceRole(target.getUniqueId(), roleData)) {
      player.sendMessage(CC.prefix("&cCe jeu ne permet pas de définir le rôle d'un joueur."));
      return;
    }

    player.sendMessage(
        CC.prefix(
            "&6%s&r possède désormais le rôle&6 %s&r."
                .formatted(target.getName(), roleData.getName())));
  }

  void mark(final Player player, final Player target, final AbstractRoleData<?> roleData) {
    this.mark(player, target, roleData, this.campData(roleData.getCampClass()));
  }

  void mark(
      final Player player,
      final Player target,
      final AbstractRoleData<?> roleData,
      final @Nullable AbstractCampData<?> camp) {
    final var holder = this.ultraHardcore.nicknameService().holder(player.getUniqueId());
    final var nickname = holder.nickname(target.getUniqueId());
    nickname.removeValue(MARK_KEY);

    final var color = camp == null ? ChatColor.WHITE : camp.getChatColor();
    final var separator = " §8" + BAR + " " + color;
    final var name =
        roleData.getName().substring(0, Math.min(roleData.getName().length(), SUFFIX_LIMIT - separator.length()));
    nickname.addValue(new NicknameValue(MARK_KEY, NicknameValueType.SUFFIX, text(separator + name)));
    holder.updateNickname(target.getUniqueId());
  }

  java.util.Optional<? extends AbstractRoleData<?>> roleData(final AbstractRole<?> role) {
    return this.module.getRoleRegistrar().getRepository().findAll().stream()
        .filter(data -> data.getId().equals(role.getClass()))
        .findFirst();
  }

  private List<? extends AbstractRoleData<?>> roleDatas() {
    return List.copyOf(this.module.getEnabledRoles());
  }

  private List<? extends AbstractCampData<?>> campDatas() {
    if (!(this.module instanceof final CampModule<?, ?, ?> campModule)) {
      return List.of();
    }

    return campModule.getCampRegistrar().getRegistry().findAll().stream()
        .sorted(Comparator.comparing(AbstractCampData::getName))
        .toList();
  }

  @Nullable AbstractCampData<?> campData(final @Nullable Class<?> campClass) {
    if (campClass == null) {
      return null;
    }

    return this.campDatas().stream()
        .filter(data -> data.getId().equals(campClass))
        .findFirst()
        .orElse(null);
  }

  private List<Group> group(final List<Entry> entries) {
    final var groups = new ArrayList<Group>();
    final var remaining = new ArrayList<>(entries);

    for (final var camp : this.campDatas()) {
      final var names =
          remaining.stream()
              .filter(entry -> camp.getId().equals(entry.camp()))
              .map(Entry::name)
              .sorted()
              .toList();
      remaining.removeIf(entry -> camp.getId().equals(entry.camp()));
      if (!names.isEmpty()) {
        groups.add(new Group(camp.getName(), camp.getTextColor(), names));
      }
    }

    if (!remaining.isEmpty()) {
      groups.add(
          new Group(
              groups.isEmpty() ? "Rôles" : "Autres",
              GOLD,
              remaining.stream().map(Entry::name).sorted().toList()));
    }

    return groups;
  }

  private void send(
      final Player player, final String title, final int total, final List<Entry> entries) {
    final var message =
        text()
            .append(line(GOLD, YELLOW))
            .appendNewline()
            .appendNewline()
            .append(
                text()
                    .appendSpace()
                    .append(text(BIG_SQUARE, GOLD))
                    .appendSpace()
                    .append(text(title).decorate(BOLD))
                    .appendSpace()
                    .append(text('(', DARK_GRAY))
                    .append(text(total, GOLD, BOLD))
                    .append(text(')', DARK_GRAY))
                    .build())
            .appendNewline()
            .appendNewline();

    for (final var group : this.group(entries)) {
      message.append(this.render(group));
    }

    message.append(line(GOLD, YELLOW));
    this.ultraHardcore
        .profileService()
        .getOrCreateProfile(player.getUniqueId())
        .sendMessage(message.build());
  }

  private Component render(final Group group) {
    if (group.entries().isEmpty()) {
      return empty();
    }

    final var message =
        text()
            .appendSpace()
            .append(text(BAR, group.color()))
            .appendSpace()
            .append(text(group.title(), group.color(), BOLD))
            .appendSpace()
            .append(text('(', DARK_GRAY))
            .append(text(group.entries().size(), group.color(), BOLD))
            .append(text(')', DARK_GRAY))
            .appendNewline();

    for (final var name : group.entries()) {
      message.append(
          text()
              .appendSpace()
              .appendSpace()
              .append(text(SQUARE, group.color()))
              .appendSpace()
              .append(text(name, group.color()))
              .appendNewline()
              .build());
    }

    return message.appendNewline().build();
  }
}
