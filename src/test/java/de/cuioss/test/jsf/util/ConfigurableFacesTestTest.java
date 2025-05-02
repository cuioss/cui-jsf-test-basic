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

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

@JsfTestConfiguration(BasicApplicationConfiguration.class)
class ConfigurableFacesTestTest extends ConfigurableFacesTest
    implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {

    public static final String TO_VIEW_JSF = "/to/view.jsf";
    public static final String OUTCOME = "outcome";

    private boolean componentsCallbackCalled = false;
    private boolean applicationCallbackCalled = false;
    private boolean requestCallbackCalled = false;

    @Test
    void shouldCallCallbackDecorator() {
        assertTrue(componentsCallbackCalled);
        assertTrue(applicationCallbackCalled);
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
