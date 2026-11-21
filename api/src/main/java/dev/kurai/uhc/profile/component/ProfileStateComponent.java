package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.profile.state.ProfileState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public final class ProfileStateComponent implements Component {

    private ProfileState state;
}
