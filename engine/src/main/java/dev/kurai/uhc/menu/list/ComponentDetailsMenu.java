package dev.kurai.uhc.menu.list;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ComponentDetailsMenu extends PaginatedMenu {

  private final Component component;

  public ComponentDetailsMenu(final Player player, final Component component) {
    super("Composant - %s".formatted(component.getClass().getSimpleName()), MenuSize.FIVE, player);
    this.component = component;
  }

  @Override
  public List<Button> getEntries() {
    return this.getAllFields(this.component.getClass()).stream()
        .map(field -> new FieldButton(this.component, field))
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  private static final class FieldButton extends Button {

    private final Component component;
    private final Field field;

    private FieldButton(final Component component, final Field field) {
      this.component = component;
      this.field = field;
      this.field.setAccessible(true);
    }

    @Override
    public ItemStack getIcon() {
      try {
        return new ItemBuilder(Material.NAME_TAG)
            .name("&a&l%s".formatted(this.field.getName()))
            .lore("", "&e»&r Valeur:&e %s".formatted(this.field.get(this.component)), "")
            .asItemStack();
      } catch (final IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private List<Field> getAllFields(Class<?> clazz) {
    final List<Field> fields = new ArrayList<>();
    while (clazz != null && clazz != Object.class) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }
}
