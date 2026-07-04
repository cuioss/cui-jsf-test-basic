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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuiMockServletContext")
class CuiMockServletContextTest {

    @Test
    @DisplayName("Should expose sensible mock defaults")
    void shouldHandleDefaultBehavior() {
        var context = new CuiMockServletContext();

        assertAll("mock servlet context defaults",
            () -> assertEquals(StandardCharsets.UTF_8.name(), context.getRequestCharacterEncoding(),
                "Request encoding should default to UTF-8"),
            () -> assertEquals(StandardCharsets.UTF_8.name(), context.getResponseCharacterEncoding(),
                "Response encoding should default to UTF-8"),
            () -> assertEquals(200, context.getSessionTimeout(), "Session timeout should default to 200"),
            () -> assertEquals("virtual", context.getVirtualServerName(),
                "Virtual server name should default to 'virtual'"),
            () -> assertEquals("/mock-context", context.getContextPath(),
                "Context path should default to '/mock-context'"),
            () -> assertNull(context.getSessionCookieConfig(), "Session cookie config should be null"));
    }

    @Test
    @DisplayName("Should reject adding a JSP file")
    void shouldRejectAddJspFile() {
        var context = new CuiMockServletContext();

        assertThrows(UnsupportedOperationException.class, () -> context.addJspFile("aa", "bb"),
            "addJspFile should not be supported by the mock");
    }

}
