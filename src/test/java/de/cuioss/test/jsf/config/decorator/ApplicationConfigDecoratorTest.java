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
package de.cuioss.test.jsf.config.decorator;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.jsf.util.ConfigurableFacesTest;
import de.cuioss.test.valueobjects.util.IdentityResourceBundle;
import jakarta.faces.application.ProjectStage;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigDecoratorTest extends ConfigurableFacesTest {

    private static final String BUNDLE_PATH = "de.cuioss.test.jsf.testbundle";
    private static final String BUNDLE_NAME = "testbundle";

    private static final String INVALID_BUNDLE_PATH = "de.cuioss.test.jsf.notthere";
    private static final String INVALID_BUNDLE_NAME = "invalidbundle";

    private ApplicationConfigDecorator decorator;

    @BeforeEach
    void before() {
        decorator = new ApplicationConfigDecorator(getApplication(), getFacesContext());
    }

    @Test
    void shouldRegisterResourceBundle() {
        assertNull(getApplication().getResourceBundle(getFacesContext(), BUNDLE_NAME));

        decorator.registerResourceBundle(BUNDLE_NAME, BUNDLE_PATH);

        assertNotNull(getApplication().getResourceBundle(getFacesContext(), BUNDLE_NAME));

        assertNull(getApplication().getResourceBundle(getFacesContext(), INVALID_BUNDLE_NAME));

        decorator.registerResourceBundle(INVALID_BUNDLE_NAME, INVALID_BUNDLE_PATH);

        assertInstanceOf(IdentityResourceBundle.class,
            getApplication().getResourceBundle(getFacesContext(), INVALID_BUNDLE_NAME));
    }

    @Test
    void shouldRegisterSupportedLocales() {
        assertFalse(getApplication().getSupportedLocales().hasNext());

        final var locale = Generators.locales().next();
        decorator.registerSupportedLocales(immutableSet(locale));

        assertEquals(locale, getApplication().getSupportedLocales().next());
    }

    @Test
    void shouldRegisterDefaultLocale() {
        assertNotNull(getApplication().getDefaultLocale());

        final var locale = Generators.locales().next();
        decorator.registerDefaultLocale(locale);

        assertEquals(locale, getApplication().getDefaultLocale());
    }

    @Test
    void shouldRegisterNavigationCase() {
        decorator.registerNavigationCase("outcome", "/toViewId");
        getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, "outcome");
        assertEquals("/toViewId", getFacesContext().getViewRoot().getViewId());
    }

    @Test
    void shouldRegisterContextPath() {
        decorator.setContextPath("hello");
        assertEquals("hello", ((MockHttpServletRequest) getExternalContext().getRequest()).getContextPath());
    }

    @Test
    void shouldSetProjectStage() {
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, getApplication().getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, getApplication().getProjectStage());
    }

    @Test
    void shouldSetInitParameter() {
        final var key = "initKey";
        final var value = "initValue";
        assertNull(getFacesContext().getExternalContext().getInitParameter(key));

        decorator.addInitParameter(key, value);
        assertEquals(value, getFacesContext().getExternalContext().getInitParameter(key));
    }

    @Test
    void shouldSetProjectStageThroughWrapper() {
        decorator = new ApplicationConfigDecorator(new ConfigurableApplication(getApplication()), getFacesContext());
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, getApplication().getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, getApplication().getProjectStage());
    }

}
