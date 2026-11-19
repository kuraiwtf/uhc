package dev.kurai.uhc.profile.component;

import com.google.common.collect.Queues;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.profile.action.OfflinePlayerAction;
import org.jspecify.annotations.NullMarked;

import java.util.Queue;

@NullMarked
public record OfflineActionComponent(Queue<OfflinePlayerAction> actions) implements Component {

    public OfflineActionComponent() {
        this(Queues.newArrayDeque());
    }
}
