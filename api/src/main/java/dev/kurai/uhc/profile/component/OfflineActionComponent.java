package dev.kurai.uhc.profile.component;

import com.google.common.collect.Queues;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.profile.action.OfflinePlayerAction;
import java.util.Queue;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OfflineActionComponent implements Component {

  private final Queue<OfflinePlayerAction> actions;

  public OfflineActionComponent() {
    this.actions = Queues.newSynchronousQueue();
  }

  public OfflineActionComponent(final Queue<OfflinePlayerAction> actions) {
    this.actions = actions;
  }

  public Queue<OfflinePlayerAction> getActions() {
    return this.actions;
  }
}
