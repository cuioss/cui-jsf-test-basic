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
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.mocks.CuiMockSearchExpressionHandler;
import jakarta.faces.application.Application;
import jakarta.faces.component.search.SearchExpressionHandler;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Regression tests for issue #104: {@link CuiMockSearchExpressionHandler} must be
 * the default {@link SearchExpressionHandler} under {@link EnableJsfEnvironment},
 * and the injected {@link Application} parameter must be the same instance used
 * by the renderer (no double-wrap).
 */
@EnableJsfEnvironment
class SearchExpressionHandlerDefaultsTest {

    @Test
    @DisplayName("Should provide a default CuiMockSearchExpressionHandler")
    void defaultSearchExpressionHandlerIsProvided(FacesContext facesContext) {
        assertInstanceOf(CuiMockSearchExpressionHandler.class,
            facesContext.getApplication().getSearchExpressionHandler(),
            "Default SearchExpressionHandler must be a CuiMockSearchExpressionHandler");
        assertNotNull(CuiMockSearchExpressionHandler.retrieve(facesContext));
    }

    @Test
    @DisplayName("Should expose the mock search expression handler via the decorator")
    void decoratorExposesMockSearchExpressionHandler(ApplicationConfigDecorator appConfig) {
        assertNotNull(appConfig.getMockSearchExpressionHandler());
    }

    @Test
    @DisplayName("Should inject the same Application instance used by the FacesContext")
    void injectedApplicationMatchesFacesContextApplication(Application application, FacesContext facesContext) {
        assertSame(application, facesContext.getApplication(),
            "Injected Application parameter must be identical to facesContext.getApplication() — "
                + "otherwise configuration set on the injected param is lost to the renderer (issue #104).");
    }

    @Test
    @DisplayName("Should make configuration on the injected Application visible to the FacesContext")
    void configurationOnInjectedApplicationIsVisibleToFacesContext(Application application, FacesContext facesContext) {
        var custom = new CuiMockSearchExpressionHandler();
        application.setSearchExpressionHandler(custom);
        assertSame(custom, facesContext.getApplication().getSearchExpressionHandler(),
            "Setting a handler on the injected Application must be visible via facesContext.getApplication()");
    }

    @Test
    @DisplayName("Should provide a default CuiMockResourceHandler")
    void defaultResourceHandlerIsProvided(FacesContext facesContext, ApplicationConfigDecorator appConfig) {
        assertInstanceOf(CuiMockResourceHandler.class,
            facesContext.getApplication().getResourceHandler(),
            "Default ResourceHandler must be a CuiMockResourceHandler");
        assertNotNull(appConfig.getMockResourceHandler());
    }
}
