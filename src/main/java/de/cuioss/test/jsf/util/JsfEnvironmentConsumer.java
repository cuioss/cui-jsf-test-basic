package de.cuioss.test.jsf.util;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import de.cuioss.test.jsf.config.JsfTestContextConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.junit5.JsfSetupExtension;

/**
 * Simple interface to be used in context of {@link JsfSetupExtension}. It
 * provides an interface for injecting the configured
 * {@link JsfEnvironmentHolder}. It is needed, when the test needs to access
 * either an {@link JsfTestContextConfigurator} like
 * {@link ComponentConfigDecorator} or {@link RequestConfigDecorator} or a
 * JSF-Object like {@link FacesContext} or {@link Application} programmatically.
 * The typical usage:
 *
 * <pre>
 * <code>
    &#64;Setter
    &#64;Getter
    private JsfEnvironmentHolder environmentHolder;
 * </code>
 *
 * </pre>
 *
 * The actual delegation is implemented using default implementations within
 * this interface, see the unit-test:
 *
 * <pre>
 * <code>
&#64;EnableJsfEnvironment
&#64;JsfTestConfiguration(BasicApplicationConfiguration.class)
class JsfSetupExtensionTest implements JsfEnvironmentConsumer {

    &#64;Setter
    &#64;Getter
    private JsfEnvironmentHolder environmentHolder;

    &#64;Test
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

    &#64;Test
    void shouldApplyBasicConfiguration() {
        assertEquals(BasicApplicationConfiguration.FIREFOX,
                getExternalContext().getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }
 * </code>
 * </pre>
 *
 * @author Oliver Wolff
 *
 */
public interface JsfEnvironmentConsumer {

    /**
     * @param holder to be set, is never null. Will be called from
     *               {@link TestInstancePostProcessor#postProcessTestInstance(Object, org.junit.jupiter.api.extension.ExtensionContext)}
     */
    void setEnvironmentHolder(JsfEnvironmentHolder holder);

    /**
     * @return holder set by {@link #setEnvironmentHolder(JsfEnvironmentHolder)}
     */
    JsfEnvironmentHolder getEnvironmentHolder();

    /**
     * @return an {@link ComponentConfigDecorator} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default ComponentConfigDecorator getComponentConfigDecorator() {
        return getEnvironmentHolder().getComponentConfigDecorator();
    }

    /**
     * @return an {@link BeanConfigDecorator} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default BeanConfigDecorator getBeanConfigDecorator() {
        return getEnvironmentHolder().getBeanConfigDecorator();
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default ApplicationConfigDecorator getApplicationConfigDecorator() {
        return getEnvironmentHolder().getApplicationConfigDecorator();
    }

    /**
     * @return an {@link RequestConfigDecorator} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default RequestConfigDecorator getRequestConfigDecorator() {
        return getEnvironmentHolder().getRequestConfigDecorator();
    }

    /**
     * @return an {@link FacesContext} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default FacesContext getFacesContext() {
        return getEnvironmentHolder().getFacesContext();
    }

    /**
     * @return an {@link Application} for the contained {@link JsfEnvironmentHolder}
     */
    default Application getApplication() {
        return getEnvironmentHolder().getApplication();
    }

    /**
     * @return an {@link ExternalContext} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default ExternalContext getExternalContext() {
        return getEnvironmentHolder().getExternalContext();
    }

    /**
     * @return an {@link MockHttpServletResponse} for the contained
     *         {@link JsfEnvironmentHolder}
     */
    default MockHttpServletResponse getResponse() {
        return getEnvironmentHolder().getResponse();
    }

    /**
     * Asserts whether a navigation was handled by calling
     * {@link NavigationHandler#handleNavigation(javax.faces.context.FacesContext, String, String)}
     *
     * @param outcome must not be null
     */
    default void assertNavigatedWithOutcome(final String outcome) {
        assertNotNull(emptyToNull(outcome), "Outcome must not be null");
        assertTrue(getFacesContext().getExternalContext().isResponseCommitted(), "Response is not committed");
        var handler = getApplicationConfigDecorator().getMockNavigationHandler();
        assertTrue(handler.isHandleNavigationCalled(), "handleNavigation is not called");
        assertEquals(outcome, handler.getCalledOutcome());
    }

    /**
     * Asserts whether a navigation was initialized by calling
     * {@link ExternalContext#redirect(String)}
     *
     * @param redirectUrl must not be null
     */
    default void assertRedirect(final String redirectUrl) {
        assertNotNull(emptyToNull(redirectUrl), "redirectUrl must not be null");
        assertTrue(getFacesContext().getExternalContext().isResponseCommitted(), "Response is not committed");
        var tempResponse = (HttpServletResponse) getExternalContext().getResponse();
        assertTrue(tempResponse.containsHeader("Location"), "Response must provide a header with the name 'Location'");
        assertEquals(redirectUrl, tempResponse.getHeader("Location"));
    }
}
