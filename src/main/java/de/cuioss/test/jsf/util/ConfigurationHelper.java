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
package de.cuioss.test.jsf.util;

import de.cuioss.test.jsf.config.*;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Helper class providing some utility methods for handling configuration.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1118")
// owolff: lombok generated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationHelper {

    /**
     * Checks the given type for the annotation {@link JsfTestConfiguration} and and
     * puts all found in the immutable {@link Set} to be returned
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     * @return immutable {@link Set} of found {@link JsfTestConfiguration} elements.
     */
    public static Set<JsfTestConfiguration> extractJsfTestConfiguration(final Class<?> annotated) {
        requireNonNull(annotated);

        final var builder = new CollectionBuilder<JsfTestConfiguration>();

        MoreReflection.extractAllAnnotations(annotated, JsfTestConfiguration.class).forEach(builder::add);

        return builder.toImmutableSet();
    }

    /**
     * Instantiates the given {@link ComponentConfigurator} and calls them with the
     * given {@link ComponentConfigDecorator}. In case the given testClass instance
     * also implements {@link ComponentConfigurator} the corresponding method will
     * be called <em>after</em> the others
     *
     * @param testClass      the actual instance of test, must not be null
     * @param registry       to be passed the the individual instances of
     *                       {@link ComponentConfigurator}, must not be null
     * @param configurations the previously extracted annotations, must not be null
     *                       but may be empty.
     */
    public static void configureComponents(final Object testClass, final ComponentConfigDecorator registry,
        final Collection<JsfTestConfiguration> configurations) {
        requireNonNull(testClass);
        requireNonNull(registry);
        requireNonNull(configurations);
        final List<ComponentConfigurator> instances = getAssignableContextConfigurators(configurations,
            ComponentConfigurator.class);
        if (testClass instanceof ComponentConfigurator configurator) {
            instances.add(configurator);
        }
        instances.forEach(instance -> instance.configureComponents(registry));
        getBareJsfTestSetups(configurations, ComponentConfigurator.class)
            .forEach(instance -> instance.configureComponents(registry));
    }

    /**
     * Instantiates the given {@link ApplicationConfigurator} and calls them with
     * the given {@link ApplicationConfigDecorator}. In case the given testClass
     * instance also implements {@link ApplicationConfigurator} the corresponding
     * method will be called <em>after</em> the others
     *
     * @param testClass      the actual instance of test must not be null
     * @param registry       to be passed the individual instances of
     *                       {@link ApplicationConfigurator}, must not be null
     * @param configurations the previously extracted annotations must not be null
     *                       but may be empty.
     */
    public static void configureApplication(final Object testClass, final ApplicationConfigDecorator registry,
        final Collection<JsfTestConfiguration> configurations) {
        requireNonNull(testClass);
        requireNonNull(registry);
        requireNonNull(configurations);
        final List<ApplicationConfigurator> instances = getAssignableContextConfigurators(configurations,
            ApplicationConfigurator.class);
        if (testClass instanceof ApplicationConfigurator configurator) {
            instances.add(configurator);
        }
        instances.forEach(instance -> instance.configureApplication(registry));
        getBareJsfTestSetups(configurations, ApplicationConfigurator.class)
            .forEach(instance -> instance.configureApplication(registry));
    }

    /**
     * Instantiates the given {@link RequestConfigurator} and calls them with the
     * given {@link RequestConfigDecorator}. In case the given testClass instance
     * also implements {@link RequestConfigurator} the corresponding method will be
     * called <em>after</em> the others
     *
     * @param testClass      the actual instance of test must not be null
     * @param registry       to be passed the individual instances of
     *                       {@link RequestConfigurator}, must not be null
     * @param configurations the previously extracted annotations must not be null
     *                       but may be empty.
     */
    public static void configureRequestConfig(final Object testClass, final RequestConfigDecorator registry,
        final Collection<JsfTestConfiguration> configurations) {
        requireNonNull(testClass);
        requireNonNull(registry);
        requireNonNull(configurations);
        final List<RequestConfigurator> instances = getAssignableContextConfigurators(configurations,
            RequestConfigurator.class);
        if (testClass instanceof RequestConfigurator configurator) {
            instances.add(configurator);
        }
        instances.forEach(instance -> instance.configureRequest(registry));
        getBareJsfTestSetups(configurations, RequestConfigurator.class)
            .forEach(instance -> instance.configureRequest(registry));
    }

    /**
     * @param configurations all configurators
     * @param configurator   class to check if assignable
     * @param <T>            target type
     * @return list of {@link JsfTestSetup} with target type
     */
    @SuppressWarnings("unchecked")
    private static <T extends JsfTestSetup> List<T> getAssignableContextConfigurators(
        final Collection<JsfTestConfiguration> configurations, final Class<T> configurator) {
        final List<T> instances = new ArrayList<>();
        for (final JsfTestConfiguration config : configurations) {
            for (final Class<? extends JsfTestSetup> type : config.value()) {
                if (configurator.isAssignableFrom(type)) {
                    instances.add((T) new DefaultInstantiator<>(type).newInstance());
                }
            }
        }
        return instances;
    }

    /**
     * Collects instances of bare {@link JsfTestSetup} implementors — types referenced by
     * {@link JsfTestConfiguration#value()} that do <em>not</em> implement the given legacy
     * sub-interface. These implementors are dispatched via the {@link JsfTestSetup} default
     * methods, while legacy implementors are handled by the per-sub-interface dispatch path
     * to avoid double-invocation.
     *
     * @param configurations the previously extracted annotations, must not be null
     * @param legacyConfigurator the legacy sub-interface whose implementors are already
     *                           dispatched elsewhere and must be excluded here
     * @return list of {@link JsfTestSetup} instances that are not instances of the legacy
     *         sub-interface
     */
    private static List<JsfTestSetup> getBareJsfTestSetups(final Collection<JsfTestConfiguration> configurations,
        final Class<? extends JsfTestSetup> legacyConfigurator) {
        final List<JsfTestSetup> instances = new ArrayList<>();
        for (final JsfTestConfiguration config : configurations) {
            for (final Class<? extends JsfTestSetup> type : config.value()) {
                if (!legacyConfigurator.isAssignableFrom(type)) {
                    instances.add(new DefaultInstantiator<>(type).newInstance());
                }
            }
        }
        return instances;
    }
}
