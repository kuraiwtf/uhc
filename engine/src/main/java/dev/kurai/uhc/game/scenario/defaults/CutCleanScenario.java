package dev.kurai.uhc.game.scenario.defaults;

import static org.bukkit.Material.*;
import static org.bukkit.Material.DIAMOND;
import static org.bukkit.Material.DIAMOND_ORE;
import static org.bukkit.Material.GOLD_INGOT;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.ore.OreConfiguration;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public final class CutCleanScenario extends AbstractScenario implements Listener {

  private static final Map<Material, Material> ALLOWED_MATERIALS =
      Map.of(
          IRON_ORE, IRON_INGOT,
          GOLD_ORE, GOLD_INGOT,
          DIAMOND_ORE, DIAMOND);

  private static final Collection<EntityType> ALLOWED_ENTITIES =
      Set.of(
          EntityType.COW, EntityType.PIG, EntityType.CHICKEN, EntityType.SHEEP, EntityType.RABBIT);

  public CutCleanScenario(final UltraHardcoreAPI ultraHardcore) {
    super("cut_clean", "Cut Clean", ultraHardcore, ScenarioCategory.MINING);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.IRON_INGOT);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBreak(final BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }

    final var block = event.getBlock();
    final var blockType = block.getType();
    if (!ALLOWED_MATERIALS.containsKey(blockType)) {
      return;
    }

    final var player = event.getPlayer();

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    final var miningComponent = profile.getComponent(ProfileMiningComponent.class);
    if (miningComponent != null && this.isOreLimitReached(blockType, miningComponent)) {
      return;
    }

    final var smeltedMaterial = ALLOWED_MATERIALS.get(blockType);
    final var drops = block.getDrops();
    event.getBlock().setType(AIR);
    event.setExpToDrop(0);
    player.giveExp(ThreadLocalRandom.current().nextInt(2, 6));

    for (final var stack : drops) {
      stack.setType(smeltedMaterial);
      final var map = player.getInventory().addItem(stack);
      if (map.isEmpty()) {
        continue;
      }
      block.getWorld().dropItemNaturally(player.getLocation(), stack);
    }
  }

  private boolean isOreLimitReached(
      final Material blockType, final ProfileMiningComponent miningComponent) {
    return switch (blockType) {
      case IRON_ORE -> {
        final var limit = OreConfiguration.IRON_LIMIT_OPTION.getValue();
        yield limit > 0 && miningComponent.getIronMined() >= limit;
      }
      case GOLD_ORE -> {
        final var limit = OreConfiguration.GOLD_LIMIT_OPTION.getValue();
        yield limit > 0 && miningComponent.getGoldMined() >= limit;
      }
      case DIAMOND_ORE -> {
        final var limit = OreConfiguration.DIAMOND_LIMIT_OPTION.getValue();
        yield limit > 0 && miningComponent.getDiamondMined() >= limit;
      }
      default -> false;
    };
  }

  @EventHandler
  public void onDeath(final EntityDeathEvent event) {
    final var entity = event.getEntity();
    if (!ALLOWED_ENTITIES.contains(entity.getType())) {
      return;
    }

    event.getDrops().clear();

    final var world = entity.getWorld();
    final var location = entity.getLocation();

    final var leather = new ItemStack(LEATHER, 2);

    switch (entity.getType()) {
      case COW -> {
        world.dropItemNaturally(location, new ItemStack(COOKED_BEEF, 2));
        world.dropItemNaturally(location, leather);
      }
      case CHICKEN -> {
        world.dropItemNaturally(location, new ItemStack(COOKED_CHICKEN, 2));
        world.dropItemNaturally(location, new ItemStack(FEATHER, 2));
      }
      case PIG -> world.dropItemNaturally(location, new ItemStack(GRILLED_PORK, 2));
      case SHEEP -> {
        world.dropItemNaturally(location, new ItemStack(COOKED_MUTTON, 2));
        world.dropItemNaturally(location, new ItemStack(WOOL));
      }
      case RABBIT -> {
        world.dropItemNaturally(location, new ItemStack(COOKED_RABBIT, 2));
        world.dropItemNaturally(location, leather);
      }
    }
  }
}
