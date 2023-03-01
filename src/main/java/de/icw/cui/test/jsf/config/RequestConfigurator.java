package de.icw.cui.test.jsf.config;

import de.icw.cui.test.jsf.config.decorator.RequestConfigDecorator;

/**
 * Instances of this are to be called by the test framework <em>after</em> the initial setup is
 * done.
 * 
 * @author Oliver Wolff
 */
public interface RequestConfigurator extends JsfTestContextConfigurator {

    /**
     * Callback method for interacting with the {@link RequestConfigDecorator} at the correct
     * time.
     * 
     * @param decorator is never null
     */
    void configureRequest(RequestConfigDecorator decorator);
}
