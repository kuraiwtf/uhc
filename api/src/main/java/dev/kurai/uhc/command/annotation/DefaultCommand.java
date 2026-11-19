package dev.kurai.uhc.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks the method executed when a {@link Command} class is run without any argument (e.g. {@code
 * /uhc}). The permission of the parent command applies. Use {@code /<command> help} to display the
 * sub-commands help in that case.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DefaultCommand {}
