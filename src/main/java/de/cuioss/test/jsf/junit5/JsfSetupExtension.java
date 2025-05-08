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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.jsf.util.JsfRuntimeSetup;
import de.cuioss.tools.logging.CuiLogger;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.stream.Collectors;

import static de.cuioss.test.jsf.util.ConfigurationHelper.*;

/**
 * Starts and Configures the {@link JsfRuntimeSetup}, wraps it into an
 * {@link JsfEnvironmentHolder} and inject it into an
 * {@link JsfEnvironmentConsumer} if available.
 * <p>
 * This extension also implements {@link ParameterResolver} to support parameter
 * resolution for JSF-related objects in test methods. This allows test methods to
 * directly declare the JSF objects they need as parameters, without requiring the
 * test class to implement any specific interface.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @EnableJsfEnvironment
 * class MyTest {
 *
 *     @Test
 *     void testWithFacesContext(FacesContext facesContext) {
 *         assertNotNull(facesContext);
 *         // Test code using facesContext
 *     }
 * }
 * }
 * </pre>
 *
 * @author Oliver Wolff
 */
public class JsfSetupExtension implements TestInstancePostProcessor, AfterEachCallback, ParameterResolver {

    /**
     * Identifies the {@link Namespace} under which the concrete instance of
     * {@link JsfRuntimeSetup} is stored.
     */
    public static final Namespace NAMESPACE = Namespace.create("test", "jsf", "JsfRuntimeSetup");
    
    /**
     * Identifies the {@link Namespace} under which the concrete instance of
     * {@link JsfEnvironmentHolder} is stored.
     */
    public static final Namespace ENVIRONMENT_NAMESPACE = Namespace.create("test", "jsf", "JsfEnvironmentHolder");
    
    private static final CuiLogger LOGGER = new CuiLogger(JsfSetupExtension.class);

    private static void put(JsfRuntimeSetup runtimeSetup, ExtensionContext context) {
        context.getStore(NAMESPACE).put(JsfRuntimeSetup.class.getName(), runtimeSetup);
    }
    
    private static void putEnvironment(JsfEnvironmentHolder environment, ExtensionContext context) {
        context.getStore(ENVIRONMENT_NAMESPACE).put(JsfEnvironmentHolder.class.getName(), environment);
    }
    
    private static Optional<JsfEnvironmentHolder> getEnvironment(ExtensionContext context) {
        return Optional.ofNullable((JsfEnvironmentHolder) context.getStore(ENVIRONMENT_NAMESPACE)
            .get(JsfEnvironmentHolder.class.getName()));
    }

    @Override
    @SuppressWarnings("squid:S3655")
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var setup = new JsfRuntimeSetup();
        LOGGER.debug(() -> "Starting JSF-Environment");
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
        // Store the environment in the context for parameter resolution
        putEnvironment(environment, context);
        
        ConfigurableApplication.createWrapAndRegister((MockFacesContext) environment.getFacesContext())
            .setUseIdentityResourceBundle(useIdentityResourceBundle);

        // Ensure that the ConfigurableApplication is set for
        // JsfRuntimeSetup#getApplication
        setup.setApplication(environment.getFacesContext().getApplication());

        LOGGER.debug(() -> "Registering Decorators");
        Set<JsfTestConfiguration> decoratorAnnotations = new LinkedHashSet<>(16);

        retrieveDecoratorAnnotations(testInstance.getClass(), decoratorAnnotations);
        decoratorAnnotations = decoratorAnnotations.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        configureApplication(testInstance, environment.getApplicationConfigDecorator(), decoratorAnnotations);
        configureComponents(testInstance, environment.getComponentConfigDecorator(), decoratorAnnotations);
        configureRequestConfig(testInstance, environment.getRequestConfigDecorator(), decoratorAnnotations);

        if (testInstance instanceof JsfEnvironmentConsumer consumer) {
            consumer.setEnvironmentHolder(environment);
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

    private Optional<JsfRuntimeSetup> get(ExtensionContext context) {
        return Optional.ofNullable((JsfRuntimeSetup) context.getStore(NAMESPACE).get(JsfRuntimeSetup.class.getName()));
    }
    
    /**
     * Determines if this resolver supports the given parameter.
     *
     * @param parameterContext the context for the parameter for which a resolver is being requested;
     *                        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked;
     *                        never {@code null}
     * @return {@code true} if this resolver can resolve the parameter
     * @throws ParameterResolutionException if an error occurs while determining whether the parameter
     *                                     can be resolved
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Optional<JsfEnvironmentHolder> environment = getEnvironment(extensionContext);
        if (environment.isEmpty()) {
            return false;
        }
        
        JsfParameterResolver resolver = new JsfParameterResolver(environment.get());
        return resolver.supportsParameter(parameterContext, extensionContext);
    }

    /**
     * Resolves the given parameter.
     *
     * @param parameterContext the context for the parameter to be resolved;
     *                        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked;
     *                        never {@code null}
     * @return the resolved parameter value; may be {@code null} but only if the parameter type
     *         is not a primitive
     * @throws ParameterResolutionException if an error occurs while resolving the parameter
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Optional<JsfEnvironmentHolder> environment = getEnvironment(extensionContext);
        if (environment.isEmpty()) {
            throw new ParameterResolutionException("JSF environment not initialized");
        }
        
        JsfParameterResolver resolver = new JsfParameterResolver(environment.get());
        return resolver.resolveParameter(parameterContext, extensionContext);
    }
}