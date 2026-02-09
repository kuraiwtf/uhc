package dev.kurai.uhc.timer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Duration {

  /**
   * Minimum duration in seconds.
   *
   * @return the minimum duration in seconds
   */
  int min() default 0;

  /**
   * Default duration in seconds.
   *
   * @return the default duration in seconds, or -1 for infinite
   */
  int defaultValue() default -1;

  /**
   * Maximum duration in seconds.
   *
   * @return the maximum duration in seconds, or -1 for infinite
   */
  int max() default -1;
}
