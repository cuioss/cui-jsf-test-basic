package de.cuioss.test.jsf.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Produces a {@link RequestScoped} {@link HttpServletResponse}.
 * This class has no bean annotation. It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 */
public class TestHttpServletResponseProducer {

    @Produces
    @Typed({HttpServletResponse.class})
    @RequestScoped
    HttpServletResponse getServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
}
