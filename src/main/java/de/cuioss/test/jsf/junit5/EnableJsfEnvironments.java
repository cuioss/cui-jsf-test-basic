package de.cuioss.test.jsf.junit5;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link EnableJsfEnvironment} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@interface EnableJsfEnvironments {

    /**
     * @return an array of {@link EnableJsfEnvironment}
     */
    EnableJsfEnvironment[] value();
}
