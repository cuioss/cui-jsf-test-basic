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
package de.cuioss.test.jsf.util;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import jakarta.faces.application.Application;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;

/**
 * Holder for {@link JsfRuntimeSetup} that provides shorthands for accessing
 * contained JSF-Objects like {@link FacesContext}, {@link ExternalContext},
 * {@link Application}, ... and implicit accessor for objects like
 * {@link ComponentConfigDecorator},{@link ApplicationConfigDecorator} and {@link RequestConfigDecorator}
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class JsfEnvironmentHolder {

    @NonNull
    private final JsfRuntimeSetup runtimeSetup;

    /**
     * @return an {@link ComponentConfigDecorator} for the contained
     * {@link JsfRuntimeSetup}
     */
    public ComponentConfigDecorator getComponentConfigDecorator() {
        return new ComponentConfigDecorator(getApplication(), getFacesContext());
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained
     * {@link JsfRuntimeSetup}
     */
    public ApplicationConfigDecorator getApplicationConfigDecorator() {
        return new ApplicationConfigDecorator(getApplication(), getFacesContext());
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained
     * {@link JsfRuntimeSetup}
     */
    public RequestConfigDecorator getRequestConfigDecorator() {
        return new RequestConfigDecorator((MockFacesContext) getFacesContext(),
            (MockExternalContext) getExternalContext());
    }

    /**
     * @return an {@link FacesContext} for the contained {@link JsfRuntimeSetup}
     */
    public FacesContext getFacesContext() {
        return runtimeSetup.getFacesContext();
    }

    /**
     * @return an {@link Application} for the contained {@link JsfRuntimeSetup}
     */
    public Application getApplication() {
        return runtimeSetup.getApplication();
    }

    /**
     * @return an {@link ExternalContext} for the contained {@link JsfRuntimeSetup}
     */
    public ExternalContext getExternalContext() {
        return runtimeSetup.getExternalContext();
    }

    /**
     * @return an {@link MockHttpServletResponse} for the contained
     * {@link JsfRuntimeSetup}
     */
    public MockHttpServletResponse getResponse() {
        return runtimeSetup.getResponse();
    }

    /**
     * @return an {@link MockHttpServletRequest} for the contained
     * {@link JsfRuntimeSetup}
     */
    public MockHttpServletRequest getRequest() {
        return runtimeSetup.getRequest();
    }

}
