package de.icw.cui.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.config.JsfTestConfiguration;
import de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration;
import de.icw.cui.test.jsf.util.ConfigurableApplication;
import de.icw.cui.test.jsf.util.JsfEnvironmentConsumer;
import de.icw.cui.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

@JsfTestConfiguration(BasicApplicationConfiguration.class)
@EnableJsfEnvironment
class JsfSetupExtensionTest implements JsfEnvironmentConsumer {

    public static final String TO_VIEW_JSF = "/to/view.jsf";
    public static final String OUTCOME = "outcome";

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Test
    void shouldBootstrapJsf() {
        assertNotNull(environmentHolder);
        assertNotNull(getApplication());
        assertNotNull(getApplicationConfigDecorator());
        assertNotNull(getBeanConfigDecorator());
        assertNotNull(getComponentConfigDecorator());
        assertNotNull(getExternalContext());
        assertNotNull(getFacesContext());
        assertNotNull(getRequestConfigDecorator());
        assertNotNull(getResponse());
    }

    @Test
    void shouldApplyBasicConfiguration() {
        assertEquals(BasicApplicationConfiguration.FIREFOX,
                getExternalContext().getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }

    @Test
    void shoudFailForNoNavigationOutcome() {
        // No Navigation took place -> Assertion Error
        assertThrows(AssertionError.class, () -> {
            assertNavigatedWithOutcome(OUTCOME);
        });
    }

    @Test
    void shoudAssertNavigationOutcome() {
        getApplicationConfigDecorator().registerNavigationCase(OUTCOME, TO_VIEW_JSF);
        getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, OUTCOME);
        assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    void shoudFailForNoRedirect() {
        // No Navigation took place -> Assertion Error
        assertThrows(AssertionError.class, () -> {
            assertRedirect(TO_VIEW_JSF);
        });
    }

    @Test
    void shoudAssertRedirect() throws IOException {
        getExternalContext().redirect(TO_VIEW_JSF);
        assertRedirect(TO_VIEW_JSF);
    }

    @Test
    void shouldWrapConfigurableApplication() {
        assertEquals(ConfigurableApplication.class, getApplication().getClass());
    }

    @Test
    void shouldDefaultToMirrorResourceBundle() {
        ResourceBundleVarNames.addVarName("msg", "msg");
        ResourceBundle resourceBundle = getApplication().getResourceBundle(getFacesContext(), "msg");
        assertNotNull(resourceBundle);
        assertEquals("some.key", resourceBundle.getString("some.key"));
    }
}
