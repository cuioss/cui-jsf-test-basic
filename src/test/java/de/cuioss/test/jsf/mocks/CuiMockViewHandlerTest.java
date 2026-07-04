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

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.component.html.HtmlOutputText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuiMockViewHandler")
class CuiMockViewHandlerTest {

    @Test
    @DisplayName("Should return a view declaration language for any parameters")
    void shouldReturnVdlForAnyParameter() {
        var handler = new CuiMockViewHandler();

        assertNotNull(handler.getViewDeclarationLanguage(null, null),
            "View declaration language should never be null");
    }

    @Test
    @DisplayName("Should silently register any composite component")
    void shouldRegisterAnyCompositeComponent() {
        var handler = new CuiMockViewHandler();

        assertDoesNotThrow(() -> handler.registerCompositeComponent(null, null, null),
            "Registering a composite component should not throw");
    }

    @Test
    @DisplayName("Should support registering more than one composite component (MOCK-5)")
    void shouldRegisterMultipleCompositeComponents() {
        var handler = new CuiMockViewHandler();
        UIComponent first = new HtmlInputText();
        UIComponent second = new HtmlOutputText();

        handler.registerCompositeComponent("libA", "tagA", first);
        // A second registration on the same handler must not throw (regression for MOCK-5)
        assertDoesNotThrow(() -> handler.registerCompositeComponent("libB", "tagB", second),
            "Registering a second composite component must be supported");

        var vdl = handler.getViewDeclarationLanguage(null, null);
        assertSame(first, vdl.createComponent(null, "libA", "tagA", null),
            "First composite component must be resolvable");
        assertSame(second, vdl.createComponent(null, "libB", "tagB", null),
            "Second composite component must be resolvable");
        assertNull(vdl.createComponent(null, "libX", "tagX", null),
            "Unknown composite component must resolve to null");
    }

}
