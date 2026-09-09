package dev.kurai.uhc.menu.module;

import static org.bukkit.Material.NETHER_STAR;

import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.role.module.RoleModule;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ModuleMenu extends Menu {

  protected final AbstractModule module;

  public ModuleMenu(final MenuSize size, final Player player, final AbstractModule module) {
    super(module.getName(), size, player);
    this.module = module;
  }

  @Override
  public void setup(final BackgroundLayer backgroundLayer, final ForegroundLayer front) {
    this.apply(new BorderTemplate(this.module.color().asDyeColor().getData()));

    if (this.module instanceof final RoleModule<?, ?, ?> roleModule) {
      front.set(10, new HiddenCompositionButton(roleModule));
    }

    if (!this.module.events().isEmpty()) {
      front.set(28, new RandomEventButton(this.module));
    }
  }

  private static final class HiddenCompositionButton extends Button {

    private final RoleModule<?, ?, ?> roleModule;

    private HiddenCompositionButton(final RoleModule<?, ?, ?> roleModule) {
      this.roleModule = roleModule;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(NETHER_STAR)
          .name("&c&lComposition cachée")
          .lore(
              "",
              "&c "
                  + CC.SQUARE
                  + "&f Composition: "
                  + (this.roleModule.hiddenComposition() ? "&cCachée" : "&aVisible"),
              "")
          .amount(this.roleModule.hiddenComposition() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.roleModule.hiddenComposition(!this.roleModule.hiddenComposition());
      click.getMenu().update();
    }
  }
}
