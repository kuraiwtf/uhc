package dev.kurai.uhc.actionbar;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ActionbarServiceImpl implements ActionbarService {

  private final Map<@NotNull UUID, @NotNull Actionbar> actionbars;

  public ActionbarServiceImpl() {
    this.actionbars = Maps.newConcurrentMap();
  }

  @Override
  public @NotNull Actionbar getActionbar(final @NotNull UUID uniqueId) {
    return this.actionbars.computeIfAbsent(uniqueId, _ -> new ActionbarImpl(uniqueId));
  }
}
