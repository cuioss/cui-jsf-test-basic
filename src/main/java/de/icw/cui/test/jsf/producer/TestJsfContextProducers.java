package de.icw.cui.test.jsf.producer;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.annotation.ApplicationMap;
import javax.faces.annotation.HeaderMap;
import javax.faces.annotation.HeaderValuesMap;
import javax.faces.annotation.InitParameterMap;
import javax.faces.annotation.RequestCookieMap;
import javax.faces.annotation.RequestParameterMap;
import javax.faces.annotation.SessionMap;
import javax.faces.annotation.ViewMap;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Various JSF Producers for junit tests.
 * Reason: In the test context we don't have a JSF implementation, which would provide equivalent
 * producers.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class TestJsfContextProducers {

    @Produces
    @RequestScoped
    @Named
    FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Produces
    @RequestScoped
    @Named
    ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    @Produces
    @RequestScoped
    @Named
    Object getRequest() {
        return getExternalContext().getRequest();
    }

    @Produces
    @RequestScoped
    @SessionMap
    @Named("sessionScope")
    Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    @Produces
    @RequestScoped
    @RequestParameterMap
    @Named("param")
    Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    @Produces
    @RequestScoped
    @Named("view")
    UIViewRoot getViewRoot() {
        return getFacesContext().getViewRoot();
    }

    @Produces
    @RequestScoped
    @ViewMap
    @Named("viewScope")
    Map<String, Object> getViewMap() {
        return getFacesContext().getViewRoot().getViewMap();
    }

    @Produces
    @RequestScoped
    @HeaderMap
    @Named("header")
    Map<String, String> getHeaderMap() {
        return getExternalContext().getRequestHeaderMap();
    }

    @Produces
    @RequestScoped
    @HeaderValuesMap
    @Named("headerValues")
    Map<String, String[]> getHeaderValuesMap() {
        return getExternalContext().getRequestHeaderValuesMap();
    }

    @Produces
    @RequestScoped
    @InitParameterMap
    @Named("initParam")
    Map<String, String> getInitParameter() {
        return getExternalContext().getInitParameterMap();
    }

    @Produces
    @ApplicationScoped
    @ApplicationMap
    @Named("applicationScope")
    Map<String, Object> getApplicationMap() {
        return getExternalContext().getApplicationMap();
    }

    @Produces
    @RequestScoped
    @RequestCookieMap
    @Named("cookie")
    Map<String, Object> getRequestCookieMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
    }
}
