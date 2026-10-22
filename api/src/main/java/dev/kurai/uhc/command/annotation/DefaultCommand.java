package dev.kurai.uhc.command.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the method executed when a {@link Command} class is run without any argument (e.g. {@code
 * /uhc}). The permission of the parent command applies. Use {@code /<command> help} to display the
 * sub-commands help in that case.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DefaultCommand {}
