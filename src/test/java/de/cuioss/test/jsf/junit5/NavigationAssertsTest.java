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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link NavigationAsserts} class.
 * <p>
 * This test class verifies that the NavigationAsserts object can correctly
 * assert navigation outcomes and redirects.
 */
@EnableJsfEnvironment
class NavigationAssertsTest {

    /**
     * Test that {@link NavigationAsserts#assertNavigatedWithOutcome(String)} correctly
     * verifies navigation outcomes.
     */
    @Test
    @DisplayName("Should assert a matching navigation outcome")
    void shouldAssertNavigationOutcome(
        FacesContext facesContext,
        ApplicationConfigDecorator applicationConfig,
        NavigationAsserts navigationAsserts) {
        applicationConfig.registerNavigationCase("testOutcome", "targetViewId");
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "testOutcome");

        navigationAsserts.assertNavigatedWithOutcome("testOutcome");
    }

    /**
     * Test that {@link NavigationAsserts#assertNavigatedWithOutcome(String)} throws
     * an exception when the outcome doesn't match.
     */
    @Test
    @DisplayName("Should fail when the asserted outcome does not match")
    void shouldFailWhenOutcomeDoesntMatch(
        FacesContext facesContext,
        ApplicationConfigDecorator applicationConfig,
        NavigationAsserts navigationAsserts) {
        applicationConfig.registerNavigationCase("testOutcome", "targetViewId");
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "testOutcome");

        Executable assertion = () -> navigationAsserts.assertNavigatedWithOutcome("wrongOutcome");

        assertThrows(AssertionError.class, assertion,
            "Asserting a different outcome should fail");
    }

    /**
     * Test that {@link NavigationAsserts#assertRedirect(String)} correctly
     * verifies redirects.
     */
    @Test
    @DisplayName("Should assert a matching redirect")
    void shouldAssertRedirect(
        ExternalContext externalContext,
        NavigationAsserts navigationAsserts) throws Exception {
        externalContext.redirect("/test/url");

        navigationAsserts.assertRedirect("/test/url");
    }

    /**
     * Test that {@link NavigationAsserts#assertRedirect(String)} throws
     * an exception when the redirect URL doesn't match.
     */
    @Test
    @DisplayName("Should fail when the asserted redirect URL does not match")
    void shouldFailWhenRedirectUrlDoesntMatch(
        ExternalContext externalContext,
        NavigationAsserts navigationAsserts) throws Exception {
        externalContext.redirect("/test/url");

        Executable assertion = () -> navigationAsserts.assertRedirect("/wrong/url");

        assertThrows(AssertionError.class, assertion,
            "Asserting a different redirect URL should fail");
    }
}
