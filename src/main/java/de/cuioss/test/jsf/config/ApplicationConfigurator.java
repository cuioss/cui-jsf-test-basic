package de.cuioss.test.jsf.config;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;

/**
 * Instances of this are to be called by the test framework <em>after</em> the
 * initial setup is done.
 *
 * @author Oliver Wolff
 */
public interface ApplicationConfigurator extends JsfTestContextConfigurator {

    /**
     * Callback method for interacting with the {@link ApplicationConfigDecorator}
     * at the correct time.
     *
     * @param decorator is never null
     */
    void configureApplication(ApplicationConfigDecorator decorator);
}
