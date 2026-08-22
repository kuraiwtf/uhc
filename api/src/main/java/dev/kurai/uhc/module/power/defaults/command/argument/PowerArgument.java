package dev.kurai.uhc.module.power.defaults.command.argument;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public record PowerArgument(String key, Object object) {

  public @Nullable Integer asInteger() {
    return this.asInteger(0);
  }

  public @Nullable Integer asInteger(final Integer defaultValue) {
    return this.object instanceof Integer ? (Integer) this.object : defaultValue;
  }

  public @Nullable Long asLong() {
    return this.asLong(0L);
  }

  public @Nullable Long asLong(final Long defaultValue) {
    return this.object instanceof Long ? (Long) this.object : defaultValue;
  }

  public @Nullable Double asDouble() {
    return this.asDouble(0.0);
  }

  public @Nullable Double asDouble(final Double defaultValue) {
    return this.object instanceof Double ? (Double) this.object : defaultValue;
  }

  public @Nullable Float asFloat() {
    return this.asFloat(0.0F);
  }

  public @Nullable Float asFloat(final Float defaultValue) {
    return this.object instanceof Float ? (Float) this.object : defaultValue;
  }

  public @Nullable Boolean asBoolean() {
    return this.asBoolean(false);
  }

  public @Nullable Boolean asBoolean(final Boolean defaultValue) {
    return this.object instanceof Boolean ? (Boolean) this.object : defaultValue;
  }

  public @Nullable String asString() {
    return this.asString("");
  }

  public @Nullable String asString(final String defaultValue) {
    return this.object instanceof String ? (String) this.object : defaultValue;
  }

  public @Nullable UUID asUniqueId() {
    return this.asUniqueId(null);
  }

  public @Nullable UUID asUniqueId(final UUID defaultValue) {
    return this.object instanceof UUID ? (UUID) this.object : defaultValue;
  }

  public @Nullable Player asPlayer() {
    return this.asPlayer(null);
  }

  public @Nullable Player asPlayer(final @Nullable UUID defaultValue) {
    return this.object instanceof UUID
        ? Bukkit.getPlayer((UUID) this.object)
        : Bukkit.getPlayer(defaultValue);
  }

  public <T> @Nullable T as(final Class<T> clazz) {
    return this.as(clazz, null);
  }

  public <T> @Nullable T as(final Class<T> clazz, final @Nullable T defaultValue) {
    return clazz.isInstance(this.object) ? clazz.cast(this.object) : defaultValue;
  }
}
