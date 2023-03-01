package de.icw.cui.test.jsf.config;

import de.icw.cui.test.jsf.config.decorator.ComponentConfigDecorator;

/**
 * Instances of this are to be called by the test framework <em>after</em> the initial setup is
 * done.
 *
 * @author Oliver Wolff
 */
public interface ComponentConfigurator extends JsfTestContextConfigurator {

    /**
     * Callback method for interacting with the {@link ComponentConfigDecorator} at the correct
     * time.
     *
     * @param decorator is never null
     */
    void configureComponents(ComponentConfigDecorator decorator);
}
