package de.icw.cui.test.jsf.util;

import static de.icw.cui.test.jsf.util.ConfigurationHelper.configureApplication;
import static de.icw.cui.test.jsf.util.ConfigurationHelper.configureComponents;
import static de.icw.cui.test.jsf.util.ConfigurationHelper.configureManagedBeans;
import static de.icw.cui.test.jsf.util.ConfigurationHelper.configureRequestConfig;
import static de.icw.cui.test.jsf.util.ConfigurationHelper.extractJsfTestConfiguration;
import static io.cui.tools.string.MoreStrings.emptyToNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.IntegerConverter;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockExternalContext22;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockFacesContext22;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.junit.Before;

import de.icw.cui.test.jsf.config.ApplicationConfigurator;
import de.icw.cui.test.jsf.config.BeanConfigurator;
import de.icw.cui.test.jsf.config.ComponentConfigurator;
import de.icw.cui.test.jsf.config.JsfTestConfiguration;
import de.icw.cui.test.jsf.config.RequestConfigurator;
import de.icw.cui.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.BeanConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.ComponentConfigDecorator;
import de.icw.cui.test.jsf.config.decorator.RequestConfigDecorator;
import io.cui.test.valueobjects.util.IdentityResourceBundle;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for configuring the {@link FacesContext} provided by MyFaces-Test. The configuration
 * is implemented using some kind of decorator pattern (roughly). The actual configuration relies on
 * two sources:
 * <ul>
 * <li>It scans for the annotations of type {@link JsfTestConfiguration} instantiates the
 * corresponding classes and configures the environment accordingly</li>
 * <li>It checks whether the actual test-class implements one of {@link ApplicationConfigurator},
 * {@link BeanConfigurator}, {@link RequestConfigurator} or {@link ComponentConfigurator} and calls
 * the corresponding methods accordingly, <em>after</em> the configurator derived by the
 * annotations</li>
 * </ul>
 * <p>
 * The corresponding objects {@link ComponentConfigurator}, {@link BeanConfigDecorator},
 * {@link RequestConfigDecorator} and {@link ApplicationConfigurator} can be accessed by the
 * getter-methods, in case you need to configure elements for a certain test only.
 * </p>
 * <p>
 * In addition there is a new way of dealing with localized messages for unit-tests. In essence
 * there is the {@link IdentityResourceBundle} configured: This is helpful for tests
 * where you want to ensure that a certain message key is used to create a message but do not want
 * to test the actual ResourceBundle mechanism itself. It will always return the given key itself.
 * As default this mechanism is active, you can change this by overwriting
 * #isUseIdentityResouceBundle(). If it is active it is used as well for resolving
 * the MessageBundle
 * </p>
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S2187") // owolff Base class for actual tests
public class ConfigurableFacesTest {

    /** "Location" */
    public static final String LOCATION_HEADER = "Location";

    @Getter(AccessLevel.PROTECTED)
    private ComponentConfigDecorator componentConfigDecorator;

    @Getter(AccessLevel.PROTECTED)
    private BeanConfigDecorator beanConfigDecorator;

    @Getter(AccessLevel.PROTECTED)
    private ApplicationConfigDecorator applicationConfigDecorator;

    @Getter
    private RequestConfigDecorator requestConfigDecorator;

    private ConfigurableApplication configurableApplication;

    private final JsfRuntimeSetup runtimeSetup = new JsfRuntimeSetup();

    /**
     * See class documentation of {@link ConfigurableFacesTest} for details.
     */
    @Before
    public void setupAdditionalConfiguration() {
        runtimeSetup.setUp();
        configurableApplication = ConfigurableApplication.createWrapAndRegister((MockFacesContext) getFacesContext());
        configurableApplication.setUseIdentityResouceBundle(isUseIdentityResouceBundle());

        componentConfigDecorator = new ComponentConfigDecorator(getApplication(), getFacesContext());
        beanConfigDecorator = new BeanConfigDecorator(getFacesContext());
        applicationConfigDecorator = new ApplicationConfigDecorator(getApplication(), getFacesContext());
        requestConfigDecorator =
            new RequestConfigDecorator((MockFacesContext22) getFacesContext(),
                    (MockExternalContext22) getExternalContext());

        final var annotations = extractJsfTestConfiguration(getClass());
        configureApplication(this, applicationConfigDecorator, annotations);
        configureComponents(this, componentConfigDecorator, annotations);
        configureManagedBeans(this, beanConfigDecorator, annotations);
        configureRequestConfig(this, requestConfigDecorator, annotations);

        // Fix for invalid set converter Id at org.apache.myfaces.test.mock.MockApplication@91
        componentConfigDecorator.registerConverter(IntegerConverter.class,
                IntegerConverter.CONVERTER_ID);
        // Enum converter is currently omitted.
        componentConfigDecorator.registerConverter(EnumConverter.class,
                EnumConverter.CONVERTER_ID);
        componentConfigDecorator.registerConverter(EnumConverter.class, Enum.class);

        // Enable CuiMockConfigurableNavigationHandler to be used
        getApplicationConfigDecorator().getMockNavigationHandler();
    }

    protected FacesContext getFacesContext() {
        return runtimeSetup.getFacesContext();
    }

    protected Application getApplication() {
        return configurableApplication;
    }

    protected ExternalContext getExternalContext() {
        return runtimeSetup.getExternalContext();
    }

    protected MockHttpServletResponse getResponse() {
        return runtimeSetup.getResponse();
    }

    protected boolean isUseIdentityResouceBundle() {
        return true;
    }

    /**
     * Asserts whether a navigation was handled by calling
     * {@link NavigationHandler#handleNavigation(javax.faces.context.FacesContext, String, String)}
     *
     * @param outcome must not be null
     */
    public void assertNavigatedWithOutcome(final String outcome) {
        assertNotNull("Outcome must not be null", emptyToNull(outcome));
        assertTrue("Response is not committed", getFacesContext().getExternalContext().isResponseCommitted());
        var handler = getApplicationConfigDecorator().getMockNavigationHandler();
        assertTrue("handleNavigation is not called", handler.isHandleNavigationCalled());
        assertEquals(outcome, handler.getCalledOutcome());
    }

    /**
     * Asserts whether a navigation was initialized by calling
     * {@link ExternalContext#redirect(String)}
     *
     * @param redirectUrl must not be null
     */
    public void assertRedirect(final String redirectUrl) {
        assertNotNull("redirectUrl must not be null", emptyToNull(redirectUrl));
        assertTrue("Response is not committed", getFacesContext().getExternalContext().isResponseCommitted());
        var tempResponse = (HttpServletResponse) getExternalContext().getResponse();
        assertTrue("Response must provide a header with the name " + LOCATION_HEADER,
                tempResponse.containsHeader(LOCATION_HEADER));
        assertEquals(redirectUrl, tempResponse.getHeader(LOCATION_HEADER));

    }
}
