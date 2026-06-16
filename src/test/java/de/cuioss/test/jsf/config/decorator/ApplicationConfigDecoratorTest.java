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
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.valueobjects.util.IdentityResourceBundle;
import jakarta.faces.application.Application;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableJsfEnvironment
@DisplayName("ApplicationConfigDecorator")
class ApplicationConfigDecoratorTest {

    private static final String BUNDLE_PATH = "de.cuioss.test.jsf.testbundle";
    private static final String BUNDLE_NAME = "testbundle";

    private static final String INVALID_BUNDLE_PATH = "de.cuioss.test.jsf.notthere";
    private static final String INVALID_BUNDLE_NAME = "invalidbundle";

    @Test
    @DisplayName("Should register a resource bundle and fall back to IdentityResourceBundle for invalid paths")
    void shouldRegisterResourceBundle(ApplicationConfigDecorator decorator, Application application,
            FacesContext facesContext) {
        assertNull(application.getResourceBundle(facesContext, BUNDLE_NAME));

        decorator.registerResourceBundle(BUNDLE_NAME, BUNDLE_PATH);

        assertNotNull(application.getResourceBundle(facesContext, BUNDLE_NAME));

        assertNull(application.getResourceBundle(facesContext, INVALID_BUNDLE_NAME));

        decorator.registerResourceBundle(INVALID_BUNDLE_NAME, INVALID_BUNDLE_PATH);

        assertInstanceOf(IdentityResourceBundle.class,
            application.getResourceBundle(facesContext, INVALID_BUNDLE_NAME));
    }

    @Test
    @DisplayName("Should register supported locales")
    void shouldRegisterSupportedLocales(ApplicationConfigDecorator decorator, Application application) {
        assertFalse(application.getSupportedLocales().hasNext());

        final var locale = Generators.locales().next();
        decorator.registerSupportedLocales(immutableSet(locale));

        assertEquals(locale, application.getSupportedLocales().next());
    }

    @Test
    @DisplayName("Should register the default locale")
    void shouldRegisterDefaultLocale(ApplicationConfigDecorator decorator, Application application) {
        assertNotNull(application.getDefaultLocale());

        final var locale = Generators.locales().next();
        decorator.registerDefaultLocale(locale);

        assertEquals(locale, application.getDefaultLocale());
    }

    @Test
    @DisplayName("Should register a navigation case")
    void shouldRegisterNavigationCase(ApplicationConfigDecorator decorator, Application application,
            FacesContext facesContext) {
        decorator.registerNavigationCase("outcome", "/toViewId");
        application.getNavigationHandler().handleNavigation(facesContext, null, "outcome");
        assertEquals("/toViewId", facesContext.getViewRoot().getViewId());
    }

    @Test
    @DisplayName("Should register the context path")
    void shouldRegisterContextPath(ApplicationConfigDecorator decorator, ExternalContext externalContext) {
        decorator.setContextPath("hello");
        assertEquals("hello", ((MockHttpServletRequest) externalContext.getRequest()).getContextPath());
    }

    @Test
    @DisplayName("Should set the project stage")
    void shouldSetProjectStage(ApplicationConfigDecorator decorator, Application application) {
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, application.getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, application.getProjectStage());
    }

    @Test
    @DisplayName("Should set an init parameter")
    void shouldSetInitParameter(ApplicationConfigDecorator decorator, FacesContext facesContext) {
        final var key = "initKey";
        final var value = "initValue";
        assertNull(facesContext.getExternalContext().getInitParameter(key));

        decorator.addInitParameter(key, value);
        assertEquals(value, facesContext.getExternalContext().getInitParameter(key));
    }

    @Test
    @DisplayName("Should set the project stage through a ConfigurableApplication wrapper")
    void shouldSetProjectStageThroughWrapper(Application application, FacesContext facesContext) {
        final var decorator = new ApplicationConfigDecorator(new ConfigurableApplication(application), facesContext);
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, application.getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, application.getProjectStage());
    }

}
