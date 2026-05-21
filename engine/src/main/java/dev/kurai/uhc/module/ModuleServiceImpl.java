package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.module.ModuleInstallEvent;
import dev.kurai.uhc.module.camp.module.CampModule;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.module.team.module.TeamModule;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public final class ModuleServiceImpl implements ModuleService {

  private static final Logger LOGGER = Logger.getLogger(ModuleServiceImpl.class.getSimpleName());

  private final UltraHardcoreAPI ultraHardcore;
  private AbstractModule currentModule;

  public ModuleServiceImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.currentModule = new BuiltinModule(ultraHardcore);
  }

  @Override
  public void installModule(final @NotNull AbstractModule module) {
    if (module instanceof TeamModule && module instanceof CampModule) {
      LOGGER.warning(
          "Module %s is not compatible with this game mode.".formatted(module.getName()));
      return;
    }

    final var event =
        this.ultraHardcore
            .eventService()
            .dispatchEvent(new ModuleInstallEvent(this.currentModule, module));
    if (event.isCancelled()) {
      LOGGER.warning("Module %s installation cancelled.".formatted(module.getName()));
      return;
    }

    this.currentModule = module;
  }

  @Override
  public @NotNull AbstractModule getCurrentModule() {
    return this.currentModule;
  }
}
