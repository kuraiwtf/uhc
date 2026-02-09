package dev.kurai.uhc.tablist.service;

import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.tablist.TabListProvider;
import dev.kurai.uhc.tablist.adapter.BuiltinTablistHeaderProvider;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public final class TabListServiceImpl implements TabListService {

  private static final TabListProvider TAB_LIST_FOOTER =
      _ ->
          text()
              .appendNewline()
              .append(text("@kuraiwtf", NamedTextColor.GOLD))
              .appendNewline()
              .build();
  private TabListProvider headerProvider, footerProvider;

  public TabListServiceImpl() {
    this.install(new BuiltinTablistHeaderProvider(), TAB_LIST_FOOTER);
  }

  @Override
  public @NotNull TabListProvider getHeaderProvider() {
    return this.headerProvider;
  }

  @Override
  public @NotNull TabListProvider getFooterProvider() {
    return this.footerProvider;
  }

  @Override
  public void installHeaderProvider(final @NotNull TabListProvider headerProvider) {
    this.headerProvider = headerProvider;
  }

  @Override
  public void installFooterProvider(final @NotNull TabListProvider footerProvider) {
    this.footerProvider = footerProvider;
  }
}
