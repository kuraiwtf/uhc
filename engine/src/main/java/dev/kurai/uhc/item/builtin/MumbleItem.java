package dev.kurai.uhc.item.builtin;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static org.bukkit.event.block.Action.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MumbleItem extends CustomItem {

  private static final String IDENTIFIER = "mumble";

  private final UltraHardcoreAPI ultraHardcore;

  public MumbleItem(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(IDENTIFIER, false, false);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public @NotNull ItemStack provideIcon(final @NotNull Player player) {
    return new ItemBuilder(Material.SKULL_ITEM)
        .data(3)
        .name("&b&lMumble&8 " + CC.SQUARE + "&7 Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de rejoindre le &bMumble&f.", "")
        .glowing(true)
        .url(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2ZjMwNzk4MWU0ZWU1NjdlMTQ1NWYzZTdjMGZlNTU4MWU5YWZjNmU0ZWUxOWQyNmU2NTA2ZTdjZDdiZDc5In19fQ==")
        .asItemStack();
  }

  @Override
  public void onInteract(final @NotNull Player player, final @NotNull PlayerInteractEvent event) {
    event.setCancelled(true);
    final var action = event.getAction();
    if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) {
      return;
    }

    this.ultraHardcore
        .getBukkitAudiences()
        .player(player)
        .sendMessage(
            prefix()
                .append(text('[', DARK_GRAY))
                .append(text("Cliquez-ici", GOLD, TextDecoration.BOLD))
                .append(text(']', DARK_GRAY))
                .appendSpace()
                .append(text("Pour rejoindre le "))
                .append(text("Mumble", AQUA, TextDecoration.BOLD))
                .append(text('.'))
                .build());
  }

  @Override
  public void onDrop(final @NotNull Player player, final @NotNull PlayerDropItemEvent event) {
    event.setCancelled(true);
  }

  @Override
  public void onInventoryClick(
      final @NotNull Player player, final @NotNull InventoryClickEvent event) {
    event.setCancelled(true);
  }
}
