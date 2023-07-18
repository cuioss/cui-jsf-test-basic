package de.cuioss.test.jsf.producer;

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
 * This class has no bean annotation. It is designed as 'opt-in'. Use with
 * {@code @AddBeanClasses}.
 */
public class ServletMockObjectsProducers {

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    @Getter
    @Setter
    private CuiMockHttpServletRequest servletRequest = new CuiMockHttpServletRequest();

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    @Getter
    @Setter
    private MockHttpServletResponse servletResponse = new MockHttpServletResponse();

    @Produces
    @Typed({ ServletContext.class })
    @Dependent
    @Getter
    @Setter
    private CuiMockServletContext servletContext = new CuiMockServletContext();

}
