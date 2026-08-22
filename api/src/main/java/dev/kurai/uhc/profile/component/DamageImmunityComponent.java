package dev.kurai.uhc.profile.component;

import com.google.common.collect.Sets;
import dev.kurai.uhc.ecs.component.Component;
import java.util.Set;
import lombok.*;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class DamageImmunityComponent implements Component {

  private Set<DamageImmunity> immunities = Sets.newHashSet();

  @Getter
  @Setter
  @RequiredArgsConstructor
  @AllArgsConstructor
  public static final class DamageImmunity {

    private final EntityDamageEvent.DamageCause cause;
    private int timeLeft = -1;
  }
}
