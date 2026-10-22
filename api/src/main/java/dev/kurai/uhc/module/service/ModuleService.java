package dev.kurai.uhc.module.service;

import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.command.ModuleCommandProvider;

public interface ModuleService {

  void installModule(final AbstractModule module);

  AbstractModule getCurrentModule();

  void registerCommandProvider(final ModuleCommandProvider provider);
}
