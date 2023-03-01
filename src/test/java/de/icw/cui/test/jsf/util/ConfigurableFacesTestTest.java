package de.icw.cui.test.jsf.util;

import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.DEFAULT_LOCALE;
import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.SUPPORTED_LOCALES;
import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.icw.cui.test.jsf.config.ApplicationConfigurator;
import de.icw.cui.test.jsf.config.BeanConfigurator;
import de.icw.cui.test.jsf.config.ComponentConfigurator;
import de.icw.cui.test.jsf.config.JsfTestConfiguration;
import de.icw.cui.test.jsf.config.RequestConfigurator;
import de.icw.cui.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.BeanConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.ComponentConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.RequestConfigDecorator;
import de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration;
import io.cui.test.generator.Generators;

@SuppressWarnings("javadoc")
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
    public void shouldCallCallbackDecorator() {
        assertTrue(componentsCallbackCalled);
        assertTrue(applicationCallbackCalled);
        assertTrue(beanCallbackCalled);
        assertTrue(requestCallbackCalled);
    }

    @Test
    public void shouldDefaultToIdentityResourceBundle() {
        final String text = Generators.nonEmptyStrings().next();
        getApplicationConfigDecorator().registerResourceBundle("anyBundle", "anyBundle");
        assertEquals(text,
                getApplication().getResourceBundle(getFacesContext(), "anyBundle").getString(text));
    }

    @Test
    public void shouldHavePickedUpBasicConfiguration() {
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
    public void shouldHandleNavigationOutcome() {
        getApplicationConfigDecorator().registerNavigationCase(OUTCOME, TO_VIEW_JSF);
        getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, OUTCOME);
        assertNavigatedWithOutcome(OUTCOME);
    }

    @Test(expected = AssertionError.class)
    public void shouldFailHandleNavigationOutcome() {
        assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    public void shouldHandleRedirect() throws IOException {
        getExternalContext().redirect(TO_VIEW_JSF);
        assertRedirect(TO_VIEW_JSF);
    }

    @Test(expected = AssertionError.class)
    public void shouldFailHandleRedirect() {
        assertRedirect(TO_VIEW_JSF);
    }
}
