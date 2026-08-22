package dev.kurai.uhc.game.scenario.defaults;

import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Material.*;
import static org.bukkit.Material.DIAMOND;
import static org.bukkit.Material.DIAMOND_ORE;
import static org.bukkit.Material.GOLD_INGOT;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.game.configuration.ore.OreConfiguration;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
      if (blockType == DIAMOND_ORE) {
        player.getInventory().addItem(new ItemStack(GOLD_INGOT, 2));
      }

      event.getBlock().setType(AIR);
      return;
    }

    if (miningComponent != null) {
      this.incrementMined(blockType, miningComponent);
      this.sendLimitActionbar(profile, blockType, miningComponent);
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

  private static final Map<Material, Component> ORE_NAMES =
      Map.of(
          IRON_ORE, text("fer", NamedTextColor.GRAY),
          GOLD_ORE, text("or", NamedTextColor.YELLOW),
          DIAMOND_ORE, text("diamant", NamedTextColor.AQUA));

  private static final Map<Material, String> ARTICLES =
      Map.of(
          IRON_ORE, "du ",
          GOLD_ORE, "de l'",
          DIAMOND_ORE, "du ");

  private void sendLimitActionbar(
      final Profile profile, final Material blockType, final ProfileMiningComponent miningComponent) {
    final var oreName = ORE_NAMES.get(blockType);
    if (oreName == null) {
      return;
    }

    final var limit =
        switch (blockType) {
          case IRON_ORE -> OreConfiguration.IRON_LIMIT_OPTION.getValue();
          case GOLD_ORE -> OreConfiguration.GOLD_LIMIT_OPTION.getValue();
          default -> OreConfiguration.DIAMOND_LIMIT_OPTION.getValue();
        };
    if (limit <= 0) {
      return;
    }

    final var mined =
        switch (blockType) {
          case IRON_ORE -> miningComponent.getIronMined();
          case GOLD_ORE -> miningComponent.getGoldMined();
          default -> miningComponent.getDiamondMined();
        };

    final var oreColor = oreName.color();
    profile
        .getActionbar()
        .registerEntry(
            UltraHardcoreKey.key("ore_mined"),
            text("Vous venez de miner ")
                .append(text(ARTICLES.get(blockType)))
                .append(oreName)
                .append(text("."))
                .appendSpace()
                .append(text('(', NamedTextColor.GRAY))
                .append(text(mined, oreColor))
                .append(text('/', NamedTextColor.GRAY))
                .append(text(limit, oreColor))
                .append(text(')', NamedTextColor.GRAY)),
            Duration.ofSeconds(3L));
  }

  private void incrementMined(final Material blockType, final ProfileMiningComponent miningComponent) {
    switch (blockType) {
      case IRON_ORE -> miningComponent.setIronMined(miningComponent.getIronMined() + 1);
      case GOLD_ORE -> miningComponent.setGoldMined(miningComponent.getGoldMined() + 1);
      case DIAMOND_ORE -> miningComponent.setDiamondMined(miningComponent.getDiamondMined() + 1);
      default -> {}
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
