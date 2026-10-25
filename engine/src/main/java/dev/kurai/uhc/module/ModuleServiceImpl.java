package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.module.ModuleInstallEvent;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.command.annotation.SimpleCommandMeta;
import dev.kurai.uhc.module.color.ColorModule;
import dev.kurai.uhc.module.command.ColorModuleCommands;
import dev.kurai.uhc.module.command.ModuleCommandProvider;
import dev.kurai.uhc.module.command.RoleColorModuleCommands;
import dev.kurai.uhc.module.command.RoleModuleCommands;
import dev.kurai.uhc.module.role.RoleAttributionTimer;
import dev.kurai.uhc.module.role.RoleModule;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ModuleServiceImpl implements ModuleService {

  private static final Logger LOGGER = Logger.getLogger(ModuleServiceImpl.class.getSimpleName());

  private final UltraHardcoreAPI ultraHardcore;
  private final List<ModuleCommandProvider> commandProviders = new ArrayList<>();
  private AbstractModule currentModule;

  public ModuleServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.currentModule = new BuiltinModule(ultraHardcore);

    this.registerCommandProvider(
        module ->
            module instanceof final RoleModule roleModule
                ? new RoleModuleCommands(ultraHardcore, roleModule)
                : null);
    this.registerCommandProvider(
        module -> module instanceof ColorModule ? new ColorModuleCommands(ultraHardcore) : null);
    this.registerCommandProvider(
        module ->
            module instanceof final RoleModule roleModule && module instanceof ColorModule
                ? new RoleColorModuleCommands(ultraHardcore, roleModule)
                : null);
  }

  @Override
  public void registerCommandProvider(final ModuleCommandProvider provider) {
    this.commandProviders.add(provider);
  }

  private void registerModuleCommand(final AbstractModule module) {
    final var commandName = module.getCommandName();
    if (commandName == null) {
      return;
    }

    final var handlers = new ArrayList<>(module.commandHandlers());
    for (final var provider : this.commandProviders) {
      final var handler = provider.provide(module);
      if (handler != null) {
        handlers.add(handler);
      }
    }

    this.ultraHardcore
        .commandRegistrar()
        .registerCommand(
            SimpleCommandMeta.of(commandName, "Commandes du jeu " + module.getName()),
            handlers.toArray());
  }

  @Override
  public void installModule(final AbstractModule module) {
    final var event =
        this.ultraHardcore
            .eventService()
            .dispatchEvent(new ModuleInstallEvent(this.currentModule, module));
    if (event.isCancelled()) {
      LOGGER.warning("Module %s installation cancelled.".formatted(module.getName()));
      return;
    }

    final var previousCommand = this.currentModule.getCommandName();
    if (previousCommand != null) {
      this.ultraHardcore.commandRegistrar().unregisterCommand(previousCommand);
    }

    this.currentModule = module;
    this.registerModuleCommand(module);

    final var timerService = this.ultraHardcore.gameService().timerService();
    timerService.unregisterTimer(RoleAttributionTimer.IDENTIFIER);
    if (module instanceof RoleModule) {
      timerService.registerTimer(new RoleAttributionTimer(this.ultraHardcore));
    }
  }

  @Override
  public AbstractModule getCurrentModule() {
    return this.currentModule;
  }
}
