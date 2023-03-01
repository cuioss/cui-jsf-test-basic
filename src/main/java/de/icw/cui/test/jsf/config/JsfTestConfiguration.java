package de.icw.cui.test.jsf.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotations can be used to configure certain aspects of the configuration of the jsf-runtime
 * for unit-testing. For simple cases the test-class can implement the corresponding interfaces
 * itself: {@link ApplicationConfigurator}, {@link ComponentConfigurator},
 * {@link RequestConfigurator} or {@link BeanConfigurator}. The base-class will ensure that the
 * corresponding methods will be called at setup-time. This is useful for cases, where there is no
 * need to reuse a certain configuration.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(JsfTestConfigurations.class)
public @interface JsfTestConfiguration {

    /**
     * @return one or more concrete instances of {@link JsfTestContextConfigurator}
     */
    Class<? extends JsfTestContextConfigurator>[] value();

}
