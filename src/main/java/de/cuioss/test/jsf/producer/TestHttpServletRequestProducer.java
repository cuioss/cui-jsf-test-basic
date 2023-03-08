package de.cuioss.test.jsf.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Produces a {@link RequestScoped} {@link HttpServletRequest}.
 * This class has no bean annotation. It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 */
public class TestHttpServletRequestProducer {

    @Produces
    @Typed({HttpServletRequest.class})
    @RequestScoped
    HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
