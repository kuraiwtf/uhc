package dev.kurai.uhc.tablist;

import java.util.Collection;

public interface TabListService {

  /**
   * Retrieves all parts associated with the game tab list.
   *
   * @return a collection of {@code GameTabListPart} objects representing all parts in the game tab
   *     list.
   */
  Collection<TabListPart> parts();

  /**
   * Retrieves all parts of the game tab list that match the specified position.
   *
   * @param position the position of the parts to retrieve, which determines whether the parts are
   *     displayed at the top or bottom of the game tab list.
   * @return a collection of {@code GameTabListPart} objects that match the specified position.
   */
  Collection<TabListPart> partsByPosition(final TabListPart.Position position);

  /**
   * Adds a new part to the game tab list. The provided part will be managed by the service and may
   * appear in the game tab list based on its configuration and position.
   *
   * @param part the {@code GameTabListPart} instance to be added to the game tab list
   */
  void addPart(final TabListPart part);

  /**
   * Removes a part from the game tab list based on its unique key identifier. The part associated
   * with the specified key will no longer appear in the game tab list or be managed by the service.
   *
   * @param key the unique key of the {@code GameTabListPart} to be removed
   */
  void removePart(final String key);
}
