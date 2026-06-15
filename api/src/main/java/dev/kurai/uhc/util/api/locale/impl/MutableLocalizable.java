package dev.kurai.uhc.util.api.locale.impl;

import dev.kurai.uhc.util.api.locale.Localizable;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public interface MutableLocalizable extends Localizable {

  void setLocale(final  Locale locale);
}
