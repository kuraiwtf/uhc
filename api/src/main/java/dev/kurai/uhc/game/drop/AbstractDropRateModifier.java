package dev.kurai.uhc.game.drop;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractDropRateModifier implements Identifiable<String>, Nameable<String> {

  protected final String id;
  protected final String name;

  protected final Random random;

  protected int dropRate = 50;

  public AbstractDropRateModifier(final String id, final String name) {
    this.id = id;
    this.name = name;

    this.random = ThreadLocalRandom.current();
  }

  public abstract ItemStack getIcon();

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public int getDropRate() {
    return this.dropRate;
  }

  public void setDropRate(final int dropRate) {
    this.dropRate = dropRate;
  }
}
