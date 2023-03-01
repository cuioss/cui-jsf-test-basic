package de.icw.cui.test.jsf.util;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.test.mock.MockExternalContext22;
import org.apache.myfaces.test.mock.MockFacesContext22;
import org.apache.myfaces.test.mock.MockHttpServletResponse;

import de.icw.cui.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.BeanConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.ComponentConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.RequestConfigDecorator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Holder for for {@link JsfRuntimeSetup} that provides shorthands for accessing contained
 * JSF-Objects like {@link FacesContext}, {@link ExternalContext}, {@link Application}, ... and
 * implicit accessor for objects like {@link BeanConfigDecorator}, {@link ComponentConfigDecorator},
 * {@link ApplicationConfigDecorator} and {@link RequestConfigDecorator}
 *
 * @author Oliver Wolff
 *
 */
@RequiredArgsConstructor
public class JsfEnvironmentHolder {

    @NonNull
    private final JsfRuntimeSetup runtimeSetup;

    /**
     * @return an {@link ComponentConfigDecorator} for the contained {@link JsfRuntimeSetup}
     */
    public ComponentConfigDecorator getComponentConfigDecorator() {
        return new ComponentConfigDecorator(getApplication(), getFacesContext());
    }

    /**
     * @return an {@link BeanConfigDecorator} for the contained {@link JsfRuntimeSetup}
     */
    public BeanConfigDecorator getBeanConfigDecorator() {
        return new BeanConfigDecorator(getFacesContext());
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained {@link JsfRuntimeSetup}
     */
    public ApplicationConfigDecorator getApplicationConfigDecorator() {
        return new ApplicationConfigDecorator(getApplication(), getFacesContext());
    }

    /**
     * @return an {@link ApplicationConfigDecorator} for the contained {@link JsfRuntimeSetup}
     */
    public RequestConfigDecorator getRequestConfigDecorator() {
        return new RequestConfigDecorator((MockFacesContext22) getFacesContext(),
                (MockExternalContext22) getExternalContext());
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
     * @return an {@link MockHttpServletResponse} for the contained {@link JsfRuntimeSetup}
     */
    public MockHttpServletResponse getResponse() {
        return runtimeSetup.getResponse();
    }

}
