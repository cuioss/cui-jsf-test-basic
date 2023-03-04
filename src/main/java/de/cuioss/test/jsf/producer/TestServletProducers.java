package de.cuioss.test.jsf.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;

/**
 * This class has no bean annotation. Use it as opt-in using AddBeanClasses or create your own
 * producer with EasyMock.
 *
 * @author Sven Haag
 */
public class TestServletProducers {

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    HttpServletRequest getServletRequest() {
        return (MockHttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    @Produces
    @Typed({ HttpServletResponse.class })
    @RequestScoped
    HttpServletResponse getServletResponse() {
        return (MockHttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
}
