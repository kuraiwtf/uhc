package dev.kurai.uhc.module.command;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.module.RoleModule;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.api.annotation.Identifier;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class RoleDataArgumentResolver implements ArgumentResolver<@Nullable AbstractRoleData<?>> {

  private final ModuleService moduleService;

  public RoleDataArgumentResolver(final ModuleService moduleService) {
    this.moduleService = moduleService;
  }

  private Collection<? extends AbstractRoleData<?>> roles() {
    return this.moduleService.getCurrentModule() instanceof final RoleModule<?, ?, ?> module
        ? List.copyOf(module.getEnabledRoles())
        : List.of();
  }

  private static String compact(final String name) {
    return name.replace(" ", "");
  }

  @Override
  public @Nullable AbstractRoleData<?> resolve(final CommandSender sender, final String argument) {
    final var found =
        this.roles().stream()
            .filter(
                data -> {
                  final var identifier = data.getId().getAnnotation(Identifier.class);
                  return (identifier != null && identifier.value().equalsIgnoreCase(argument))
                      || compact(data.getName()).equalsIgnoreCase(argument);
                })
            .findFirst()
            .orElse(null);

    if (found == null) {
      sender.sendMessage(CC.colorize("&cLe rôle &4" + argument + "&c est introuvable."));
    }

    return found;
  }

  @Override
  public Collection<String> complete(final CommandSender sender, final String argument) {
    return this.roles().stream()
        .map(data -> compact(data.getName()))
        .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(argument.toLowerCase(Locale.ROOT)))
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
  }
}
