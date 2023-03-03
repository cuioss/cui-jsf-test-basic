package de.cuioss.test.jsf.mocks;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletContext;

import org.apache.myfaces.test.mock.MockHttpSession;

import lombok.Getter;
import lombok.Setter;

/**
 * Extension to {@link MockHttpSession} that provides the programmatic setting of
 * 'maxInactiveInterval'
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockHttpSession extends MockHttpSession {

    @Getter
    @Setter
    private int maxInactiveInterval;

    /**
     * Constructor.
     *
     * @param servletContext must not be null
     */
    public CuiMockHttpSession(final ServletContext servletContext) {
        super(servletContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void invalidate() {
        Collection<String> names = Collections.list(super.getAttributeNames());
        names.forEach(super::removeAttribute);
    }

}
