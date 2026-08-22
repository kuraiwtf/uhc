package dev.kurai.uhc.module.service;

import dev.kurai.uhc.module.AbstractModule;

public interface ModuleService {

  void installModule(final AbstractModule module);

  AbstractModule getCurrentModule();
}
