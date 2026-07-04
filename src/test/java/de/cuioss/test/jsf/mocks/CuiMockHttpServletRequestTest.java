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

import org.apache.myfaces.test.mock.MockHttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuiMockHttpServletRequest")
class CuiMockHttpServletRequestTest {

    private CuiMockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new CuiMockHttpServletRequest();
        request.setServletContext(new CuiMockServletContext());
    }

    @Test
    @DisplayName("getSession(false) returns null when no session exists (MOCK-6)")
    void shouldReturnNullForReadOnlyWithoutSession() {
        assertNull(request.getSession(false), "No session must exist yet");
    }

    @Test
    @DisplayName("getSession(true) creates a CuiMockHttpSession and returns it consistently")
    void shouldCreateCuiSession() {
        var session = request.getSession(true);
        assertNotNull(session, "A session must be created");
        assertInstanceOf(CuiMockHttpSession.class, session, "The created session must be a CuiMockHttpSession");
        assertSame(session, request.getSession(false), "The same session must be returned afterwards");
    }

    @Test
    @DisplayName("An invalidated session is dropped in the read-only path (MOCK-6)")
    void shouldDropInvalidatedSession() {
        var session = request.getSession(true);
        session.invalidate();
        assertNull(request.getSession(false), "An invalidated session must not be returned");
    }

    @Test
    @DisplayName("A foreign session is preserved instead of being replaced (MOCK-6)")
    void shouldPreserveForeignSession() {
        var foreign = new MockHttpSession(new CuiMockServletContext());
        request.setHttpSession(foreign);
        assertSame(foreign, request.getSession(false), "A foreign session must be preserved");
        assertSame(foreign, request.getSession(true), "A foreign session must not be replaced on create");
    }

    @Test
    @DisplayName("getLocales falls back to the default locale when none configured (MOCK-7)")
    void shouldFallBackToDefaultLocale() {
        request.setRequestLocales(List.of());
        var locales = request.getLocales();
        assertTrue(locales.hasMoreElements(), "getLocales must not be empty");
        assertEquals(Locale.getDefault(), locales.nextElement(), "Falls back to the server default locale");
    }

    @Test
    @DisplayName("getLocales returns the configured locales when present")
    void shouldReturnConfiguredLocales() {
        request.setRequestLocales(List.of(Locale.GERMAN, Locale.ENGLISH));
        var locales = request.getLocales();
        assertEquals(Locale.GERMAN, locales.nextElement());
        assertEquals(Locale.ENGLISH, locales.nextElement());
        assertFalse(locales.hasMoreElements());
    }

    @Test
    @DisplayName("getLocales falls back to the default locale when the list is null (MOCK-7)")
    void shouldFallBackWhenLocalesNull() {
        request.setRequestLocales(null);
        var locales = request.getLocales();
        assertTrue(locales.hasMoreElements(), "getLocales must not be empty for a null list");
        assertEquals(Locale.getDefault(), locales.nextElement());
    }

    @Test
    @DisplayName("getRequestURL reflects the configured request URL")
    void shouldExposeRequestUrl() {
        assertEquals(CuiMockHttpServletRequest.SERVLET_REQUEST_URL, request.getRequestURL().toString());
        request.setRequestUrl("http://example.com/foo");
        assertEquals("http://example.com/foo", request.getRequestURL().toString());
    }
}
