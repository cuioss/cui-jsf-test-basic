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
package de.cuioss.test.jsf.config.decorator;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableJsfEnvironment
@DisplayName("RequestConfigDecorator")
class RequestConfigDecoratorTest {

    @Test
    @DisplayName("Should set the postback flag")
    void shouldSetPostaback(RequestConfigDecorator decorator, FacesContext facesContext) {
        assertFalse(facesContext.isPostback());
        decorator.setPostback(true);
        assertTrue(facesContext.isPostback());
    }

    @Test
    @DisplayName("Should set the view root path")
    void shouldSetViewRootPath(RequestConfigDecorator decorator, FacesContext facesContext) {
        assertEquals("/viewId", facesContext.getViewRoot().getViewId());
        final var viewRoot = "/" + Generators.nonEmptyStrings().next();

        decorator.setViewId(viewRoot);
        assertEquals(viewRoot, facesContext.getViewRoot().getViewId());
    }

    @Test
    @DisplayName("Should register a request header")
    void shouldRegisterRequestHeader(RequestConfigDecorator decorator, ExternalContext externalContext) {
        assertFalse(externalContext.getRequestHeaderMap().containsKey(USER_AGENT));

        decorator.setRequestHeader(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, externalContext.getRequestHeaderMap().get(USER_AGENT));
    }

    @Test
    @DisplayName("Should register a request parameter")
    void shouldRegisterRequestParameter(RequestConfigDecorator decorator, ExternalContext externalContext) {
        assertFalse(externalContext.getRequestParameterMap().containsKey(USER_AGENT));

        decorator.setRequestParameter(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, externalContext.getRequestParameterMap().get(USER_AGENT));
    }

    @Test
    @DisplayName("Should register request locales")
    void shouldRegisterRequestLocales(RequestConfigDecorator decorator, ExternalContext externalContext) {
        assertNull(externalContext.getRequestLocale());
        assertTrue(externalContext.getRequestLocales().hasNext());

        decorator.setRequestLocale();
        assertNull(externalContext.getRequestLocale());
        // MOCK-7: getLocales must never return an empty enumeration; it falls back to
        // the server default locale as required by the servlet specification.
        var fallbackLocales = externalContext.getRequestLocales();
        assertTrue(fallbackLocales.hasNext(),
            "getRequestLocales must fall back to the default locale instead of being empty");
        assertEquals(Locale.getDefault(), fallbackLocales.next());

        decorator.setRequestLocale(Locale.GERMAN, Locale.ENGLISH);
        assertEquals(Locale.GERMAN, externalContext.getRequestLocale());
        var iterator = externalContext.getRequestLocales();
        assertEquals(Locale.GERMAN, iterator.next());
        assertEquals(Locale.ENGLISH, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    @DisplayName("Should register a request attribute")
    void shouldRegisterRequestAttribute(RequestConfigDecorator decorator, ExternalContext externalContext) {
        assertNull(((HttpServletRequest) externalContext.getRequest()).getAttribute(USER_AGENT));
        decorator.setRequestAttribute(USER_AGENT, FIREFOX);
        assertEquals(FIREFOX, ((HttpServletRequest) externalContext.getRequest()).getAttribute(USER_AGENT));
    }

    @Test
    @DisplayName("Should add cookies to the request")
    void shouldAddCookies(RequestConfigDecorator decorator, ExternalContext externalContext) {
        var request = (HttpServletRequest) externalContext.getRequest();
        assertEquals(0, request.getCookies().length);
        decorator.addRequestCookie(new Cookie("coo", "kie"));
        assertNotNull(request.getCookies(), "Cookies should not be there");
        assertEquals(1, request.getCookies().length);
    }

    @Test
    @DisplayName("Should set the query string and return itself for fluent chaining (LIFE-9)")
    void shouldSetQueryString(RequestConfigDecorator decorator, ExternalContext externalContext) {
        var result = decorator.setQueryString("foo=bar");

        assertSame(decorator, result, "setQueryString must return the decorator to enable fluent chaining");
        assertEquals("foo=bar", ((HttpServletRequest) externalContext.getRequest()).getQueryString(),
            "The query string should be set on the request");
    }
}
