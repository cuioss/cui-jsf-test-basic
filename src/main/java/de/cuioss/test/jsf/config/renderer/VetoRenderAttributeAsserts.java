package de.cuioss.test.jsf.config.renderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VetoRenderAttributeAssert} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@interface VetoRenderAttributeAsserts {

    /**
     * @return an array of {@link VetoRenderAttributeAssert}.
     */
    VetoRenderAttributeAssert[] value();
}
