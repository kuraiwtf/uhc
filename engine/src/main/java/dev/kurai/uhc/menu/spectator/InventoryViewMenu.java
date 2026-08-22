package dev.kurai.uhc.menu.spectator;

import com.google.common.collect.Lists;
import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import java.text.DecimalFormat;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.annotation.AutoUpdate;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

@AutoUpdate(2)
public final class InventoryViewMenu extends Menu {

  private final Player target;
  private final Profile profile;

  public InventoryViewMenu(final Player player, final Player target, final Profile profile) {
    super("Inventaire - %s".formatted(target.getName()), MenuSize.SIX, player);
    this.target = target;
    this.profile = profile;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    final GlassButton glass = new GlassButton(DyeColor.GRAY.getData());
    back.row(4, glass);

    int inventorySlot = 0;
    for (final ItemStack stack : this.target.getInventory().getContents()) {
      if (stack == null || stack.getType() == Material.AIR) {
        inventorySlot++;
        continue;
      }

      front.set(inventorySlot++, new ItemButton(stack));
    }

    int armorSlot = 0;
    for (final ItemStack stack : this.target.getInventory().getArmorContents()) {
      if (stack == null || stack.getType() == Material.AIR) {
        front.set(
            armorSlot++,
            5,
            new ItemButton(
                new ItemBuilder(Material.INK_SACK)
                    .data(DyeColor.RED.getDyeData())
                    .name("&c&lAucune pièce")
                    .lore(
                        "",
                        "&cCe joueur ne porte pas",
                        "&cde pièce d'armure à cet",
                        "&cemplacement.",
                        "")
                    .asItemStack()));
        continue;
      }

      front.set(armorSlot++, 5, new ItemButton(stack));
    }

    back.set(4, 5, glass);
    front.set(5, 5, new HealthButton(this.target));
    front.set(6, 5, new AbilitiesButton(this.target, this.profile));
    front.set(7, 5, new EffectButton(this.target));
    front.set(8, 5, new InformationButton(this.target));
  }

  private static final class HealthButton extends Button {

    private final Player target;

    private HealthButton(final Player target) {
      this.target = target;
    }

    @Override
    public ItemStack getIcon() {
      final DecimalFormat decimalFormat = new DecimalFormat("#.#");
      return new ItemBuilder(Material.APPLE)
          .name("&c&lSanté")
          .lore(
              "",
              "Vie: &c" + decimalFormat.format(this.target.getHealth() / 2) + "§4" + CC.HEART,
              "Absorption: &e"
                  + decimalFormat.format(
                      ((CraftPlayer) this.target).getHandle().getAbsorptionHearts() / 2)
                  + "§6"
                  + CC.HEART,
              "",
              "Nourriture: &a" + decimalFormat.format(this.target.getFoodLevel()),
              "Saturation: &a" + decimalFormat.format(this.target.getSaturation()),
              "")
          .asItemStack();
    }
  }

  private static final class AbilitiesButton extends Button {

    private final Player target;
    private final Profile profile;

    private AbilitiesButton(final Player target, final Profile profile) {
      this.target = target;
      this.profile = profile;
    }

    @Override
    public ItemStack getIcon() {
      final var lines = Lists.<String>newArrayList();
      lines.add("");
      final var powers = this.profile.getPowers();
      if (powers.isEmpty()) {
        lines.add("&c " + CC.SQUARE + "&r Aucun pouvoir");
      } else {
        for (final AbstractPower power : powers) {
          boolean restricted = false;

          for (final PowerRestriction restriction : power.getRestrictions()) {
            if (restriction.restrictsPower(power, this.target)) {
              restricted = true;
              break;
            }
          }

          lines.add(
              "&a "
                  + CC.SQUARE
                  + "&r "
                  + power.getName()
                  + " "
                  + (restricted ? "&cRestreint" : "&aUtilisable"));
        }
      }
      lines.add("");

      return new ItemBuilder(Material.NETHER_STAR).name("&a&lCapacités").lore(lines).asItemStack();
    }
  }

  private static final class EffectButton extends Button {

    private final Player target;

    private EffectButton(final Player target) {
      this.target = target;
    }

    @Override
    public ItemStack getIcon() {
      final var lines = Lists.<String>newArrayList();
      lines.add("");
      final var effects = this.target.getActivePotionEffects();
      if (effects.isEmpty()) {
        lines.add("&c " + CC.SQUARE + "&r Aucun effet");
      } else {
        for (final PotionEffect effect : effects) {
          lines.add(
              "&d "
                  + CC.SQUARE
                  + "&r "
                  + effect.getType().getName()
                  + ": &d"
                  + (effect.getAmplifier() + 1)
                  + "&8 (&d"
                  + (effect.getDuration() / 20)
                  + "s&8)");
        }
      }
      lines.add("");

      return new ItemBuilder(Material.POTION)
          .data(16421)
          .name("&d&lEffets")
          .lore(lines)
          .addFlags(ItemFlag.values())
          .asItemStack();
    }
  }

  private static final class InformationButton extends Button {

    private final Player target;

    private InformationButton(final Player target) {
      this.target = target;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .name("&b&lInformations")
          .lore("", "Feu: &c" + Math.max(0, this.target.getFireTicks()) + " ticks", "")
          .skullOwner(this.target.getName())
          .asItemStack();
    }
  }
}
