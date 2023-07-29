package de.cuioss.test.jsf.config.decorator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.test.mock.MockExternalContext22;
import org.apache.myfaces.test.mock.MockFacesContext22;
import org.apache.myfaces.test.mock.MockHttpServletRequest;

import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Helper class for configuring the request
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class RequestConfigDecorator {

    private final MockFacesContext22 facesContext;
    private final MockExternalContext22 externalContext;

    /**
     * Sets the postback attribute to {@link FacesContext#isPostback()}
     *
     * @param postback to be set
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setPostback(final boolean postback) {
        facesContext.setPostback(postback);
        return this;
    }

    /**
     * Sets the viewId in {@link UIViewRoot}
     *
     * @param viewId to be set
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setViewId(final String viewId) {
        facesContext.getViewRoot().setViewId(viewId);
        return this;
    }

    /**
     * Registers a concrete RequestHeader to
     * {@link ExternalContext#getRequestHeaderMap()}
     *
     * @param key   used as the key for the
     *              {@link ExternalContext#getRequestHeaderMap()}
     * @param value used as the value for the
     *              {@link ExternalContext#getRequestHeaderMap()}
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setRequestHeader(final String key, final String value) {
        externalContext.addRequestHeader(key, value);
        return this;
    }

    /**
     * Registers a concrete Request-parameter to
     * {@link ExternalContext#getRequestParameterMap()}
     *
     * @param key   used as the key for the
     *              {@link ExternalContext#getRequestHeaderMap()}
     * @param value used as the value for the
     *              {@link ExternalContext#getRequestHeaderMap()}
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setRequestParameter(final String key, final String value) {
        externalContext.addRequestParameterMap(key, value);
        return this;
    }

    /**
     * Registers a concrete Request-attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)}
     *
     * @param key   used as the key for the
     *              {@link HttpServletRequest#setAttribute(String, Object)}
     * @param value used as the value for the
     *              {@link HttpServletRequest#setAttribute(String, Object)}
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setRequestAttribute(final String key, final Serializable value) {
        ((HttpServletRequest) externalContext.getRequest()).setAttribute(key, value);
        return this;
    }

    /**
     * Registers one or more cookies the the contained request
     *
     * @param cookie to be added
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator addRequestCookie(final Cookie... cookie) {
        var request = (MockHttpServletRequest) externalContext.getRequest();
        for (Cookie aCookie : cookie) {
            request.addCookie(aCookie);
        }
        return this;
    }

    /**
     * <p>
     * Registers one or more requestLocale to
     * {@link ExternalContext#getRequestLocales()}. It can be used for resetting the
     * locales as well.
     * </p>
     * <em>Caution: </em> It expects the {@link HttpServletRequest} being an
     * instance of {@link CuiMockHttpServletRequest}
     *
     * @param requestLocale one or more requestLocales to be set
     * @return the {@link RequestConfigDecorator} itself in order to enable a
     *         fluent-api style usage
     */
    public RequestConfigDecorator setRequestLocale(final Locale... requestLocale) {
        var request = (CuiMockHttpServletRequest) externalContext.getRequest();
        List<Locale> localeList = Arrays.asList(requestLocale);
        Locale locale = null;
        if (!localeList.isEmpty()) {
            locale = localeList.iterator().next();
        }
        request.setLocale(locale);
        request.setRequestLocales(localeList);
        return this;
    }

    /**
     * Explicitly sets a given queryString as path-element of the request
     *
     * @param parameterString
     */
    public void setQueryString(String parameterString) {
        var request = (CuiMockHttpServletRequest) externalContext.getRequest();
        request.setPathElements(request.getContextPath(), request.getServletPath(), request.getPathInfo(),
                parameterString);
    }
}
