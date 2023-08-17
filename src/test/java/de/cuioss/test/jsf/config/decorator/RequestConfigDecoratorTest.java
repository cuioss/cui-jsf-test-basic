/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.cuioss.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.test.mock.MockExternalContext22;
import org.apache.myfaces.test.mock.MockFacesContext22;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.util.ConfigurableFacesTest;

class RequestConfigDecoratorTest extends ConfigurableFacesTest {

    private RequestConfigDecorator decorator;

    @BeforeEach
    void before() {
        decorator = new RequestConfigDecorator((MockFacesContext22) getFacesContext(),
                (MockExternalContext22) getExternalContext());
    }

    @Test
    void shouldSetPostaback() {
        assertFalse(getFacesContext().isPostback());
        decorator.setPostback(true);
        assertTrue(getFacesContext().isPostback());
    }

    @Test
    void shouldSetViewRootPath() {
        assertEquals("/viewId", getFacesContext().getViewRoot().getViewId());
        final var viewRoot = "/" + Generators.nonEmptyStrings().next();

        decorator.setViewId(viewRoot);
        assertEquals(viewRoot, getFacesContext().getViewRoot().getViewId());
    }

    @Test
    void shouldRegisterRequestHeader() {
        assertFalse(getExternalContext().getRequestHeaderMap().containsKey(USER_AGENT));

        decorator.setRequestHeader(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, getExternalContext().getRequestHeaderMap().get(USER_AGENT));
    }

    @Test
    void shouldRegisterRequestParameter() {
        assertFalse(getExternalContext().getRequestParameterMap().containsKey(USER_AGENT));

        decorator.setRequestParameter(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, getExternalContext().getRequestParameterMap().get(USER_AGENT));
    }

    @Test
    void shouldRegisterRequestLocales() {
        assertNull(getExternalContext().getRequestLocale());
        assertTrue(getExternalContext().getRequestLocales().hasNext());

        decorator.setRequestLocale();
        assertNull(getExternalContext().getRequestLocale());
        assertFalse(getExternalContext().getRequestLocales().hasNext());

        decorator.setRequestLocale(Locale.GERMAN, Locale.ENGLISH);
        assertEquals(Locale.GERMAN, getExternalContext().getRequestLocale());
        var iterator = getExternalContext().getRequestLocales();
        assertEquals(Locale.GERMAN, iterator.next());
        assertEquals(Locale.ENGLISH, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void shouldRegisterRequestAttribute() {
        assertNull(((HttpServletRequest) getExternalContext().getRequest()).getAttribute(USER_AGENT));
        decorator.setRequestAttribute(USER_AGENT, FIREFOX);
        assertEquals(FIREFOX, ((HttpServletRequest) getExternalContext().getRequest()).getAttribute(USER_AGENT));
    }

    @Test
    void shouldAddCookies() {
        var request = (HttpServletRequest) getExternalContext().getRequest();
        assertEquals(0, request.getCookies().length);
        decorator.addRequestCookie(new Cookie("coo", "kie"));
        assertNotNull(request.getCookies(), "Cookies should not be there");
        assertEquals(1, request.getCookies().length);
    }
}
