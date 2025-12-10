package com.tenmo2003.dynamicdto.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author anhvn
 * @since 2025-12-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TypeName {

    String value() default "";
    String[] aliases() default {};
}
