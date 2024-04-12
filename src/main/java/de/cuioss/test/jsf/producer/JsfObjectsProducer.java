/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.annotation.*;
import jakarta.faces.application.Application;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.util.Map;

/**
 * Various JSF Producers for junit tests.
 * <p>
 * Reason: In the test context, we don't have a JSF implementation, which would
 * provide equivalent producers.
 * <p>
 * It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class JsfObjectsProducer {

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
    @ApplicationScoped
    Application produceApplication() {
        return FacesContext.getCurrentInstance().getApplication();
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
