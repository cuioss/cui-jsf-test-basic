package de.cuioss.test.jsf.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Produces a {@link RequestScoped} {@link HttpServletResponse}.
 * Produces a {@link RequestScoped} {@link HttpServletRequest}.
 * This class has no bean annotation. It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 * 
 * @see TestHttpServletRequestProducer
 * @see TestHttpServletResponseProducer
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
