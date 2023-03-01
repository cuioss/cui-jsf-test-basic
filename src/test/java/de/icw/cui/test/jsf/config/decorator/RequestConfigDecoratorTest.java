package de.icw.cui.test.jsf.config.decorator;

import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.FIREFOX;
import static de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration.USER_AGENT;
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
import org.junit.Before;
import org.junit.Test;

import de.icw.cui.test.jsf.util.ConfigurableFacesTest;
import io.cui.test.generator.Generators;

class RequestConfigDecoratorTest extends ConfigurableFacesTest {

    private RequestConfigDecorator decorator;

    @Before
    public void before() {
        decorator =
            new RequestConfigDecorator((MockFacesContext22) getFacesContext(),
                    (MockExternalContext22) getExternalContext());
    }

    @Test
    public void shouldSetPostaback() {
        assertFalse(getFacesContext().isPostback());
        decorator.setPostback(true);
        assertTrue(getFacesContext().isPostback());
    }

    @Test
    public void shouldSetViewRootPath() {
        assertEquals("/viewId", getFacesContext().getViewRoot().getViewId());
        final var viewRoot = "/" + Generators.nonEmptyStrings().next();

        decorator.setViewId(viewRoot);
        assertEquals(viewRoot, getFacesContext().getViewRoot().getViewId());
    }

    @Test
    public void shouldRegisterRequestHeader() {
        assertFalse(getExternalContext().getRequestHeaderMap()
                .containsKey(USER_AGENT));

        decorator.setRequestHeader(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, getExternalContext().getRequestHeaderMap().get(USER_AGENT));
    }

    @Test
    public void shouldRegisterRequestParameter() {
        assertFalse(getExternalContext().getRequestParameterMap()
                .containsKey(USER_AGENT));

        decorator.setRequestParameter(USER_AGENT, FIREFOX);

        assertEquals(FIREFOX, getExternalContext().getRequestParameterMap().get(USER_AGENT));
    }

    @Test
    public void shouldRegisterRequestLocales() {
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
    public void shouldRegisterRequestAttribute() {
        assertNull(((HttpServletRequest) getExternalContext().getRequest()).getAttribute(USER_AGENT));
        decorator.setRequestAttribute(USER_AGENT, FIREFOX);
        assertEquals(FIREFOX, ((HttpServletRequest) getExternalContext().getRequest()).getAttribute(USER_AGENT));
    }

    @Test
    public void shouldAddCookies() {
        var request = (HttpServletRequest) getExternalContext().getRequest();
        assertEquals(0, request.getCookies().length);
        decorator.addRequestCookie(new Cookie("coo", "kie"));
        assertNotNull(request.getCookies(), "Cookies should not be there");
        assertEquals(1, request.getCookies().length);
    }
}
