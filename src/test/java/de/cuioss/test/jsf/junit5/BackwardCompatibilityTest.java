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

import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests that verify backward compatibility with the existing approach using
 * {@link JsfEnvironmentConsumer}.
 * <p>
 * This test class implements {@link JsfEnvironmentConsumer} to receive the
 * {@link JsfEnvironmentHolder} via the setter method, and also uses parameter
 * resolution in its test methods. This verifies that both approaches can be used
 * simultaneously.
 */
@EnableJsfEnvironment
class BackwardCompatibilityTest implements JsfEnvironmentConsumer {

    @Getter
    @Setter
    private JsfEnvironmentHolder environmentHolder;

    /**
     * Test that the {@link JsfEnvironmentHolder} is properly injected via the
     * {@link JsfEnvironmentConsumer} interface.
     */
    @Test
    void shouldInjectEnvironmentHolderViaConsumer() {
        assertNotNull(environmentHolder, "JsfEnvironmentHolder should be injected via consumer");
        assertNotNull(getFacesContext(), "FacesContext should be accessible via consumer");
        assertNotNull(getApplication(), "Application should be accessible via consumer");
        assertNotNull(getComponentConfigDecorator(), "ComponentConfigDecorator should be accessible via consumer");
    }

    /**
     * Test that parameter resolution works in a class that also implements
     * {@link JsfEnvironmentConsumer}.
     */
    @Test
    void shouldResolveParametersInConsumerClass(FacesContext facesContext, Application application) {
        assertNotNull(facesContext, "FacesContext should be resolved as parameter");
        assertNotNull(application, "Application should be resolved as parameter");
    }

    /**
     * Test that objects obtained via parameter resolution are the same as those
     * obtained via the {@link JsfEnvironmentConsumer} interface.
     */
    @Test
    void shouldProvideConsistentObjects(FacesContext facesContext, ComponentConfigDecorator componentConfigDecorator) {
        // Objects from parameter resolution
        assertNotNull(facesContext, "FacesContext should be resolved as parameter");
        assertNotNull(componentConfigDecorator, "ComponentConfigDecorator should be resolved as parameter");

        // Objects from JsfEnvironmentConsumer
        assertNotNull(getFacesContext(), "FacesContext should be accessible via consumer");
        assertNotNull(getComponentConfigDecorator(), "ComponentConfigDecorator should be accessible via consumer");

        // Verify they are the same objects
        assertEquals(getFacesContext(), facesContext,
                "FacesContext from consumer should be the same as from parameter resolution");

        // Note: ComponentConfigDecorator is created on demand, so we can't compare instances directly
        // Instead, verify they both work by registering a mock component
        getComponentConfigDecorator().registerCuiMockComponentWithRenderer();
        componentConfigDecorator.registerCuiMockComponentWithRenderer();

        // If we got here without exceptions, both decorators are working properly
    }
}
