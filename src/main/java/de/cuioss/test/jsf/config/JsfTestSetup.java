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
package de.cuioss.test.jsf.config;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;

/**
 * Unified configuration entry-point for JSF tests, intended to be referenced by
 * {@link JsfTestConfiguration}. It is the non-deprecated replacement for the
 * {@link JsfTestContextConfigurator} hierarchy (the {@code ApplicationConfigurator},
 * {@code ComponentConfigurator} and {@code RequestConfigurator} sub-interfaces).
 * <p>
 * Implementations override only the configuration aspects they care about; every
 * method has an empty default implementation, so a configuration class is free to
 * implement just one, two, or all three callbacks.
 * <p>
 * Each callback receives the corresponding decorator and may use its fluent API to
 * set up the JSF test environment:
 * <ul>
 *   <li>{@link #configureApplication(ApplicationConfigDecorator)} — navigation cases,
 *       resource bundles, and other application-level settings.</li>
 *   <li>{@link #configureComponents(ComponentConfigDecorator)} — registration of
 *       components, renderers, converters, and validators.</li>
 *   <li>{@link #configureRequest(RequestConfigDecorator)} — request and response
 *       level configuration.</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
public interface JsfTestSetup {

    /**
     * Configures application-level aspects of the JSF test environment, such as
     * navigation cases and resource bundles. The default implementation does nothing.
     *
     * @param applicationConfig the decorator providing the application configuration API,
     *                          never {@code null}
     */
    default void configureApplication(ApplicationConfigDecorator applicationConfig) {
        // no-op by default; implementations override as needed
    }

    /**
     * Registers components, renderers, converters, and validators with the JSF test
     * environment. The default implementation does nothing.
     *
     * @param componentConfig the decorator providing the component configuration API,
     *                        never {@code null}
     */
    default void configureComponents(ComponentConfigDecorator componentConfig) {
        // no-op by default; implementations override as needed
    }

    /**
     * Configures request- and response-level aspects of the JSF test environment.
     * The default implementation does nothing.
     *
     * @param requestConfig the decorator providing the request configuration API,
     *                      never {@code null}
     */
    default void configureRequest(RequestConfigDecorator requestConfig) {
        // no-op by default; implementations override as needed
    }
}
