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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CuiMockHttpSession")
class CuiMockHttpSessionTest {

    @Test
    @DisplayName("invalidate should really invalidate the session (MOCK-4)")
    void shouldInvalidateSession() {
        var session = new CuiMockHttpSession(new CuiMockServletContext());
        session.setAttribute("key", "value");
        assertEquals("value", session.getAttribute("key"), "Attribute should be readable before invalidation");

        session.invalidate();

        assertThrows(IllegalStateException.class, () -> session.getAttribute("key"),
            "Accessing an attribute on an invalidated session must throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> session.setAttribute("key", "other"),
            "Setting an attribute on an invalidated session must throw IllegalStateException");
    }

    @Test
    @DisplayName("maxInactiveInterval should be settable programmatically")
    void shouldSetMaxInactiveInterval() {
        var session = new CuiMockHttpSession(new CuiMockServletContext());
        session.setMaxInactiveInterval(42);
        assertEquals(42, session.getMaxInactiveInterval(), "maxInactiveInterval should be configurable");
    }
}
