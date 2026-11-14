package dev.kurai.uhc.menu.rules.enchantment;

import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.game.enchantment.EnchantmentLimitService;
import dev.kurai.uhc.game.enchantment.EnchantmentLimitType;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class EnchantmentLimitListMenu extends Menu {

  private static final int[] SLOTS = {
    10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38,
    39, 40, 41, 42, 43
  };

  public static final Map<String, String> NAMES =
      Map.ofEntries(
          Map.entry("PROTECTION_ENVIRONMENTAL", "Protection"),
          Map.entry("PROTECTION_FIRE", "Protection contre le feu"),
          Map.entry("PROTECTION_FALL", "Chute amortie"),
          Map.entry("PROTECTION_EXPLOSIONS", "Protection contre les explosions"),
          Map.entry("PROTECTION_PROJECTILE", "Protection contre les projectiles"),
          Map.entry("OXYGEN", "Apnée"),
          Map.entry("WATER_WORKER", "Affinité aquatique"),
          Map.entry("THORNS", "Épines"),
          Map.entry("DEPTH_STRIDER", "Agilité aquatique"),
          Map.entry("DAMAGE_ALL", "Tranchant"),
          Map.entry("DAMAGE_UNDEAD", "Châtiment"),
          Map.entry("DAMAGE_ARTHROPODS", "Fléau des arthropodes"),
          Map.entry("KNOCKBACK", "Recul"),
          Map.entry("FIRE_ASPECT", "Aura de feu"),
          Map.entry("LOOT_BONUS_MOBS", "Butin"),
          Map.entry("DIG_SPEED", "Efficacité"),
          Map.entry("SILK_TOUCH", "Toucher de soie"),
          Map.entry("DURABILITY", "Solidité"),
          Map.entry("LOOT_BONUS_BLOCKS", "Fortune"),
          Map.entry("ARROW_DAMAGE", "Puissance"),
          Map.entry("ARROW_KNOCKBACK", "Frappe"),
          Map.entry("ARROW_FIRE", "Flamme"),
          Map.entry("ARROW_INFINITE", "Infinité"),
          Map.entry("LUCK", "Chance de la mer"),
          Map.entry("LURE", "Appât"));

  private final EnchantmentLimitService limitService;
  private final EnchantmentLimitType type;
  private final @Nullable UUID target;

  public EnchantmentLimitListMenu(
      final Player player,
      final EnchantmentLimitService limitService,
      final EnchantmentLimitType type,
      final @Nullable UUID target) {
    super("Limites: " + type.alias(), MenuSize.SIX, player);
    this.limitService = limitService;
    this.type = type;
    this.target = target;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.PURPLE.getData()));

    final var enchantments = Enchantment.values();
    for (int i = 0; i < enchantments.length && i < SLOTS.length; i++) {
      front.set(SLOTS[i], new EnchantmentButton(enchantments[i]));
    }
  }

  private OptionalInt limit(final Enchantment enchantment) {
    return this.target == null
        ? this.limitService.limit(this.type, enchantment)
        : this.limitService.limit(this.target, this.type, enchantment);
  }

  private void limit(final Enchantment enchantment, final int level) {
    if (this.target == null) {
      this.limitService.limit(this.type, enchantment, level);
    } else {
      this.limitService.limit(this.target, this.type, enchantment, level);
    }
  }

  private void reset(final Enchantment enchantment) {
    if (this.target == null) {
      this.limitService.resetLimit(this.type, enchantment);
    } else {
      this.limitService.resetLimit(this.target, this.type, enchantment);
    }
  }

  private final class EnchantmentButton extends Button {

    private final Enchantment enchantment;

    private EnchantmentButton(final Enchantment enchantment) {
      this.enchantment = enchantment;
    }

    @Override
    public ItemStack getIcon() {
      final var limit = EnchantmentLimitListMenu.this.limit(this.enchantment);
      final var color = EnchantmentLimitListMenu.this.type.color();
      final var value =
          limit.isEmpty()
              ? "&aVanilla &7(" + this.enchantment.getMaxLevel() + ")"
              : limit.getAsInt() == 0 ? "&cInterdit" : color + limit.getAsInt();

      return new ItemBuilder(limit.isPresent() ? Material.ENCHANTED_BOOK : Material.BOOK)
          .name(
              color
                  + "&l"
                  + NAMES.getOrDefault(this.enchantment.getName(), this.enchantment.getName()))
          .lore(
              "",
              color + " " + SQUARE + "&f Limite: " + value,
              "",
              "&7" + BAR + "&f Clic gauche: &a+1",
              "&7" + BAR + "&f Clic droit: &c-1",
              "&7" + BAR + "&f Shift + clic: &evanilla",
              "")
          .glowing(limit.isPresent())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = EnchantmentLimitListMenu.this;
      final var vanilla = this.enchantment.getMaxLevel();
      final var current = menu.limit(this.enchantment);
      final var clickType = click.getType();

      if (clickType.isShiftClick()) {
        menu.reset(this.enchantment);
      } else if (clickType == ClickType.LEFT) {
        if (current.isPresent()) {
          final var next = current.getAsInt() + 1;
          if (next >= vanilla) {
            menu.reset(this.enchantment);
          } else {
            menu.limit(this.enchantment, next);
          }
        }
      } else if (clickType == ClickType.RIGHT) {
        menu.limit(this.enchantment, Math.max(0, current.orElse(vanilla) - 1));
      }

      click.getMenu().update();
    }
  }
}
