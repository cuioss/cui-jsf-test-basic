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
package de.cuioss.test.jsf.mocks;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.Part;
import lombok.Setter;
import org.apache.myfaces.test.mock.MockHttpServletRequest;

import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

/**
 * Extends {@link MockHttpServletRequest} and provides some additional
 * convenience methods
 *
 * @author Oliver Wolff
 */
public class CuiMockHttpServletRequest extends MockHttpServletRequest {

    /**
     * "http://localhost:8080/servletRequestUrl/"
     */
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
