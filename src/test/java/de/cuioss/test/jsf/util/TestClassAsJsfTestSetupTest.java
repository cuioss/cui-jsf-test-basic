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

import de.cuioss.test.jsf.config.JsfTestSetup;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression test for LIFE-6: a test class that itself implements the (non-deprecated)
 * {@link JsfTestSetup} replacement — without implementing any legacy configurator
 * sub-interface — must have all three {@code configure*} callbacks dispatched.
 */
@EnableJsfEnvironment
class TestClassAsJsfTestSetupTest implements JsfTestSetup {

    private boolean applicationCalled;
    private boolean componentsCalled;
    private boolean requestCalled;

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

    @Test
    void shouldDispatchAllPhasesToTestClass(FacesContext facesContext) {
        assertTrue(applicationCalled, "configureApplication must be dispatched to the test class");
        assertTrue(componentsCalled, "configureComponents must be dispatched to the test class");
        assertTrue(requestCalled, "configureRequest must be dispatched to the test class");
    }
}
