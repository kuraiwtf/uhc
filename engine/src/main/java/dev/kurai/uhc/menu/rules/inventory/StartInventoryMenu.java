package dev.kurai.uhc.menu.rules.inventory;

import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.profile.component.InventoryEditorComponent;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class StartInventoryMenu extends Menu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final UltraHardcoreAPI ultraHardcore;

  public StartInventoryMenu(final Player player, final UltraHardcoreAPI ultraHardcore) {
    super("Inventaire de départ", MenuSize.SIX, player);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));

    final var savedInventory = InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue();
    final var savedArmor = InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue();

    this.setupInventory(front, savedInventory);
    this.setupGlassDecoration(back);
    this.setupArmor(front, savedArmor);
    this.setupActionButtons(front);
  }

  private void setupInventory(final ForegroundLayer front, final ItemStack[] savedInventory) {
    for (int i = 0; i < savedInventory.length; i++) {
      final var item = savedInventory[i];
      if (item != null && item.getType() != Material.AIR) {
        front.set(i, new ItemButton(item));
      }
    }
  }

  private void setupGlassDecoration(final BackgroundLayer back) {
    back.fill(36, 44, GLASS);
    back.set(50, GLASS);
    back.set(53, GLASS);
  }

  private void setupArmor(final ForegroundLayer front, final ItemStack[] savedArmor) {
    final var armorSlots =
        new ArmorSlot[] {
          new ArmorSlot(45, 0, Material.CHAINMAIL_BOOTS, "Bottes"),
          new ArmorSlot(46, 1, Material.CHAINMAIL_LEGGINGS, "Pantalon"),
          new ArmorSlot(47, 2, Material.CHAINMAIL_CHESTPLATE, "Plastron"),
          new ArmorSlot(48, 3, Material.CHAINMAIL_HELMET, "Casque")
        };

    for (final var armorSlot : armorSlots) {
      final var item = savedArmor[armorSlot.armorIndex];
      if (item != null && item.getType() != Material.AIR) {
        front.set(armorSlot.menuSlot, new ItemButton(item));
      } else {
        front.set(
            armorSlot.menuSlot,
            new ArmorPlaceholderButton(armorSlot.placeholderMaterial, armorSlot.name));
      }
    }
  }

  private void setupActionButtons(final ForegroundLayer front) {
    front.set(51, new EditInventoryButton(this.ultraHardcore));
    front.set(52, new ResetInventoryButton());
  }

  private record ArmorSlot(
      int menuSlot, int armorIndex, Material placeholderMaterial, String name) {}

  private static final class ArmorPlaceholderButton extends Button {

    private final Material armorType;
    private final String armorName;

    private ArmorPlaceholderButton(final Material armorType, final String armorName) {
      this.armorType = armorType;
      this.armorName = armorName;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.armorType)
          .name("&c&l" + this.armorName)
          .lore("", "&7Aucune pièce d'armure", "&7configurée pour ce slot.", "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }

  private static final class EditInventoryButton extends Button {

    private final UltraHardcoreAPI ultraHardcore;

    private EditInventoryButton(final UltraHardcoreAPI ultraHardcore) {
      this.ultraHardcore = ultraHardcore;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.EMERALD)
          .name("&a&lModifier l'inventaire")
          .lore(
              "",
              "&7" + BAR + "&f Clic pour modifier",
              "  l'&ainventaire de départ&f.",
              "",
              "&7[&a?&7] &fUtilisez &2/&asave&f pour",
              "   &asauvegarder&f vos modifications.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var player = click.getMenu().getPlayer();
      final var profile =
          this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
      if (profile == null) {
        return;
      }

      final var inventory = player.getInventory();
      final var currentInventory = inventory.getContents().clone();
      final var currentArmor = inventory.getArmorContents().clone();

      profile.addComponent(new InventoryEditorComponent(currentInventory, currentArmor));
      player.closeInventory();

      this.loadInventoryForEditing(player);
      this.sendEditModeMessage(player);
    }

    private void loadInventoryForEditing(final Player player) {
      final var inventory = player.getInventory();
      inventory.clear();
      inventory.setArmorContents(null);

      final var savedInventory = InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue();
      final var savedArmor = InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue();

      inventory.setContents(savedInventory.clone());
      inventory.setArmorContents(savedArmor.clone());
    }

    private void sendEditModeMessage(final Player player) {
      this.ultraHardcore
          .bukkitAudiences()
          .player(player)
          .sendMessage(
              text()
                  .append(text("Mode édition activé! ", GREEN))
                  .append(text("Configurez votre inventaire et utilisez ", GRAY))
                  .append(text("/save", GOLD).clickEvent(runCommand("/save")))
                  .append(text(" pour sauvegarder.", GRAY))
                  .build());
    }
  }

  private static final class ResetInventoryButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BARRIER)
          .name("&c&lRéinitialiser l'inventaire")
          .lore(
              "",
              "&7" + BAR + "&f Permet de réinitialiser",
              "  l'&cinventaire de départ&f.",
              "",
              "&c&lAttention: &7Cette action",
              "&7supprimera tout l'inventaire!",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      InventoryConfiguration.INVENTORY_CONTENT_OPTION.setValue(new ItemStack[36]);
      InventoryConfiguration.INVENTORY_ARMOR_OPTION.setValue(new ItemStack[4]);
      click.getMenu().update();

      final var player = click.getMenu().getPlayer();
      player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
      player.sendMessage(
          colorize("&7&l" + BAR + "&f Vous avez&c réinitialisé&f l'&ainventaire&f de&a départ&f."));
    }
  }
}
