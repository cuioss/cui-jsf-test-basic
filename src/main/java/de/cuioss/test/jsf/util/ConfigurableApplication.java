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

import de.cuioss.test.jsf.mocks.CuiMockSearchExpressionHandler;
import de.cuioss.test.valueobjects.util.IdentityResourceBundle;
import jakarta.faces.FactoryFinder;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.application.ApplicationWrapper;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.search.SearchExpressionHandler;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockFacesContext;

import java.util.ResourceBundle;

import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;

/**
 * An {@link ApplicationWrapper} that is capable to do more programmatic
 * configuration compared to the ones provided by myfaces-test
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ConfigurableApplication extends ApplicationWrapper {

    private static final String COMPONENT_CONTAINER_DEFAULT_RENDERER = "javax.faces.Text";

    private static final String COMPONENT_RESOURCE_CONTAINER_COMPONENT = "javax.faces.ComponentResourceContainer";

    @Getter
    private final Application wrapped;

    @Getter
    @Setter
    private boolean useIdentityResourceBundle = true;

    @Getter
    @Setter
    private SearchExpressionHandler searchExpressionHandler = new CuiMockSearchExpressionHandler();

    /**
     * Creates a new {@link ConfigurableApplication} by loading the existing
     * {@link Application} from the {@link ApplicationFactory} and registers itself
     * again to the {@link ApplicationFactory} and {@link MockFacesContext}
     *
     * @param facesContext to be used for adding the created
     *                     {@link ConfigurableApplication} to. Must not be null
     * @return the created {@link ConfigurableApplication}
     */
    public static ConfigurableApplication createWrapAndRegister(final MockFacesContext facesContext) {
        requireNonNull(facesContext);
        final var factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        final var old = factory.getApplication();
        final var application = new ConfigurableApplication(old);
        factory.setApplication(application);
        facesContext.setApplication(application);
        return application;
    }

    @Override
    public ResourceBundle getResourceBundle(final FacesContext ctx, final String name) {
        if (useIdentityResourceBundle && null != ResourceBundleVarNames.getVarName(name)) {
            return new IdentityResourceBundle();
        }
        return wrapped.getResourceBundle(ctx, name);
    }

    @Override
    public String getMessageBundle() {
        if (useIdentityResourceBundle) {
            return IdentityResourceBundle.class.getName();
        }
        return wrapped.getMessageBundle();
    }

    /**
     * Intercept invalid argument for MyFaces Api passing null as rendererType ->
     * UiViewRoot#getComponentResources
     */
    @Override
    public UIComponent createComponent(final FacesContext context, final String componentType,
                                       final String rendererType) {
        //
        if (COMPONENT_RESOURCE_CONTAINER_COMPONENT.equals(componentType) && isEmpty(rendererType)) {
            return wrapped.createComponent(context, componentType, COMPONENT_CONTAINER_DEFAULT_RENDERER);
        }
        return wrapped.createComponent(context, componentType, rendererType);
    }

}
