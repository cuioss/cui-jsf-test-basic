/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.util;

import de.cuioss.test.jsf.config.JsfTestContextConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.junit5.JsfSetupExtension;
import jakarta.faces.application.Application;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.*;

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
 * &#64;Setter
 * &#64;Getter
 * private JsfEnvironmentHolder environmentHolder;
 * </code>
 *
 * </pre>
 * <p>
 * The actual delegation is implemented using default implementations within
 * this interface, see the unit-test:
 *
 * <pre>
 * <code>
 * &#64;EnableJsfEnvironment
 * &#64;JsfTestConfiguration(BasicApplicationConfiguration.class)
 * class JsfSetupExtensionTest implements JsfEnvironmentConsumer {
 *
 * &#64;Setter
 * &#64;Getter
 * private JsfEnvironmentHolder environmentHolder;
 *
 * &#64;Test
 * void shouldBootstrapJsf() {
 * assertNotNull(environmentHolder);
 * assertNotNull(getApplication());
 * assertNotNull(getApplicationConfigDecorator());
 * assertNotNull(getBeanConfigDecorator());
 * assertNotNull(getComponentConfigDecorator());
 * assertNotNull(getExternalContext());
 * assertNotNull(getFacesContext());
 * assertNotNull(getRequestConfigDecorator());
 * assertNotNull(getResponse());
 * }
 *
 * &#64;Test
 * void shouldApplyBasicConfiguration() {
 * assertEquals(BasicApplicationConfiguration.FIREFOX,
 * getExternalContext().getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
 * }
 * </code>
 * </pre>
 * <p>
 * <strong>Deprecation Notice:</strong> This interface is deprecated in favor of using parameter resolution
 * for JSF-related objects in test methods. Instead of implementing this interface and accessing JSF objects
 * through getter methods, you can now directly declare the JSF objects you need as parameters in your test methods.
 * <p>
 * Migration example:
 * <pre>
 * <code>
 * // Old approach (deprecated)
 * &#64;EnableJsfEnvironment
 * class MyTest implements JsfEnvironmentConsumer {
 *
 *     &#64;Setter
 *     &#64;Getter
 *     private JsfEnvironmentHolder environmentHolder;
 *
 *     &#64;Test
 *     void testSomething() {
 *         FacesContext facesContext = getFacesContext();
 *         // Test code using facesContext
 *     }
 * }
 *
 * // New approach (recommended)
 * &#64;EnableJsfEnvironment
 * class MyTest {
 *
 *     &#64;Test
 *     void testSomething(FacesContext facesContext) {
 *         // Test code using facesContext
 *     }
 * }
 * </code>
 * </pre>
 *
 * @author Oliver Wolff
 * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
 */
@Deprecated(since = "4.1", forRemoval = true)
public interface JsfEnvironmentConsumer {

    /**
     * @return holder set by {@link #setEnvironmentHolder(JsfEnvironmentHolder)}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    JsfEnvironmentHolder getEnvironmentHolder();

    /**
     * @param holder to be set, is never null. Will be called from
     *               {@link TestInstancePostProcessor#postProcessTestInstance(Object, org.junit.jupiter.api.extension.ExtensionContext)}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    void setEnvironmentHolder(JsfEnvironmentHolder holder);

    /**
     * @return an {@link ComponentConfigDecorator} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default ComponentConfigDecorator getComponentConfigDecorator() {
        return getEnvironmentHolder().getComponentConfigDecorator();
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default ApplicationConfigDecorator getApplicationConfigDecorator() {
        return getEnvironmentHolder().getApplicationConfigDecorator();
    }

    /**
     * @return an {@link RequestConfigDecorator} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default RequestConfigDecorator getRequestConfigDecorator() {
        return getEnvironmentHolder().getRequestConfigDecorator();
    }

    /**
     * @return an {@link FacesContext} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default FacesContext getFacesContext() {
        return getEnvironmentHolder().getFacesContext();
    }

    /**
     * @return an {@link Application} for the contained {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default Application getApplication() {
        return getEnvironmentHolder().getApplication();
    }

    /**
     * @return an {@link ExternalContext} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default ExternalContext getExternalContext() {
        return getEnvironmentHolder().getExternalContext();
    }

    /**
     * @return an {@link MockHttpServletResponse} for the contained
     * {@link JsfEnvironmentHolder}
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default MockHttpServletResponse getResponse() {
        return getEnvironmentHolder().getResponse();
    }

    /**
     * Asserts whether a navigation was handled by calling
     * {@link NavigationHandler#handleNavigation(jakarta.faces.context.FacesContext, String, String)}
     *
     * @param outcome must not be null
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
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
     * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
     */
    @Deprecated(since = "4.1", forRemoval = true)
    default void assertRedirect(final String redirectUrl) {
        assertNotNull(emptyToNull(redirectUrl), "redirectUrl must not be null");
        assertTrue(getFacesContext().getExternalContext().isResponseCommitted(), "Response is not committed");
        var tempResponse = (HttpServletResponse) getExternalContext().getResponse();
        assertTrue(tempResponse.containsHeader("Location"), "Response must provide a header with the name 'Location'");
        assertEquals(redirectUrl, tempResponse.getHeader("Location"));
    }
}