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

import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.JsfTestSetup;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression test for LIFE-4: a configuration class that implements one legacy
 * configurator interface ({@link ApplicationConfigurator}) and also overrides a
 * <em>different</em> {@link JsfTestSetup} phase ({@code configureComponents}) must
 * still have that override invoked.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(ConfigurationDispatchRegressionTest.MixedConfig.class)
class ConfigurationDispatchRegressionTest {

    @Test
    void mixedConfiguratorOtherPhaseInvoked(FacesContext facesContext) {
        assertTrue(MixedConfig.applicationCalled, "configureApplication must be dispatched");
        assertTrue(MixedConfig.componentsCalled,
            "configureComponents override on a legacy ApplicationConfigurator must be dispatched (LIFE-4)");
    }

    /** Implements the legacy {@link ApplicationConfigurator} but also overrides another phase. */
    public static class MixedConfig implements ApplicationConfigurator {
        static boolean applicationCalled;
        static boolean componentsCalled;

        @Override
        public void configureApplication(ApplicationConfigDecorator applicationConfig) {
            applicationCalled = true;
        }

        @Override
        public void configureComponents(ComponentConfigDecorator componentConfig) {
            componentsCalled = true;
        }
    }
}
