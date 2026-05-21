package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.scenario.CutCleanDropEvent;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CutCleanScenario extends AbstractScenario implements Listener {

  private static final Map<ItemStack, ItemStack> FURNACE_RECIPES = Maps.newHashMap();

  static {
    final var recipeIterator = Bukkit.getServer().recipeIterator();
    while (recipeIterator.hasNext()) {
      final var recipe = recipeIterator.next();

      if (recipe instanceof final FurnaceRecipe furnaceRecipe) {
        final var input = furnaceRecipe.getInput();
        final var result = furnaceRecipe.getResult();

        FURNACE_RECIPES.put(input.clone(), result.clone());
      }
    }
  }

  public CutCleanScenario(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("cut_clean", "Cut Clean", ultraHardcore);
  }

  @Override
  public @NotNull ItemStack provideIcon() {
    return new ItemStack(Material.IRON_INGOT);
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final var block = event.getBlock();
    final var type = block.getType();
    if (!type.name().contains("ORE")) {
      return;
    }

    final var result = this.findFurnaceResult(new ItemStack(block.getType(), 1, block.getData()));
    if (result == null) {
      return;
    }

    if (type == Material.LAPIS_ORE) {
      result.setAmount(ThreadLocalRandom.current().nextInt(4, 7));
    }

    final var hand = event.getPlayer().getItemInHand();
    if (hand != null
        && hand.hasItemMeta()
        && hand.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
      result.setAmount(
          result.getAmount()
              + ThreadLocalRandom.current()
                  .nextInt(
                      1, hand.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2));
    }

    final var experience =
        this.calculateExperience(
            result.getAmount(), ThreadLocalRandom.current().nextFloat(2f, 3.5f));
    final var dropEvent =
        this.ultraHardcore
            .eventService()
            .dispatchEvent(new CutCleanDropEvent(event.getPlayer(), result, experience));

    block.setType(Material.AIR);
    if (dropEvent.isCancelled()) {
      return;
    }

    final var world = block.getWorld();
    final var location = block.getLocation().clone().add(0.5, 0.5, 0.5);
    world.dropItem(location, result);
    final var orb = world.spawn(location, ExperienceOrb.class);
    orb.setExperience(experience);
  }

  private ItemStack findFurnaceResult(final ItemStack input) {
    for (final var entry : FURNACE_RECIPES.entrySet()) {
      final ItemStack recipeInput = entry.getKey();

      if (recipeInput.getType() == input.getType()
          && recipeInput.getDurability() == input.getDurability()) {
        return entry.getValue().clone();
      }
    }

    for (final var entry : FURNACE_RECIPES.entrySet()) {
      if (entry.getKey().getType() == input.getType()) {
        return entry.getValue().clone();
      }
    }
    return null;
  }

  private int calculateExperience(final int count, final float xpPerItem) {
    if (xpPerItem == 0.0f) {
      return 0;
    }

    final int totalXp;

    if (xpPerItem < 1.0f) {
      int j = (int) Math.floor((float) count * xpPerItem);
      final float exactXp = (float) count * xpPerItem;
      if (j < Math.ceil(exactXp) && Math.random() < (double) (exactXp - (float) j)) {
        ++j;
      }

      totalXp = j;
    } else {
      totalXp = (int) ((float) count * xpPerItem);
    }

    return totalXp;
  }
}
