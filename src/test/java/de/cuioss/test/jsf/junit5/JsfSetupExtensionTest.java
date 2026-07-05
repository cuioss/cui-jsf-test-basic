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

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.application.Application;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@JsfTestConfiguration(BasicApplicationConfiguration.class)
@EnableJsfEnvironment
class JsfSetupExtensionTest {

    public static final String TO_VIEW_JSF = "/to/view.jsf";
    public static final String OUTCOME = "outcome";

    @Test
    @DisplayName("Should bootstrap the full JSF environment")
    void shouldBootstrapJsf(JsfEnvironmentHolder environmentHolder, Application application,
        ApplicationConfigDecorator applicationConfig, ComponentConfigDecorator componentConfig,
        ExternalContext externalContext, FacesContext facesContext,
        RequestConfigDecorator requestConfig) {
        assertNotNull(environmentHolder, "JsfEnvironmentHolder should be resolved");
        assertNotNull(application, "Application should be resolved");
        assertNotNull(applicationConfig, "ApplicationConfigDecorator should be resolved");
        assertNotNull(componentConfig, "ComponentConfigDecorator should be resolved");
        assertNotNull(externalContext, "ExternalContext should be resolved");
        assertNotNull(facesContext, "FacesContext should be resolved");
        assertNotNull(requestConfig, "RequestConfigDecorator should be resolved");
        assertNotNull(environmentHolder.getResponse(), "Response should be available");
    }

    @Test
    @DisplayName("Should apply the basic application configuration")
    void shouldApplyBasicConfiguration(ExternalContext externalContext) {
        assertEquals(BasicApplicationConfiguration.FIREFOX,
            externalContext.getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT),
            "User-Agent header should match the configured value");
    }

    @Test
    @DisplayName("Should fail asserting an outcome when no navigation took place")
    void shouldFailForNoNavigationOutcome(NavigationAsserts navigationAsserts) {
        assertThrows(AssertionError.class, () -> navigationAsserts.assertNavigatedWithOutcome(OUTCOME),
            "Asserting an outcome without navigation should fail");
    }

    @Test
    @DisplayName("Should assert a registered navigation outcome")
    void shouldAssertNavigationOutcome(FacesContext facesContext,
        ApplicationConfigDecorator applicationConfig, NavigationAsserts navigationAsserts) {
        applicationConfig.registerNavigationCase(OUTCOME, TO_VIEW_JSF);
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, OUTCOME);

        navigationAsserts.assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    @DisplayName("Should fail asserting a redirect when none took place")
    void shouldFailForNoRedirect(NavigationAsserts navigationAsserts) {
        assertThrows(AssertionError.class, () -> navigationAsserts.assertRedirect(TO_VIEW_JSF),
            "Asserting a redirect without one should fail");
    }

    @Test
    @DisplayName("Should assert a performed redirect")
    void shouldAssertRedirect(ExternalContext externalContext, NavigationAsserts navigationAsserts)
        throws Exception {
        externalContext.redirect(TO_VIEW_JSF);

        navigationAsserts.assertRedirect(TO_VIEW_JSF);
    }

    @Test
    @DisplayName("Should wrap the application in a ConfigurableApplication")
    void shouldWrapConfigurableApplication(Application application) {
        assertEquals(ConfigurableApplication.class, application.getClass(),
            "Application should be wrapped in a ConfigurableApplication");
    }

    @Test
    @DisplayName("Should default to a mirroring resource bundle")
    void shouldDefaultToMirrorResourceBundle(Application application, FacesContext facesContext) {
        ResourceBundleVarNames.addVarName("msg", "msg");

        var resourceBundle = application.getResourceBundle(facesContext, "msg");

        assertNotNull(resourceBundle, "Resource bundle should be resolved");
        assertEquals("some.key", resourceBundle.getString("some.key"),
            "Identity resource bundle should mirror the requested key");
    }
}
