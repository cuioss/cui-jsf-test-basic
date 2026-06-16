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
package de.cuioss.test.jsf.util;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.junit5.NavigationAsserts;
import jakarta.faces.application.Application;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.DEFAULT_LOCALE;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.SUPPORTED_LOCALES;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies the JSF test environment provided by {@link EnableJsfEnvironment} together with a
 * {@link JsfTestConfiguration}: the configuration declared by {@link BasicApplicationConfiguration}
 * is applied, the {@code IdentityResourceBundle} is active by default, and navigation outcomes and
 * redirects can be asserted via {@link NavigationAsserts}.
 * <p>
 * Uses JUnit 5 parameter resolution exclusively — no deprecated base class or configurator
 * interfaces.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(BasicApplicationConfiguration.class)
class ConfigurableFacesTestTest {

    private static final String TO_VIEW_JSF = "/to/view.jsf";
    private static final String OUTCOME = "outcome";

    @Test
    void shouldDefaultToIdentityResourceBundle(Application application, FacesContext facesContext,
        ApplicationConfigDecorator applicationConfig) {
        var text = Generators.nonEmptyStrings().next();
        applicationConfig.registerResourceBundle("anyBundle", "anyBundle");

        var resolved = application.getResourceBundle(facesContext, "anyBundle").getString(text);

        assertEquals(text, resolved, "IdentityResourceBundle should return the key itself");
    }

    @Test
    void shouldHavePickedUpBasicConfiguration(Application application, ExternalContext externalContext) {
        assertEquals(SUPPORTED_LOCALES.iterator().next(), application.getSupportedLocales().next(),
            "First supported locale should match the basic configuration");
        assertEquals(DEFAULT_LOCALE, application.getDefaultLocale(),
            "Default locale should match the basic configuration");
        assertEquals(FIREFOX, externalContext.getRequestHeaderMap().get(USER_AGENT),
            "User-agent header should match the basic configuration");
    }

    @Test
    void shouldHandleNavigationOutcome(FacesContext facesContext, ApplicationConfigDecorator applicationConfig,
        NavigationAsserts navigationAsserts) {
        applicationConfig.registerNavigationCase(OUTCOME, TO_VIEW_JSF);

        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, OUTCOME);

        navigationAsserts.assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    void shouldFailHandleNavigationOutcome(NavigationAsserts navigationAsserts) {
        assertThrows(AssertionError.class, () -> navigationAsserts.assertNavigatedWithOutcome(OUTCOME));
    }

    @Test
    void shouldHandleRedirect(ExternalContext externalContext, NavigationAsserts navigationAsserts) throws Exception {
        externalContext.redirect(TO_VIEW_JSF);

        navigationAsserts.assertRedirect(TO_VIEW_JSF);
    }

    @Test
    void shouldFailHandleRedirect(NavigationAsserts navigationAsserts) {
        assertThrows(AssertionError.class, () -> navigationAsserts.assertRedirect(TO_VIEW_JSF));
    }
}
