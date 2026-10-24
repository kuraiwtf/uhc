package dev.kurai.uhc.module.camp;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public interface Camp {

  String getName();

  ChatColor primaryColor();

  ChatColor secondaryColor();

  ItemStack display();
}
