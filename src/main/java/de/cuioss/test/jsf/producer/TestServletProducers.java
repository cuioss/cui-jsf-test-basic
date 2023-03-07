package de.cuioss.test.jsf.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class has no bean annotation. This type is designed as 'opt-in' and will not needed anymore
 * when we have a real CDI integration in place.
 * 
 * @author Sven Haag
 */
public class TestServletProducers {

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    @Produces
    @Typed({ HttpServletResponse.class })
    @RequestScoped
    HttpServletResponse getServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
}
