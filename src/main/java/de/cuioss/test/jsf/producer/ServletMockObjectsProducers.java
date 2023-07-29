package de.cuioss.test.jsf.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockHttpServletResponse;

import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import de.cuioss.test.jsf.mocks.CuiMockServletContext;
import lombok.Getter;
import lombok.Setter;

/**
 * Produces a
 * <ul>
 * <li>{@link RequestScoped} {@link HttpServletResponse}</li>
 * <li>{@link RequestScoped} {@link HttpServletRequest}.</li>
 * <li>{@link Dependent} {@link ServletContext}</li>
 * </ul>
 *
 * In contrast to {@link ServletObjectsFromJSFContextProducers} the mocks are
 * instantiated directly
 *
 * It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 */
@ApplicationScoped
public class ServletMockObjectsProducers {

    @Getter
    @Setter
    private CuiMockHttpServletRequest servletRequest = new CuiMockHttpServletRequest();

    @Getter
    @Setter
    private MockHttpServletResponse servletResponse = new MockHttpServletResponse();

    @Getter
    @Setter
    private CuiMockServletContext servletContext = new CuiMockServletContext();

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    HttpServletRequest produceServletRequest() {
        return servletRequest;
    }

    @Produces
    @Typed({ HttpServletResponse.class })
    @RequestScoped
    HttpServletResponse produceServletResponse() {
        return servletResponse;
    }

    @Produces
    @Typed({ ServletContext.class })
    @Dependent
    ServletContext produceServletContext() {
        return servletContext;
    }

}
