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
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableJsfEnvironment
@DisplayName("CuiMockSearchExpressionContextFactory")
class CuiMockSearchExpressionContextFactoryTest {

    @Test
    @DisplayName("Should be provided by the JSF test setup")
    void shouldBeProvidedBySetup() {
        assertNotNull(CuiMockSearchExpressionContextFactory.retrieve(),
            "Factory should be registered by the test setup");
    }

    @Test
    @DisplayName("Should create and serve search expression contexts")
    void shouldHandleSearchExpressionContext(FacesContext facesContext) {
        var factory = CuiMockSearchExpressionContextFactory.retrieve();

        var inlineCreated = factory.getSearchExpressionContext(facesContext, new CuiMockComponent(),
            Set.of(), Set.of());

        assertNotNull(inlineCreated.getVisitHints(), "Inline-created context should provide visit hints");

        factory.setSearchExpressionContext(
            new CuiMockSearchExpressionContext(new CuiMockComponent(), facesContext, null, null));

        var preconfigured = factory.getSearchExpressionContext(facesContext, new CuiMockComponent(),
            Set.of(), Set.of());

        assertNull(preconfigured.getVisitHints(), "Preconfigured context should serve the provided null hints");
    }

}
