package de.cuioss.test.jsf.junit5;

import static de.cuioss.test.jsf.util.ConfigurationHelper.configureApplication;
import static de.cuioss.test.jsf.util.ConfigurationHelper.configureComponents;
import static de.cuioss.test.jsf.util.ConfigurationHelper.configureManagedBeans;
import static de.cuioss.test.jsf.util.ConfigurationHelper.configureRequestConfig;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.myfaces.test.mock.MockFacesContext;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.commons.support.AnnotationSupport;

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.jsf.util.JsfRuntimeSetup;

/**
 * Starts and Configures the {@link JsfRuntimeSetup}, wraps it into an
 * {@link JsfEnvironmentHolder} and inject it into an
 * {@link JsfEnvironmentConsumer} if available
 *
 * @author Oliver Wolff
 *
 */
public class JsfSetupExtension implements TestInstancePostProcessor, AfterEachCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsfSetupExtension.class);

    /**
     * Identifies the {@link Namespace} under which the concrete instance of
     * {@link JsfRuntimeSetup} is stored .
     */
    public static final Namespace NAMESPACE = Namespace.create("test", "jsf", "JsfRuntimeSetup");

    @Override
    @SuppressWarnings("squid:S3655")
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var setup = new JsfRuntimeSetup();
        LOGGER.info(() -> "Starting JSF-Environment");
        setup.setUp();
        put(setup, context);

        var useIdentityResourceBundle = false;
        List<EnableJsfEnvironment> environments = new ArrayList<>();
        retrieveEnableJSFAnnotations(testInstance.getClass(), environments);
        if (!environments.isEmpty()) {
            // Use the outermost annotation
            useIdentityResourceBundle = environments.get(0).useIdentityResourceBundle();
        }

        var environment = new JsfEnvironmentHolder(setup);
        ConfigurableApplication.createWrapAndRegister((MockFacesContext) environment.getFacesContext())
                .setUseIdentityResouceBundle(useIdentityResourceBundle);

        // Ensure that the ConfigurableApplication is set for
        // JsfRuntimeSetup#getApplication
        setup.setApplication(environment.getFacesContext().getApplication());

        LOGGER.debug(() -> "Registering Decorators");
        Set<JsfTestConfiguration> decoratorAnnotations = new LinkedHashSet<>(16);

        retrieveDecoratorAnnotations(testInstance.getClass(), decoratorAnnotations);
        decoratorAnnotations = decoratorAnnotations.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        configureApplication(testInstance, environment.getApplicationConfigDecorator(), decoratorAnnotations);
        configureComponents(testInstance, environment.getComponentConfigDecorator(), decoratorAnnotations);
        configureManagedBeans(testInstance, environment.getBeanConfigDecorator(), decoratorAnnotations);
        configureRequestConfig(testInstance, environment.getRequestConfigDecorator(), decoratorAnnotations);

        if (testInstance instanceof JsfEnvironmentConsumer) {
            ((JsfEnvironmentConsumer) testInstance).setEnvironmentHolder(environment);
        }
    }

    private void retrieveDecoratorAnnotations(Class<?> type, Set<JsfTestConfiguration> result) {
        if (null == type || Object.class.equals(type)) {
            return;
        }
        result.addAll(AnnotationSupport.findRepeatableAnnotations(type, JsfTestConfiguration.class));
        retrieveDecoratorAnnotations(type.getSuperclass(), result);
    }

    private void retrieveEnableJSFAnnotations(Class<?> type, List<EnableJsfEnvironment> result) {
        if (null == type || Object.class.equals(type)) {
            return;
        }
        result.addAll(AnnotationSupport.findRepeatableAnnotations(type, EnableJsfEnvironment.class));
        retrieveEnableJSFAnnotations(type.getSuperclass(), result);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        LOGGER.debug(() -> "Tear-Down JSF-Environment");
        get(context).ifPresent(JsfRuntimeSetup::tearDown);
    }

    private static void put(JsfRuntimeSetup runtimeSetup, ExtensionContext context) {
        context.getStore(NAMESPACE).put(JsfRuntimeSetup.class.getName(), runtimeSetup);
    }

    private Optional<JsfRuntimeSetup> get(ExtensionContext context) {
        return Optional.ofNullable((JsfRuntimeSetup) context.getStore(NAMESPACE).get(JsfRuntimeSetup.class.getName()));
    }
}
