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

import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the aggregation of JsfTestConfiguration annotations up the class hierarchy.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(JsfTestConfigurationAggregationTest.ParentConfig.class)
class JsfTestConfigurationAggregationTest {

    private static final List<String> APPLIED_CONFIGS = new ArrayList<>();

    @Test
    void clearAppliedConfigs() {
        assertDoesNotThrow(APPLIED_CONFIGS::clear);
    }

    @Test
    void shouldApplyParentConfig(FacesContext facesContext) {
        assertNotNull(facesContext);
        assertTrue(APPLIED_CONFIGS.contains("ParentConfig"));
        assertEquals(1, APPLIED_CONFIGS.size());
        assertEquals("/parent-path", facesContext.getExternalContext().getRequestContextPath());
        APPLIED_CONFIGS.clear();
    }

    @Nested
    @JsfTestConfiguration(JsfTestConfigurationAggregationTest.NestedConfig.class)
    class NestedClass {

        @Test
        void shouldApplyParentAndNestedConfig(FacesContext facesContext) {
            assertNotNull(facesContext);
            assertTrue(APPLIED_CONFIGS.contains("ParentConfig"));
            assertTrue(APPLIED_CONFIGS.contains("NestedConfig"));
            assertEquals(2, APPLIED_CONFIGS.size());
            assertEquals("/nested-path", facesContext.getExternalContext().getRequestContextPath());
            APPLIED_CONFIGS.clear();
        }

        @Test
        @JsfTestConfiguration(JsfTestConfigurationAggregationTest.MethodConfig.class)
        void shouldApplyParentNestedAndMethodConfig(FacesContext facesContext) {
            assertNotNull(facesContext);
            assertTrue(APPLIED_CONFIGS.contains("ParentConfig"));
            assertTrue(APPLIED_CONFIGS.contains("NestedConfig"));
            assertTrue(APPLIED_CONFIGS.contains("MethodConfig"));
            assertEquals(3, APPLIED_CONFIGS.size());

            // Check that the method-level configuration takes precedence
            assertEquals("/method-path", facesContext.getExternalContext().getRequestContextPath());
            APPLIED_CONFIGS.clear();
        }
    }

    @Nested
    @JsfTestConfiguration(JsfTestConfigurationAggregationTest.SiblingConfig.class)
    class SiblingClass {

        @Test
        void shouldApplyParentAndSiblingConfig(FacesContext facesContext) {
            assertNotNull(facesContext);
            assertTrue(APPLIED_CONFIGS.contains("ParentConfig"));
            assertTrue(APPLIED_CONFIGS.contains("SiblingConfig"));
            assertEquals(2, APPLIED_CONFIGS.size());
            assertEquals("/sibling-path", facesContext.getExternalContext().getRequestContextPath());
            APPLIED_CONFIGS.clear();
        }
    }

    /**
     * Test configuration for parent class.
     */
    public static class ParentConfig implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            if (!APPLIED_CONFIGS.contains("ParentConfig")) {
                APPLIED_CONFIGS.add("ParentConfig");
            }
            decorator.setContextPath("/parent-path");
        }

        @Override
        public void configureComponents(ComponentConfigDecorator decorator) {
            // No configuration needed
        }

        @Override
        public void configureRequest(RequestConfigDecorator decorator) {
            // No configuration needed
        }
    }

    /**
     * Test configuration for nested class.
     */
    public static class NestedConfig implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            if (!APPLIED_CONFIGS.contains("NestedConfig")) {
                APPLIED_CONFIGS.add("NestedConfig");
            }
            decorator.setContextPath("/nested-path");
        }

        @Override
        public void configureComponents(ComponentConfigDecorator decorator) {
            // No configuration needed
        }

        @Override
        public void configureRequest(RequestConfigDecorator decorator) {
            // No configuration needed
        }
    }

    /**
     * Test configuration for sibling class.
     */
    public static class SiblingConfig implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            if (!APPLIED_CONFIGS.contains("SiblingConfig")) {
                APPLIED_CONFIGS.add("SiblingConfig");
            }
            decorator.setContextPath("/sibling-path");
        }

        @Override
        public void configureComponents(ComponentConfigDecorator decorator) {
            // No configuration needed
        }

        @Override
        public void configureRequest(RequestConfigDecorator decorator) {
            // No configuration needed
        }
    }

    /**
     * Test configuration for method-level annotation.
     */
    public static class MethodConfig implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            if (!APPLIED_CONFIGS.contains("MethodConfig")) {
                APPLIED_CONFIGS.add("MethodConfig");
            }
            decorator.setContextPath("/method-path");
        }

        @Override
        public void configureComponents(ComponentConfigDecorator decorator) {
            // No configuration needed
        }

        @Override
        public void configureRequest(RequestConfigDecorator decorator) {
            // No configuration needed
        }
    }
}
