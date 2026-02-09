package dev.kurai.uhc.module.service;

import dev.kurai.uhc.module.AbstractModule;
import org.jetbrains.annotations.NotNull;

public interface ModuleService {

  void installModule(final @NotNull AbstractModule module);

  @NotNull
  AbstractModule getCurrentModule();
}
