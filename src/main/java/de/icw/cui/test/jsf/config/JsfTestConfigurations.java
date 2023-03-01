package de.icw.cui.test.jsf.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link JsfTestConfiguration} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@interface JsfTestConfigurations {

    /**
     * @return an array of {@link JsfTestConfiguration}
     */
    JsfTestConfiguration[] value();
}
