/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
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
public class JsfSetupExtension implements TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback, ParameterResolver {

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

    /**
     * Identifies the {@link Namespace} under which the test instance is stored.
     */
    public static final Namespace TEST_INSTANCE_NAMESPACE = Namespace.create("test", "jsf", "TestInstance");

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

    private static void putTestInstance(Object testInstance, ExtensionContext context) {
        context.getStore(TEST_INSTANCE_NAMESPACE).put(Object.class.getName(), testInstance);
    }

    private static Optional<Object> getTestInstance(ExtensionContext context) {
        return Optional.ofNullable(context.getStore(TEST_INSTANCE_NAMESPACE).get(Object.class.getName()));
    }

    @Override
    @SuppressWarnings("squid:S3655")
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        // Store the test instance for later use in beforeEach
        putTestInstance(testInstance, context);

        var setup = new JsfRuntimeSetup();
        LOGGER.debug(() -> "Starting JSF-Environment");
        setup.setUp();
        put(setup, context);

        var useIdentityResourceBundle = false;
        List<EnableJsfEnvironment> environments = new ArrayList<>();
        retrieveEnableJSFAnnotations(testInstance.getClass(), environments);
        if (!environments.isEmpty()) {
            // Use the outermost annotation
            useIdentityResourceBundle = environments.getFirst().useIdentityResourceBundle();
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
        Set<JsfTestConfiguration> decoratorAnnotations = LinkedHashSet.newLinkedHashSet(16);

        retrieveDecoratorAnnotations(testInstance.getClass(), decoratorAnnotations);
        decoratorAnnotations = decoratorAnnotations.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        configureApplication(testInstance, environment.getApplicationConfigDecorator(), decoratorAnnotations);
        configureComponents(testInstance, environment.getComponentConfigDecorator(), decoratorAnnotations);
        configureRequestConfig(testInstance, environment.getRequestConfigDecorator(), decoratorAnnotations);

        if (testInstance instanceof JsfEnvironmentConsumer consumer) {
            consumer.setEnvironmentHolder(environment);
        }
    }

    /**
     * Retrieves all JsfTestConfiguration annotations from the class hierarchy, including nested classes.
     * The annotations are added to the result set in order of priority (method > nested class > parent class).
     *
     * @param type   the class to check for annotations
     * @param result the set to add the annotations to
     * @param method the method to check for annotations, or null if not checking a method
     */
    private void retrieveDecoratorAnnotations(Class<?> type, Set<JsfTestConfiguration> result, java.lang.reflect.Method method) {
        if (null == type || Object.class.equals(type)) {
            return;
        }

        // Check method-level annotations first (highest priority)
        if (method != null) {
            Set<JsfTestConfiguration> methodAnnotations = new LinkedHashSet<>(
                AnnotationSupport.findRepeatableAnnotations(method, JsfTestConfiguration.class));

            // If method-level annotations are found, they take precedence over all others
            if (!methodAnnotations.isEmpty()) {
                result.clear();
                result.addAll(methodAnnotations);
                return;
            }
        }

        // Check class-level annotations
        Set<JsfTestConfiguration> classAnnotations = new LinkedHashSet<>(
            AnnotationSupport.findRepeatableAnnotations(type, JsfTestConfiguration.class));

        // If class-level annotations are found, they take precedence over parent/enclosing classes
        if (!classAnnotations.isEmpty()) {
            result.clear();
            result.addAll(classAnnotations);
            return;
        }

        // If no annotations found at this level, check enclosing classes or superclass
        Class<?> enclosingClass = type.getEnclosingClass();
        if (enclosingClass != null) {
            retrieveDecoratorAnnotations(enclosingClass, result, null);
        } else if (type.getSuperclass() != null) {
            retrieveDecoratorAnnotations(type.getSuperclass(), result, null);
        }
    }

    /**
     * Backward compatibility method that calls retrieveDecoratorAnnotations without a method parameter.
     *
     * @param type   the class to check for annotations
     * @param result the set to add the annotations to
     */
    private void retrieveDecoratorAnnotations(Class<?> type, Set<JsfTestConfiguration> result) {
        retrieveDecoratorAnnotations(type, result, null);
    }

    /**
     * Retrieves all EnableJsfEnvironment annotations from the class hierarchy, including nested classes.
     * The annotations are added to the result list in order of priority (method > nested class > parent class).
     *
     * @param type   the class to check for annotations
     * @param result the list to add the annotations to
     * @param method the method to check for annotations, or null if not checking a method
     */
    private void retrieveEnableJSFAnnotations(Class<?> type, List<EnableJsfEnvironment> result, java.lang.reflect.Method method) {
        if (null == type || Object.class.equals(type)) {
            return;
        }

        // Create a new list to hold all annotations
        List<EnableJsfEnvironment> allAnnotations = new ArrayList<>();

        // Check method-level annotations first (highest priority)
        if (method != null) {
            allAnnotations.addAll(AnnotationSupport.findRepeatableAnnotations(method, EnableJsfEnvironment.class));
        }

        // Then check class-level annotations
        allAnnotations.addAll(AnnotationSupport.findRepeatableAnnotations(type, EnableJsfEnvironment.class));

        // If we found annotations at this level, clear the result set and add these annotations
        if (!allAnnotations.isEmpty()) {
            result.clear();
            result.addAll(allAnnotations);
            return;
        }

        // If no annotations found at this level, check enclosing classes or superclass
        Class<?> enclosingClass = type.getEnclosingClass();
        if (enclosingClass != null) {
            retrieveEnableJSFAnnotations(enclosingClass, result, null);
        } else if (type.getSuperclass() != null) {
            retrieveEnableJSFAnnotations(type.getSuperclass(), result, null);
        }
    }

    /**
     * Backward compatibility method that calls retrieveEnableJSFAnnotations without a method parameter.
     *
     * @param type   the class to check for annotations
     * @param result the list to add the annotations to
     */
    private void retrieveEnableJSFAnnotations(Class<?> type, List<EnableJsfEnvironment> result) {
        retrieveEnableJSFAnnotations(type, result, null);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Optional<Object> testInstanceOpt = getTestInstance(context);
        if (testInstanceOpt.isEmpty()) {
            return;
        }

        Object testInstance = testInstanceOpt.get();
        Optional<JsfEnvironmentHolder> environmentOpt = getEnvironment(context);
        if (environmentOpt.isEmpty()) {
            return;
        }

        JsfEnvironmentHolder environment = environmentOpt.get();

        // Get the current test method
        Optional<java.lang.reflect.Method> testMethod = context.getTestMethod();
        if (testMethod.isEmpty()) {
            return;
        }

        // Get the actual test class (which might be a nested class)
        Class<?> testClass = context.getRequiredTestClass();

        // Check for method-level EnableJsfEnvironment annotations
        List<EnableJsfEnvironment> methodAnnotations = new ArrayList<>();
        retrieveEnableJSFAnnotations(testClass, methodAnnotations, testMethod.get());

        // Check for method-level JsfTestConfiguration annotations
        Set<JsfTestConfiguration> decoratorAnnotations = LinkedHashSet.newLinkedHashSet(16);
        retrieveDecoratorAnnotations(testClass, decoratorAnnotations, testMethod.get());
        decoratorAnnotations = decoratorAnnotations.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        // Apply method-level EnableJsfEnvironment annotation if present
        if (!methodAnnotations.isEmpty()) {
            // Method-level annotation found, apply it
            EnableJsfEnvironment annotation = methodAnnotations.getFirst();

            // Update the identity resource bundle setting
            ConfigurableApplication.createWrapAndRegister((MockFacesContext) environment.getFacesContext())
                .setUseIdentityResourceBundle(annotation.useIdentityResourceBundle());
        }

        // Apply method-level JsfTestConfiguration annotations if present
        if (!decoratorAnnotations.isEmpty()) {
            // Apply method-level configurations
            configureApplication(testInstance, environment.getApplicationConfigDecorator(), decoratorAnnotations);
            configureComponents(testInstance, environment.getComponentConfigDecorator(), decoratorAnnotations);
            configureRequestConfig(testInstance, environment.getRequestConfigDecorator(), decoratorAnnotations);
        }
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
