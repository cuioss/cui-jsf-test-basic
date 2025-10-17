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
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.application.Application;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the parameter resolution functionality in {@link JsfSetupExtension}.
 * <p>
 * This test class verifies that all supported JSF-related objects can be resolved
 * as parameters in test methods.
 */
@EnableJsfEnvironment
class JsfParameterResolverTest {

    @BeforeEach
    void shouldSetupJsfEnvironment(JsfEnvironmentHolder holder) {
        assertNotNull(holder);
    }

    /**
     * Test that {@link JsfEnvironmentHolder} can be resolved as a parameter.
     */
    @Test
    void shouldResolveJsfEnvironmentHolder(JsfEnvironmentHolder environmentHolder) {
        assertNotNull(environmentHolder, "JsfEnvironmentHolder should be resolved");
    }

    /**
     * Test that {@link FacesContext} can be resolved as a parameter.
     */
    @Test
    void shouldResolveFacesContext(FacesContext facesContext) {
        assertNotNull(facesContext, "FacesContext should be resolved");
    }

    /**
     * Test that {@link ExternalContext} can be resolved as a parameter.
     */
    @Test
    void shouldResolveExternalContext(ExternalContext externalContext) {
        assertNotNull(externalContext, "ExternalContext should be resolved");
    }

    /**
     * Test that {@link Application} can be resolved as a parameter.
     */
    @Test
    void shouldResolveApplication(Application application) {
        assertNotNull(application, "Application should be resolved");
    }

    /**
     * Test that {@link RequestConfigDecorator} can be resolved as a parameter.
     */
    @Test
    void shouldResolveRequestConfigDecorator(RequestConfigDecorator requestConfigDecorator) {
        assertNotNull(requestConfigDecorator, "RequestConfigDecorator should be resolved");
    }

    /**
     * Test that {@link ApplicationConfigDecorator} can be resolved as a parameter.
     */
    @Test
    void shouldResolveApplicationConfigDecorator(ApplicationConfigDecorator applicationConfigDecorator) {
        assertNotNull(applicationConfigDecorator, "ApplicationConfigDecorator should be resolved");
    }

    /**
     * Test that {@link ComponentConfigDecorator} can be resolved as a parameter.
     */
    @Test
    void shouldResolveComponentConfigDecorator(ComponentConfigDecorator componentConfigDecorator) {
        assertNotNull(componentConfigDecorator, "ComponentConfigDecorator should be resolved");
    }

    /**
     * Test that {@link MockHttpServletResponse} can be resolved as a parameter.
     */
    @Test
    void shouldResolveMockHttpServletResponse(MockHttpServletResponse response) {
        assertNotNull(response, "MockHttpServletResponse should be resolved");
    }

    /**
     * Test that multiple parameters can be resolved in a single test method.
     */
    @Test
    void shouldResolveMultipleParameters(
        FacesContext facesContext,
        Application application,
        ComponentConfigDecorator componentConfigDecorator) {
        assertNotNull(facesContext, "FacesContext should be resolved");
        assertNotNull(application, "Application should be resolved");
        assertNotNull(componentConfigDecorator, "ComponentConfigDecorator should be resolved");
    }

    /**
     * Test that {@link MockHttpServletRequest} can be resolved as a parameter.
     */
    @Test
    void shouldResolveMockHttpServletRequest(MockHttpServletRequest request) {
        assertNotNull(request, "MockHttpServletRequest should be resolved");
    }

    /**
     * Test that {@link NavigationAsserts} can be resolved as a parameter.
     */
    @Test
    void shouldResolveNavigationAsserts(NavigationAsserts navigationAsserts) {
        assertNotNull(navigationAsserts, "NavigationAsserts should be resolved");
    }
}
