package de.cuioss.test.jsf.mocks;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.Part;

import org.apache.myfaces.test.mock.MockHttpServletRequest;

import lombok.Setter;

/**
 * Extends {@link MockHttpServletRequest} and provides some additional convenience methods
 *
 * @author Oliver Wolff
 */
public class CuiMockHttpServletRequest extends MockHttpServletRequest {

    /** "http://localhost:8080/servletRequestUrl/" */
    public static final String SERVLET_REQUEST_URL = "http://localhost:8080/servletRequestUrl/";

    @Setter
    private List<Locale> requestLocales = mutableList(Locale.getDefault());

    @Setter
    private String requestUrl = SERVLET_REQUEST_URL;

    @SuppressWarnings("rawtypes") // API defined
    @Override
    public Enumeration getLocales() {
        return new Vector<>(requestLocales).elements();
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(requestUrl);
    }

    /* Servlet 3+ api */
    @Override
    public boolean authenticate(final HttpServletResponse response) {
        return false;
    }

    @Override
    public Part getPart(final String name) {
        return null;
    }

    @Override
    public Collection<Part> getParts() {
        return Collections.emptyList();
    }

    @Override
    public void login(final String username, final String password) {
        // No login for mock
    }

    @Override
    public void logout() {
        // No logout for mock
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext startAsync() {
        return null;
    }

    @Override
    public AsyncContext startAsync(final ServletRequest request, final ServletResponse response) {
        return null;
    }

    /**
     * getSession should never return an invalidated session.
     */
    @Override
    public HttpSession getSession(final boolean create) {
        HttpSession session = null;
        if (!create) {
            session = super.getSession(false);
            if (null == session) {
                return null;
            }
        } else {
            session = super.getSession(true);
        }
        try {
            session.getAttribute("test"); // test if the session was invalidated
        } catch (IllegalStateException e) {
            super.setHttpSession(null);
            session = super.getSession(true);
        }
        if (!(session instanceof CuiMockHttpSession)) {
            session = new CuiMockHttpSession(getServletContext());
            super.setHttpSession(session);
            var container = getWebContainer();
            if (container != null) {
                var se = new HttpSessionEvent(session);
                container.sessionCreated(se);
            }
        }
        return session;
    }

}
