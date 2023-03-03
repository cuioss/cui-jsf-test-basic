package de.cuioss.test.jsf.config.renderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.component.html.HtmlForm;

import de.cuioss.test.jsf.renderer.AbstractComponentRendererTest;

/**
 * This annotation can be used for further configuration of tests based on
 * {@link AbstractComponentRendererTest}
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyComponentRendererConfig {

    /**
     * @return boolean indicating whether the test should wrap the component to be tested into a
     *         {@link HtmlForm} prior to rendering. Defaults to {@code false}, The form itself will
     *         not be rendered
     */
    boolean wrapComponentInForm() default false;
}
