package dev.kurai.uhc.tablist;

import org.jetbrains.annotations.NotNull;

public interface TabListService {

  @NotNull
  TabListProvider getHeaderProvider();

  @NotNull
  TabListProvider getFooterProvider();

  void installHeaderProvider(final @NotNull TabListProvider headerProvider);

  void installFooterProvider(final @NotNull TabListProvider footerProvider);

  default void install(
      final @NotNull TabListProvider headerProvider,
      final @NotNull TabListProvider footerProvider) {
    this.installHeaderProvider(headerProvider);
    this.installFooterProvider(footerProvider);
  }
}
