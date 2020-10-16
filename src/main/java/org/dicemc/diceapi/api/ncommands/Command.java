package org.dicemc.diceapi.api.ncommands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();

    String[] aliases() default {};

    String args() default "";

    String desc();

    boolean onlyPlayer() default true;

    boolean onlyConsole() default false;

    boolean invisible() default false;
}
