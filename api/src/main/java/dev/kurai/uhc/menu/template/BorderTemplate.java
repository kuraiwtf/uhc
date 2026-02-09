package dev.kurai.uhc.menu.template;

import dev.kurai.uhc.menu.button.GlassButton;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.template.Template;
import org.jetbrains.annotations.NotNull;

public final class BorderTemplate implements Template {

  private final int primaryColour;

  public BorderTemplate(final int primaryColour) {
    this.primaryColour = primaryColour;
  }

  @Override
  public void apply(final @NotNull BackgroundLayer background, final ForegroundLayer foreground) {
    final var size = background.getMenu().getTotalSlots();
    for (int i :
        new int[] {
          0, 1, 7, 8, 9, 17, size - 18, size - 10, size - 9, size - 8, size - 2, size - 1
        }) {
      background.set(i, new GlassButton(this.primaryColour));
    }
  }
}
