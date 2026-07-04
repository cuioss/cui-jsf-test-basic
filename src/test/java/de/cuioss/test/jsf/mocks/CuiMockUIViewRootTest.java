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

import jakarta.faces.component.html.HtmlInputText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuiMockUIViewRoot")
class CuiMockUIViewRootTest {

    @Test
    @DisplayName("getViewMap(false) returns null until the map is created (MOCK-8)")
    void shouldNotCreateViewMapOnDemand() {
        var root = new CuiMockUIViewRoot();

        assertNull(root.getViewMap(false), "View map must be null until explicitly created");

        var created = root.getViewMap(true);
        assertNotNull(created, "getViewMap(true) must create the map");
        assertSame(created, root.getViewMap(false), "The created map must be returned afterwards");
        assertSame(created, root.getViewMap(), "The no-arg getViewMap must return the created map");
    }

    @Test
    @DisplayName("addUiComponent registers a component resolvable via findComponent")
    void shouldResolveRegisteredComponent() {
        var root = new CuiMockUIViewRoot();
        var component = new HtmlInputText();

        root.addUiComponent("expr", component);

        assertSame(component, root.findComponent("expr"), "Registered component must be resolvable");
    }
}
