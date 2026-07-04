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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuiMockResourceHandler")
class CuiMockResourceHandlerTest {

    @Test
    @DisplayName("Deprecated availableResouces accessors delegate to the correctly-spelled ones (API-2)")
    @SuppressWarnings("deprecation")
    void deprecatedAvailableResourcesAccessorsDelegate() {
        var handler = new CuiMockResourceHandler();
        var map = new HashMap<String, CuiMockResource>();

        handler.setAvailableResouces(map);

        assertSame(map, handler.getAvailableResources(), "The correctly-spelled getter must return the set map");
        assertSame(map, handler.getAvailableResouces(), "The deprecated getter must delegate to the same map");
    }

    @Test
    @DisplayName("createResource resolves against the configured resources map")
    void shouldResolveResourceFromMap() {
        var handler = new CuiMockResourceHandler();

        assertNull(handler.createResource("res", "lib"),
            "An unregistered resource must resolve to null");

        var resource = new CuiMockResource();
        handler.getAvailableResources().put(CuiMockResourceHandler.createResourceMapKey("res", "lib"), resource);
        assertSame(resource, handler.createResource("res", "lib"),
            "A registered resource must be resolved from the map");
    }

    @Test
    @DisplayName("Should derive the resource map key from name and library")
    void shouldCreateResourceSuffix() {
        var key = CuiMockResourceHandler.createResourceMapKey("my", "ressource");

        assertEquals("ressource-my", key, "Key should combine library and resource name");
    }

    @Test
    @DisplayName("Should fall back to defaults for blank name and library")
    void shouldFallBackToDefaultsForBlankInput() {
        var key = CuiMockResourceHandler.createResourceMapKey("", "");

        assertEquals("notThere-notThere", key, "Blank input should map to the default placeholder");
    }

    @Test
    @DisplayName("libraryExists should be true for real libraries and false only for the sentinel (MOCK-1)")
    void shouldReportLibraryExistence() {
        var handler = new CuiMockResourceHandler();

        assertTrue(handler.libraryExists("some.real.library"),
            "A real library must be reported as existing");
        assertFalse(handler.libraryExists(CuiMockResourceHandler.LIBRARY_NOT_THERE),
            "The sentinel library must be reported as not existing");
    }

    @Test
    @DisplayName("getRendererTypeForResourceName returns null for empty input (MOCK-12)")
    void shouldReturnNullRendererTypeForEmptyResourceName() {
        var handler = new CuiMockResourceHandler();

        assertNull(handler.getRendererTypeForResourceName(null),
            "null resource name must not resolve to a renderer type");
        assertNull(handler.getRendererTypeForResourceName(""),
            "empty resource name must not resolve to a renderer type");
        assertEquals("some.js" + CuiMockResourceHandler.RENDERER_SUFFIX,
            handler.getRendererTypeForResourceName("some.js"),
            "non-empty resource name resolves to the synthetic renderer type");
    }

}
