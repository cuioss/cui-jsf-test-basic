package de.icw.cui.test.jsf.util;

import static io.cui.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;

import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ApplicationWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.context.FacesContext;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockFacesContext;

import de.icw.cui.test.jsf.mocks.CuiMockSearchExpressionHandler;
import io.cui.test.valueobjects.util.IdentityResourceBundle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * An {@link ApplicationWrapper} that is capable to do more programmatic configuration compared to
 * the ones provided by myfaces-test
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ConfigurableApplication extends ApplicationWrapper {

    private static final String COMPONENT_CONTAINER_DEFAULT_RENDERER = "javax.faces.Text";

    private static final String COMPONENT_RESOUCE_CONTAINER_COMPONENT =
            "javax.faces.ComponentResourceContainer";

    @Getter
    private final Application wrapped;

    @Getter
    @Setter
    private boolean useIdentityResouceBundle = true;

    @Getter
    @Setter
    private SearchExpressionHandler searchExpressionHandler = new CuiMockSearchExpressionHandler();

    @Override
    public ResourceBundle getResourceBundle(final FacesContext ctx, final String name) {
        if (useIdentityResouceBundle && null != ResourceBundleVarNames.getVarName(name)) {
            return new IdentityResourceBundle();
        }
        return wrapped.getResourceBundle(ctx, name);
    }

    @Override
    public String getMessageBundle() {
        if (useIdentityResouceBundle) {
            return IdentityResourceBundle.class.getName();
        }
        return wrapped.getMessageBundle();
    }

    /**
     * Creates a new {@link ConfigurableApplication} by loading the existing {@link Application}
     * from the {@link ApplicationFactory} and registers itself again to the
     * {@link ApplicationFactory} and {@link MockFacesContext}
     *
     * @param facesContext to be used for adding the created {@link ConfigurableApplication} to.
     *            Must not be null
     * @return the created {@link ConfigurableApplication}
     */
    public static final ConfigurableApplication createWrapAndRegister(
            final MockFacesContext facesContext) {
        requireNonNull(facesContext);
        final ApplicationFactory factory =
                (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        final Application old = factory.getApplication();
        final ConfigurableApplication application = new ConfigurableApplication(old);
        factory.setApplication(application);
        facesContext.setApplication(application);
        return application;
    }

    /**
     * Intercept invalid argument for MyFaces Api passing null as rendererType ->
     * UiViewRoot#getComponentResources
     */
    @Override
    public UIComponent createComponent(final FacesContext context, final String componentType,
            final String rendererType) {
        //
        if (COMPONENT_RESOUCE_CONTAINER_COMPONENT.equals(componentType)
                && isEmpty(rendererType)) {
            return wrapped.createComponent(context, componentType,
                    COMPONENT_CONTAINER_DEFAULT_RENDERER);
        }
        return wrapped.createComponent(context, componentType, rendererType);
    }

}
