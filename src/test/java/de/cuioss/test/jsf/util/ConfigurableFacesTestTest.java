package de.cuioss.test.jsf.util;

import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.DEFAULT_LOCALE;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.SUPPORTED_LOCALES;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;

@JsfTestConfiguration(BasicApplicationConfiguration.class)
class ConfigurableFacesTestTest extends ConfigurableFacesTest
        implements BeanConfigurator, ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {

    public static final String TO_VIEW_JSF = "/to/view.jsf";
    public static final String OUTCOME = "outcome";

    private boolean componentsCallbackCalled = false;
    private boolean applicationCallbackCalled = false;
    private boolean beanCallbackCalled = false;
    private boolean requestCallbackCalled = false;

    @Test
    void shouldCallCallbackDecorator() {
        assertTrue(componentsCallbackCalled);
        assertTrue(applicationCallbackCalled);
        assertTrue(beanCallbackCalled);
        assertTrue(requestCallbackCalled);
    }

    @Test
    void shouldDefaultToIdentityResourceBundle() {
        final var text = Generators.nonEmptyStrings().next();
        getApplicationConfigDecorator().registerResourceBundle("anyBundle", "anyBundle");
        assertEquals(text, getApplication().getResourceBundle(getFacesContext(), "anyBundle").getString(text));
    }

    @Test
    void shouldHavePickedUpBasicConfiguration() {
        assertEquals(SUPPORTED_LOCALES.iterator().next(), getApplication().getSupportedLocales().next());
        assertEquals(DEFAULT_LOCALE, getApplication().getDefaultLocale());
        assertEquals(FIREFOX, getExternalContext().getRequestHeaderMap().get(USER_AGENT));
    }

    @Override
    public void configureComponents(final ComponentConfigDecorator decorator) {
        componentsCallbackCalled = true;
    }

    @Override
    public void configureApplication(final ApplicationConfigDecorator decorator) {
        applicationCallbackCalled = true;
    }

    @Override
    public void configureBeans(final BeanConfigDecorator decorator) {
        beanCallbackCalled = true;
    }

    @Override
    public void configureRequest(final RequestConfigDecorator decorator) {
        requestCallbackCalled = true;
    }

    @Test
    void shouldHandleNavigationOutcome() {
        getApplicationConfigDecorator().registerNavigationCase(OUTCOME, TO_VIEW_JSF);
        getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, OUTCOME);
        assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    void shouldFailHandleNavigationOutcome() {
        assertThrows(AssertionError.class, () -> assertNavigatedWithOutcome(OUTCOME));
    }

    @Test
    void shouldHandleRedirect() throws IOException {
        getExternalContext().redirect(TO_VIEW_JSF);
        assertRedirect(TO_VIEW_JSF);
    }

    @Test
    void shouldFailHandleRedirect() {
        assertThrows(AssertionError.class, () -> assertRedirect(TO_VIEW_JSF));
    }
}
