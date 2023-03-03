package de.cuioss.test.jsf.config.decorator;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ProjectStage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockApplication20;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockServletContext;

import de.cuioss.test.jsf.mocks.CuiMockConfigurableNavigationHandler;
import lombok.RequiredArgsConstructor;

/**
 * Helper class acting as runtime-registry for {@link ResourceBundle}, {@link NavigationHandler},
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ApplicationConfigDecorator {

    private final Application application;
    private final FacesContext facesContext;

    /**
     * Registers a {@link ResourceBundle} to a given name
     *
     * @param bundleName the name of the bundle to be registered to
     * @param bundlePath the path to the {@link ResourceBundle}
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator registerResourceBundle(final String bundleName,
            final String bundlePath) {
        ResourceBundleVarNames.addVarName(bundleName, bundlePath);
        return this;
    }

    /**
     * Registers the supported {@link Locale}s, see
     * {@link Application#setSupportedLocales(Collection)}
     *
     * @param locales to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator registerSupportedLocales(final Collection<Locale> locales) {
        application.setSupportedLocales(locales);
        return this;
    }

    /**
     * Registers the default {@link Locale}, see {@link Application#setDefaultLocale(Locale)}
     *
     * @param locale to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator registerDefaultLocale(final Locale locale) {
        application.setDefaultLocale(locale);
        return this;
    }

    /**
     * Register a navigation case in a simple way, with two parameter only
     *
     * @param outcome to be registered
     * @param toViewId to be registered
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator registerNavigationCase(final String outcome,
            final String toViewId) {
        getMockNavigationHandler().addNavigationCase(outcome,
                new NavigationCase(null, null, outcome, null, toViewId, null, null, true, true));
        return this;
    }

    /**
     * @return an instance of {@link CuiMockConfigurableNavigationHandler} If not already configured
     *         this method will implicitly register a new instance
     */
    public CuiMockConfigurableNavigationHandler getMockNavigationHandler() {
        if (!(application.getNavigationHandler() instanceof CuiMockConfigurableNavigationHandler)) {
            application.setNavigationHandler(new CuiMockConfigurableNavigationHandler());
        }
        return (CuiMockConfigurableNavigationHandler) application.getNavigationHandler();
    }

    /**
     * Sets the {@link ProjectStage}. Caution: this method uses hardcore reflection to access the
     * field with the name "_projectStage", may therefore be fragile.
     *
     * @param projectStage to be set
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator setProjectStage(final ProjectStage projectStage) {
        try {
            var projectStageField = MockApplication20.class.getDeclaredField("_projectStage");
            projectStageField.setAccessible(true);
            projectStageField.set(getMockApplicationInstance(application), projectStage);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            throw new IllegalStateException(
                    "Unable to set projectStage, due to underlying Exception", e);
        }
        return this;
    }

    private static Object getMockApplicationInstance(final Application application) {
        if (application instanceof MockApplication20) {
            return application;
        }
        if (application instanceof ApplicationWrapper) {
            return getMockApplicationInstance(((ApplicationWrapper) application).getWrapped());
        }
        return application;
    }

    /**
     * Sets the contextPath in {@link HttpServletRequest}
     *
     * @param contextPath to be set
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator setContextPath(final String contextPath) {
        ((MockHttpServletRequest) facesContext.getExternalContext().getRequest())
                .setContextPath(contextPath);
        return this;
    }

    /**
     * Registers a concrete InitParameter to {@link ExternalContext#getInitParameterMap()}
     *
     * @param key used as the key for the {@link ExternalContext#getInitParameterMap()}
     * @param value used as the value for the {@link ExternalContext#getInitParameterMap()}
     * @return the {@link ApplicationConfigDecorator} itself in order to enable a fluent-api style
     *         usage
     */
    public ApplicationConfigDecorator addInitParameter(final String key, final String value) {
        var context = (MockServletContext) facesContext.getExternalContext().getContext();
        context.addInitParameter(key, value);
        return this;
    }

}
