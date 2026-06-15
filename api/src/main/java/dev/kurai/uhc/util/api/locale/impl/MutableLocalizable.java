package dev.kurai.uhc.util.api.locale.impl;

import dev.kurai.uhc.util.api.locale.Localizable;
import java.util.Locale;

public interface MutableLocalizable extends Localizable {

  void setLocale(final Locale locale);
}
