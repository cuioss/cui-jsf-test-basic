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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility class for asserting navigation outcomes and redirects in JSF tests.
 * <p>
 * This class is designed to be used with parameter resolution in JUnit 5 tests.
 * It provides methods for asserting that navigation was handled with a specific
 * outcome or that a redirect was initiated to a specific URL.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @EnableJsfEnvironment
 * class MyTest {
 *
 *     @Test
 *     void testNavigation(FacesContext facesContext, ApplicationConfigDecorator applicationConfig,
 *                         NavigationAsserts navigationAsserts) {
 *         // Set up navigation
 *         applicationConfig.registerNavigationCase("viewId", "outcome", "targetViewId");
 *
 *         // Perform navigation
 *         facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "outcome");
 *
 *         // Assert navigation outcome
 *         navigationAsserts.assertNavigatedWithOutcome("outcome");
 *     }
 *
 *     @Test
 *     void testRedirect(FacesContext facesContext, ExternalContext externalContext,
 *                       NavigationAsserts navigationAsserts) throws IOException {
 *         // Perform redirect
 *         externalContext.redirect("/some/url");
 *
 *         // Assert redirect
 *         navigationAsserts.assertRedirect("/some/url");
 *     }
 * }
 * }
 * </pre>
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class NavigationAsserts {

    private final FacesContext facesContext;
    private final ExternalContext externalContext;
    private final ApplicationConfigDecorator applicationConfig;

    /**
     * Asserts whether a navigation was handled by calling
     * {@link NavigationHandler#handleNavigation(jakarta.faces.context.FacesContext, String, String)}
     *
     * @param outcome must not be null
     */
    public void assertNavigatedWithOutcome(String outcome) {
        assertNotNull(emptyToNull(outcome), "Outcome must not be null");
        assertTrue(facesContext.getExternalContext().isResponseCommitted(), "Response is not committed");
        var handler = applicationConfig.getMockNavigationHandler();
        assertTrue(handler.isHandleNavigationCalled(), "handleNavigation is not called");
        assertEquals(outcome, handler.getCalledOutcome());
    }

    /**
     * Asserts whether a navigation was initialized by calling
     * {@link ExternalContext#redirect(String)}
     *
     * @param redirectUrl must not be null
     */
    public void assertRedirect(String redirectUrl) {
        assertNotNull(emptyToNull(redirectUrl), "redirectUrl must not be null");
        assertTrue(facesContext.getExternalContext().isResponseCommitted(), "Response is not committed");
        var tempResponse = (HttpServletResponse) externalContext.getResponse();
        assertTrue(tempResponse.containsHeader("Location"), "Response must provide a header with the name 'Location'");
        assertEquals(redirectUrl, tempResponse.getHeader("Location"));
    }
}