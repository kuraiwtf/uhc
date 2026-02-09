package dev.kurai.uhc.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMeta {

  String name();

  String[] aliases() default {};

  String permission() default "";

  String description() default "";

  boolean async() default false;
}
