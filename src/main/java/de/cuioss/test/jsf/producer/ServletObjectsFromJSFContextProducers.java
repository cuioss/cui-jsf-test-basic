package de.cuioss.test.jsf.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockExternalContext22;

/**
 * Produces a
 * <ul>
 * <li>{@link RequestScoped} {@link HttpServletResponse}</li>
 * <li>{@link RequestScoped} {@link HttpServletRequest}.</li>
 * <li>{@link Dependent} {@link ServletContext}</li>
 * </ul>
 * The objects are derived from the {@link FacesContext}
 * 
 * It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 * 
 */
@ApplicationScoped
public class ServletObjectsFromJSFContextProducers {

    @Produces
    @Typed({ HttpServletRequest.class })
    @RequestScoped
    HttpServletRequest produceServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    @Produces
    @Typed({ HttpServletResponse.class })
    @RequestScoped
    HttpServletResponse produceServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    @Produces
//    @Typed({ ServletContext.class })
    @Dependent
    ServletContext produceServletContext() {
        return (ServletContext) ((MockExternalContext22) FacesContext.getCurrentInstance().getExternalContext())
                .getContext();
    }
}
