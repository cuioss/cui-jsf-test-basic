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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.JsfTestContextConfigurator;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Helper class providing some utility methods for handling configuration.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1118") // owolff: lombok generated
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
    }

    /**
     * Instantiates the given {@link BeanConfigurator} and calls them with the given
     * {@link BeanConfigDecorator}. In case the given testClass instance also
     * implements {@link BeanConfigurator} the corresponding method will be called
     * <em>after</em> the others
     *
     * @param testClass      the actual instance of test, must not be null
     * @param registry       to be passed the the individual instances of
     *                       {@link BeanConfigurator}, must not be null
     * @param configurations the previously extracted annotations, must not be null
     *                       but may be empty.
     */
    public static void configureManagedBeans(final Object testClass, final BeanConfigDecorator registry,
            final Collection<JsfTestConfiguration> configurations) {
        requireNonNull(testClass);
        requireNonNull(registry);
        requireNonNull(configurations);
        final List<BeanConfigurator> instances = getAssignableContextConfigurators(configurations,
                BeanConfigurator.class);
        if (testClass instanceof BeanConfigurator configurator) {
            instances.add(configurator);
        }
        instances.forEach(instance -> instance.configureBeans(registry));
    }

    /**
     * Instantiates the given {@link ApplicationConfigurator} and calls them with
     * the given {@link ApplicationConfigDecorator}. In case the given testClass
     * instance also implements {@link ApplicationConfigurator} the corresponding
     * method will be called <em>after</em> the others
     *
     * @param testClass      the actual instance of test, must not be null
     * @param registry       to be passed the the individual instances of
     *                       {@link ApplicationConfigurator}, must not be null
     * @param configurations the previously extracted annotations, must not be null
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
    }

    /**
     * Instantiates the given {@link RequestConfigurator} and calls them with the
     * given {@link RequestConfigDecorator}. In case the given testClass instance
     * also implements {@link RequestConfigurator} the corresponding method will be
     * called <em>after</em> the others
     *
     * @param testClass      the actual instance of test, must not be null
     * @param registry       to be passed the the individual instances of
     *                       {@link RequestConfigurator}, must not be null
     * @param configurations the previously extracted annotations, must not be null
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
    }

    /**
     * @param configurations all configurators
     * @param configurator   class to check if assignable
     * @param <T>            target type
     * @return list of {@link JsfTestContextConfigurator} with target type
     */
    @SuppressWarnings("unchecked")
    private static <T extends JsfTestContextConfigurator> List<T> getAssignableContextConfigurators(
            final Collection<JsfTestConfiguration> configurations, final Class<T> configurator) {
        final List<T> instances = new ArrayList<>();
        for (final JsfTestConfiguration config : configurations) {
            for (final Class<? extends JsfTestContextConfigurator> type : config.value()) {
                if (configurator.isAssignableFrom(type)) {
                    instances.add((T) new DefaultInstantiator<>(type).newInstance());
                }
            }
        }
        return instances;
    }
}
