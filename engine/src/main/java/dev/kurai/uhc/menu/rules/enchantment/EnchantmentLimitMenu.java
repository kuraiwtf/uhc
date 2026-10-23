package dev.kurai.uhc.menu.rules.enchantment;

import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.game.enchantment.EnchantmentLimitService;
import dev.kurai.uhc.game.enchantment.EnchantmentLimitType;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.ModernBorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.UUID;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Entry point of the enchantment limits. With a {@code target}, the menu edits the limits of that
 * player only, otherwise the limits of the whole game.
 */
@NullMarked
public final class EnchantmentLimitMenu extends Menu {

  private final EnchantmentLimitService limitService;
  private final @Nullable UUID target;
  private final @Nullable String targetName;

  public EnchantmentLimitMenu(
      final Player player,
      final EnchantmentLimitService limitService,
      final @Nullable UUID target,
      final @Nullable String targetName) {
    super(
        target == null ? "Limites des enchantements" : "Enchantements de " + targetName,
        MenuSize.THREE,
        player);
    this.limitService = limitService;
    this.target = target;
    this.targetName = targetName;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new ModernBorderTemplate(DyeColor.PURPLE.getData(), DyeColor.GRAY.getData()));

    front.set(
        4,
        new ItemButton(
            new ItemBuilder(Material.PAPER)
                .name("&e&lInformations")
                .lore(
                    "",
                    "&7" + BAR + "&f Définit le niveau maximum de",
                    "  chaque&d enchantement&f"
                        + (this.target == null
                            ? " pour la partie."
                            : " pour &e" + this.targetName + "&f."),
                    "",
                    "&7" + BAR + "&f La limite du&b type&f d'équipement",
                    "  passe avant la limite&d globale&f.",
                    this.target == null ? "" : "&7" + BAR + "&f Elle remplace celle de la partie.",
                    "")
                .asItemStack()));

    front.set(12, new TypeButton(EnchantmentLimitType.GLOBAL));
    front.set(13, new TypeButton(EnchantmentLimitType.IRON));
    front.set(14, new TypeButton(EnchantmentLimitType.DIAMOND));
  }

  private final class TypeButton extends Button {

    private final EnchantmentLimitType type;

    private TypeButton(final EnchantmentLimitType type) {
      this.type = type;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.type.icon())
          .name(this.type.displayName())
          .lore(
              "",
              "&7" + BAR + "&f Cliquez pour modifier les",
              "  limites de type " + this.type.color() + this.type.alias() + "&f.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var listMenu =
          new EnchantmentLimitListMenu(
              menu.getPlayer(),
              EnchantmentLimitMenu.this.limitService,
              this.type,
              EnchantmentLimitMenu.this.target);
      listMenu.setPreviousMenu(menu);
      listMenu.open();
    }
  }
}
