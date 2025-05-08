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
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

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
    void shouldAssertNavigationOutcome(
            FacesContext facesContext,
            ApplicationConfigDecorator applicationConfig,
            NavigationAsserts navigationAsserts) {
        // Set up navigation
        applicationConfig.registerNavigationCase("testOutcome", "targetViewId");

        // Perform navigation
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "testOutcome");

        // Assert navigation outcome
        navigationAsserts.assertNavigatedWithOutcome("testOutcome");
    }

    /**
     * Test that {@link NavigationAsserts#assertNavigatedWithOutcome(String)} throws
     * an exception when the outcome doesn't match.
     */
    @Test
    void shouldFailWhenOutcomeDoesntMatch(
            FacesContext facesContext,
            ApplicationConfigDecorator applicationConfig,
            NavigationAsserts navigationAsserts) {
        // Set up navigation
        applicationConfig.registerNavigationCase("testOutcome", "targetViewId");

        // Perform navigation
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "testOutcome");

        // Assert that asserting a different outcome fails
        Executable assertion = () -> navigationAsserts.assertNavigatedWithOutcome("wrongOutcome");
        assertThrows(AssertionError.class, assertion);
    }

    /**
     * Test that {@link NavigationAsserts#assertRedirect(String)} correctly
     * verifies redirects.
     */
    @Test
    void shouldAssertRedirect(
            ExternalContext externalContext,
            NavigationAsserts navigationAsserts) throws IOException {
        // Perform redirect
        externalContext.redirect("/test/url");

        // Assert redirect
        navigationAsserts.assertRedirect("/test/url");
    }

    /**
     * Test that {@link NavigationAsserts#assertRedirect(String)} throws
     * an exception when the redirect URL doesn't match.
     */
    @Test
    void shouldFailWhenRedirectUrlDoesntMatch(
            ExternalContext externalContext,
            NavigationAsserts navigationAsserts) throws IOException {
        // Perform redirect
        externalContext.redirect("/test/url");

        // Assert that asserting a different URL fails
        Executable assertion = () -> navigationAsserts.assertRedirect("/wrong/url");
        assertThrows(AssertionError.class, assertion);
    }
}
