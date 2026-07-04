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

    @SuppressWarnings("rawtypes")
    // API defined
    @Override
    public Enumeration getLocales() {
        var locales = requestLocales;
        if (null == locales || locales.isEmpty()) {
            // Servlet spec: at least the server default locale must be returned.
            locales = mutableList(Locale.getDefault());
        }
        return Collections.enumeration(locales);
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
        return List.of();
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
     * <p>
     * Contract notes:
     * <ul>
     * <li>An existing but invalidated session is dropped; with {@code create == false}
     * this method then returns {@code null} instead of silently creating a fresh one.</li>
     * <li>A foreign (non-{@link CuiMockHttpSession}) session is preserved in the
     * read-only ({@code create == false}) path so its attributes are not lost.</li>
     * </ul>
     */
    @Override
    public HttpSession getSession(final boolean create) {
        var session = super.getSession(false);

        // An existing session may have been invalidated; detect and drop it.
        if (null != session && isInvalidated(session)) {
            super.setHttpSession(null);
            session = null;
        }

        if (null != session) {
            // Preserve the existing (possibly foreign) session so its attributes are
            // never discarded, regardless of the create flag.
            return session;
        }

        if (!create) {
            return null;
        }
        return createCuiSession();
    }

    private static boolean isInvalidated(final HttpSession session) {
        try {
            session.getAttribute("test");
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }

    private HttpSession createCuiSession() {
        HttpSession session = new CuiMockHttpSession(getServletContext());
        super.setHttpSession(session);
        var container = getWebContainer();
        if (container != null) {
            container.sessionCreated(new HttpSessionEvent(session));
        }
        return session;
    }

}
