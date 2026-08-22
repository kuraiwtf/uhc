package dev.kurai.uhc.tablist;

public interface TabListService {

  TabListProvider getHeaderProvider();

  TabListProvider getFooterProvider();

  void installHeaderProvider(final TabListProvider headerProvider);

  void installFooterProvider(final TabListProvider footerProvider);

  default void install(final TabListProvider headerProvider, final TabListProvider footerProvider) {
    this.installHeaderProvider(headerProvider);
    this.installFooterProvider(footerProvider);
  }
}
