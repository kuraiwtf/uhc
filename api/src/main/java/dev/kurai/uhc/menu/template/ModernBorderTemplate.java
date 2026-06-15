package dev.kurai.uhc.menu.template;

import dev.kurai.uhc.menu.button.GlassButton;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.template.Template;
import org.jetbrains.annotations.NotNull;

public final class ModernBorderTemplate implements Template {

  private final int primaryColour;
  private final int secondaryColour;

  public ModernBorderTemplate(final int primaryColour, final int secondaryColour) {
    this.primaryColour = primaryColour;
    this.secondaryColour = secondaryColour;
  }

  @Override
  public void apply(final  BackgroundLayer background, final ForegroundLayer foreground) {
    final var size = background.getMenu().getTotalSlots();
    for (final int i :
        new int[] {
          0, 1, 7, 8, 9, 17, size - 18, size - 10, size - 9, size - 8, size - 2, size - 1
        }) {
      background.set(i, new GlassButton(this.primaryColour));
    }

    for (final int i :
        new int[] {
          2, 3, 5, 6, 10, 16, size - 17, size - 11, size - 6, size - 7, size - 4, size - 3
        }) {
      background.set(i, new GlassButton(this.secondaryColour));
    }
  }
}
