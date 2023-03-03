package de.cuioss.test.jsf.config.renderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.cuioss.test.jsf.renderer.AbstractComponentRendererTest;
import de.cuioss.test.jsf.renderer.CommonRendererAsserts;

/**
 * A contract Veto is used for for suppressing certain test-contracts.
 * The default implementation of {@link AbstractComponentRendererTest} tests all contracts that are
 * not vetoed.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VetoRenderAttributeAsserts.class)
public @interface VetoRenderAttributeAssert {

    /**
     * @return the concrete contract to be suppressed / ignored
     */
    CommonRendererAsserts[] value();
}
