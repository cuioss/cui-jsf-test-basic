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
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.stream.Collectors;

import static de.cuioss.test.jsf.util.ConfigurationHelper.*;

/**
 * Starts and Configures the {@link JsfRuntimeSetup}, wraps it into an
 * {@link JsfEnvironmentHolder} and inject it into an
 * {@link JsfEnvironmentConsumer} if available
 *
 * @author Oliver Wolff
 */
public class JsfSetupExtension implements TestInstancePostProcessor, AfterEachCallback {

    /**
     * Identifies the {@link Namespace} under which the concrete instance of
     * {@link JsfRuntimeSetup} is stored.
     */
    public static final Namespace NAMESPACE = Namespace.create("test", "jsf", "JsfRuntimeSetup");
    private static final CuiLogger LOGGER = new CuiLogger(JsfSetupExtension.class);

    private static void put(JsfRuntimeSetup runtimeSetup, ExtensionContext context) {
        context.getStore(NAMESPACE).put(JsfRuntimeSetup.class.getName(), runtimeSetup);
    }

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
}
