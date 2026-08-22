package dev.kurai.uhc.module.component;

import dev.kurai.uhc.ecs.component.Component;
import java.util.Collection;

public record ModuleResourcePackComponent(Collection<ResourcePack> packs) implements Component {

  public record ResourcePack(String url, String hash) {}
}
