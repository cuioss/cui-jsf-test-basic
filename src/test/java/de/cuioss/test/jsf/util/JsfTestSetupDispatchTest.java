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

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.JsfTestSetup;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that a bare {@link JsfTestSetup} implementor — one that does <em>not</em>
 * also implement any of the legacy configurator sub-interfaces — has all three
 * {@code configure*} default methods dispatched by {@code ConfigurationHelper}.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(JsfTestSetupDispatchTest.BareSetup.class)
class JsfTestSetupDispatchTest {

    @Test
    void shouldDispatchAllThreeConfigureMethodsForBareSetup(FacesContext facesContext) {
        assertNotNull(facesContext);
        assertAll("bare JsfTestSetup dispatch",
            () -> assertTrue(BareSetup.applicationCalled, "configureApplication should be dispatched"),
            () -> assertTrue(BareSetup.componentsCalled, "configureComponents should be dispatched"),
            () -> assertTrue(BareSetup.requestCalled, "configureRequest should be dispatched"));
    }

    /**
     * A configuration class implementing only {@link JsfTestSetup}, overriding all three
     * configure methods to record that each was dispatched.
     */
    public static class BareSetup implements JsfTestSetup {

        static boolean applicationCalled;
        static boolean componentsCalled;
        static boolean requestCalled;

        @Override
        public void configureApplication(ApplicationConfigDecorator applicationConfig) {
            applicationCalled = true;
        }

        @Override
        public void configureComponents(ComponentConfigDecorator componentConfig) {
            componentsCalled = true;
        }

        @Override
        public void configureRequest(RequestConfigDecorator requestConfig) {
            requestCalled = true;
        }
    }
}
