package dev.kurai.uhc.module.power.defaults.passive;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public abstract class AbstractPassivePower extends AbstractPower {

    public AbstractPassivePower(
            final String identifier, final String name, final UUID owner, final UltraHardcoreAPI ultraHardcore) {
        super(identifier, name, owner, ultraHardcore);
    }
}
