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
package de.cuioss.test.jsf.config.decorator;

import de.cuioss.test.jsf.mocks.CuiMockConfigurableNavigationHandler;
import de.cuioss.tools.reflect.FieldWrapper;
import jakarta.faces.application.*;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockApplication20;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockServletContext;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper class acting as runtime-registry for {@link ResourceBundle},
 * {@link NavigationHandler},
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ApplicationConfigDecorator {

    private final Application application;
    private final FacesContext facesContext;

    private static Object getMockApplicationInstance(final Application application) {
        if (application instanceof MockApplication20) {
            return application;
        }
        if (application instanceof ApplicationWrapper wrapper) {
            return getMockApplicationInstance(wrapper.getWrapped());
        }
        return application;
    }

    /**
     * Registers a {@link ResourceBundle} to a given name
     *
     * @param bundleName the name of the bundle to be registered to
     * @param bundlePath the path to the {@link ResourceBundle}
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator registerResourceBundle(final String bundleName, final String bundlePath) {
        ResourceBundleVarNames.addVarName(bundleName, bundlePath);
        return this;
    }

    /**
     * Registers the supported {@link Locale}s, see
     * {@link Application#setSupportedLocales(Collection)}
     *
     * @param locales to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator registerSupportedLocales(final Collection<Locale> locales) {
        application.setSupportedLocales(locales);
        return this;
    }

    /**
     * Registers the default {@link Locale}, see
     * {@link Application#setDefaultLocale(Locale)}
     *
     * @param locale to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator registerDefaultLocale(final Locale locale) {
        application.setDefaultLocale(locale);
        return this;
    }

    /**
     * Register a navigation case in a simple way, with two parameter only
     *
     * @param outcome  to be registered
     * @param toViewId to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator registerNavigationCase(final String outcome, final String toViewId) {
        getMockNavigationHandler().addNavigationCase(outcome,
            new NavigationCase(null, null, outcome, null, toViewId, null, null, true, true));
        return this;
    }

    /**
     * @return an instance of {@link CuiMockConfigurableNavigationHandler} If not
     * already configured this method will implicitly register a new
     * instance
     */
    public CuiMockConfigurableNavigationHandler getMockNavigationHandler() {
        if (!(application.getNavigationHandler() instanceof CuiMockConfigurableNavigationHandler)) {
            application.setNavigationHandler(new CuiMockConfigurableNavigationHandler());
        }
        return (CuiMockConfigurableNavigationHandler) application.getNavigationHandler();
    }

    /**
     * Sets the {@link ProjectStage}. Caution: this method uses hardcore reflection
     * to access the field with the name "_projectStage", may therefore be fragile.
     *
     * @param projectStage to be set
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator setProjectStage(final ProjectStage projectStage) {
        var projectStageField = FieldWrapper.from(MockApplication20.class, "_projectStage");
        if (projectStageField.isEmpty()) {
            throw new IllegalStateException("Unable to set projectStage, due to underlying Exception");
        }
        projectStageField.get().writeValue(getMockApplicationInstance(application), projectStage);
        return this;
    }

    /**
     * Sets the contextPath in {@link HttpServletRequest}
     *
     * @param contextPath to be set
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator setContextPath(final String contextPath) {
        ((MockHttpServletRequest) facesContext.getExternalContext().getRequest()).setContextPath(contextPath);
        return this;
    }

    /**
     * Registers a concrete InitParameter to
     * {@link ExternalContext#getInitParameterMap()}
     *
     * @param key   used as the key for the
     *              {@link ExternalContext#getInitParameterMap()}
     * @param value used as the value for the
     *              {@link ExternalContext#getInitParameterMap()}
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ApplicationConfigDecorator addInitParameter(final String key, final String value) {
        var context = (MockServletContext) facesContext.getExternalContext().getContext();
        context.addInitParameter(key, value);
        return this;
    }

}
