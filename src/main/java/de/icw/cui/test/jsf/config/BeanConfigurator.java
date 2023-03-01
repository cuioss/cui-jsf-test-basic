package de.icw.cui.test.jsf.config;

import javax.inject.Named;

import de.icw.cui.test.jsf.config.decorator.BeanConfigDecorator;

/**
 * Provides a callback method for registering {@link Named} beans for unit-testing
 *
 * @author Oliver Wolff
 */
public interface BeanConfigurator extends JsfTestContextConfigurator {

    /**
     * Callback method for interacting with the {@link BeanConfigDecorator} at the correct
     * time.
     *
     * @param decorator is never null
     */
    void configureBeans(BeanConfigDecorator decorator);
}
