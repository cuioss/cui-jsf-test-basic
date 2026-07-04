/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.mocks;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.application.NavigationCase;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@DisplayName("CuiMockConfigurableNavigationHandler")
class CuiMockConfigurableNavigationHandlerTest {

    private static final String KNOWN_OUTCOME = "known";

    @Test
    @DisplayName("An unknown outcome must not count as navigation (MOCK-2)")
    void unknownOutcomeIsNoNavigation(FacesContext facesContext) {
        var handler = new CuiMockConfigurableNavigationHandler();

        handler.handleNavigation(facesContext, null, "does-not-exist");

        assertFalse(handler.isHandleNavigationCalled(),
            "An unregistered outcome must not be reported as a handled navigation");
        assertNull(handler.getCalledOutcome(), "No outcome should be recorded for an unknown navigation");
        assertFalse(facesContext.getExternalContext().isResponseCommitted(),
            "An unknown outcome must not commit a redirect response");
    }

    @Test
    @DisplayName("A registered redirect case navigates and redirects (MOCK-3)")
    void registeredRedirectCaseNavigates(FacesContext facesContext) {
        var handler = new CuiMockConfigurableNavigationHandler();
        handler.addNavigationCase(KNOWN_OUTCOME, redirectCase("/target.xhtml"));

        handler.handleNavigation(facesContext, null, KNOWN_OUTCOME);

        assertTrue(handler.isHandleNavigationCalled(), "A registered case must be reported as handled");
        assertEquals(KNOWN_OUTCOME, handler.getCalledOutcome(), "The outcome must be recorded");
        assertTrue(facesContext.getExternalContext().isResponseCommitted(),
            "A registered redirect case must commit a redirect response");
    }

    @Test
    @DisplayName("A non-redirect case updates the view without redirecting (MOCK-3)")
    void nonRedirectCaseDoesNotRedirect(FacesContext facesContext) {
        var handler = new CuiMockConfigurableNavigationHandler();
        handler.addNavigationCase(KNOWN_OUTCOME, nonRedirectCase("/target.xhtml"));

        handler.handleNavigation(facesContext, null, KNOWN_OUTCOME);

        assertTrue(handler.isHandleNavigationCalled(), "A registered case must be reported as handled");
        assertFalse(facesContext.getExternalContext().isResponseCommitted(),
            "A non-redirect case must not commit a redirect response");
    }

    @Test
    @DisplayName("Registering with a fromAction sets the fromAction tracking flag (MOCK-14)")
    void fromActionFlagSetOnlyWithFromAction() {
        var handler = new CuiMockConfigurableNavigationHandler();

        handler.addNavigationCase(KNOWN_OUTCOME, redirectCase("/target.xhtml"));
        assertFalse(handler.isAddNavigationWithFromActionCalled(),
            "The fromAction flag must not be set for the outcome-only overload");

        handler.addNavigationCase("fromAction", KNOWN_OUTCOME, redirectCase("/target.xhtml"));
        assertTrue(handler.isAddNavigationWithFromActionCalled(),
            "The fromAction flag must be set when a fromAction is supplied");
    }

    private static NavigationCase redirectCase(String toViewId) {
        return new NavigationCase(null, null, KNOWN_OUTCOME, null, toViewId, null, null, true, true);
    }

    private static NavigationCase nonRedirectCase(String toViewId) {
        return new NavigationCase(null, null, KNOWN_OUTCOME, null, toViewId, null, null, false, false);
    }
}
