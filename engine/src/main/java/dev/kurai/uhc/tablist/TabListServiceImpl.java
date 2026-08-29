package dev.kurai.uhc.tablist;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.tablist.part.CreditTabListPart;
import dev.kurai.uhc.tablist.part.GlobalInformationTablistPart;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

public final class TabListServiceImpl implements TabListService {

  private final Object2ObjectMap<String, TabListPart> parts;
  private final Collection<TabListPart> partsView;

  public TabListServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.parts = new Object2ObjectOpenHashMap<>();
    this.partsView = Collections.unmodifiableCollection(this.parts.values());

    this.addPart(new GlobalInformationTablistPart(ultraHardcore));
    this.addPart(new CreditTabListPart());
  }

  @Contract(pure = true)
  @Override
  public Collection<TabListPart> parts() {
    return this.partsView;
  }

  @Override
  public @UnmodifiableView Collection<TabListPart> partsByPosition(
      final TabListPart.Position position) {
    return this.partsView.stream()
        .filter(part -> part.position() == position)
        .sorted(Comparator.comparingInt(TabListPart::priority))
        .toList();
  }

  @Override
  public void addPart(final TabListPart part) {
    this.parts.put(part.key(), part);
  }

  @Override
  public void removePart(final String key) {
    this.parts.remove(key);
  }
}
