package de.cuioss.test.jsf.config.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VerifyComponentProperties} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@interface VerifyComponentPropertiesRepeat {

    /**
     * @return an array of {@link VerifyComponentProperties}
     */
    VerifyComponentProperties[] value();
}
