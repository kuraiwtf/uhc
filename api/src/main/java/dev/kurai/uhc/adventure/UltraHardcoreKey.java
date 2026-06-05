package dev.kurai.uhc.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
@UtilityClass
public final class UltraHardcoreKey {

  public final @KeyPattern.Namespace String NAMESPACE = "uhc";

  public Key key(final @KeyPattern.Value String key) {
    return Key.key(NAMESPACE, key);
  }
}
