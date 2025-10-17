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

/**
 * Instances of this are to be called by the test framework <em>after</em> the
 * initial setup is done.
 *
 * @author Oliver Wolff
 * @deprecated As of 1.0.0, replaced by parameter resolution approach. Instead of implementing this interface,
 * use {@link ApplicationConfigDecorator} as a parameter in your test methods.
 * Example:
 * <pre>
 * {@code
 * @Test
 * void testApplicationConfiguration(ApplicationConfigDecorator applicationConfig) {
 *     // Configure the application
 *     applicationConfig.registerNavigationCase("outcome", "targetViewId");
 *
 *     // Test code using the configured application
 * }
 * }
 * </pre>
 * See the migration guide for more details.
 */
@Deprecated
public interface ApplicationConfigurator extends JsfTestContextConfigurator {

    /**
     * Callback method for interacting with the {@link ApplicationConfigDecorator}
     * at the correct time.
     *
     * @param decorator is never null
     */
    void configureApplication(ApplicationConfigDecorator decorator);
}
