package dev.kurai.uhc.skin;

import dev.kurai.uhc.util.Skin;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface SkinHolder {

  /**
   * Retrieves the {@link Skin} associated with the specified unique identifier.
   *
   * @param uniqueId the UUID of the entity whose skin is to be retrieved
   * @return the {@link Skin} object associated with the given UUID, or {@code null} if no skin is
   *     associated with the specified UUID
   */
  @Nullable Skin skin(final UUID uniqueId);

  /**
   * Applies the specified {@link Skin} to the entity identified by the given UUID.
   *
   * @param uniqueId the UUID of the entity to which the skin should be applied
   * @param skin the {@link Skin} object to be applied to the entity
   */
  void applySkin(final UUID uniqueId, final Skin skin);

  /**
   * Removes the skin associated with the specified unique identifier.
   *
   * @param uniqueId the UUID of the entity whose skin is to be removed
   */
  void removeSkin(final UUID uniqueId);

  /**
   * Updates the appearance of an entity's skin for all online players.
   *
   * <p>This method iterates over all online players and briefly hides the player associated with
   * this skin holder from their view, before showing the player again. This process forces the
   * client to refresh its view of the player's skin, allowing any updates to the skin to take
   * effect immediately.
   *
   * <p>Typically used when a player's skin has been modified and the changes need to be propagated
   * to other clients in real time.
   */
  void updateSkin();
}
