package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import lombok.*;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class DamageImmunityComponent implements Component {

    private Set<DamageImmunity> immunities = ConcurrentHashMap.newKeySet();

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static final class DamageImmunity {

        private final EntityDamageEvent.DamageCause cause;
        private int timeLeft = -1;
    }
}
