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
import de.cuioss.test.jsf.util.ConfigurableApplication;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link EnableJsfEnvironment} annotation with nested classes and methods.
 */
@EnableJsfEnvironment
class EnableJsfEnvironmentNestedTest {

    @Test
    void shouldInjectFacesContext(FacesContext facesContext) {
        assertNotNull(facesContext);
    }

    @Test
    void shouldUseIdentityResourceBundleByDefault(FacesContext facesContext) {
        var application = facesContext.getApplication();
        assertNotNull(application);
        assertTrue(((ConfigurableApplication) application).isUseIdentityResourceBundle());
    }

    @Nested
    @EnableJsfEnvironment(useIdentityResourceBundle = false)
    class NestedWithoutIdentityResourceBundle {

        @Test
        void shouldNotUseIdentityResourceBundle(FacesContext facesContext) {
            var application = facesContext.getApplication();
            assertNotNull(application);
            assertFalse(((ConfigurableApplication) application).isUseIdentityResourceBundle());
        }

        @Test
        @EnableJsfEnvironment
        void shouldUseIdentityResourceBundleWhenMethodAnnotationOverridesClass(FacesContext facesContext) {
            var application = facesContext.getApplication();
            assertNotNull(application);
            assertTrue(((ConfigurableApplication) application).isUseIdentityResourceBundle());
        }
    }

    @Nested
    class NestedWithoutAnnotation {

        @Test
        void shouldInheritParentAnnotation(FacesContext facesContext) {
            var application = facesContext.getApplication();
            assertNotNull(application);
            assertTrue(((ConfigurableApplication) application).isUseIdentityResourceBundle());
        }

        @Test
        @EnableJsfEnvironment(useIdentityResourceBundle = false)
        void shouldOverrideParentAnnotationWithMethodAnnotation(FacesContext facesContext) {
            var application = facesContext.getApplication();
            assertNotNull(application);
            assertFalse(((ConfigurableApplication) application).isUseIdentityResourceBundle());
        }
    }

    @Nested
    @EnableJsfEnvironment
    @JsfTestConfiguration(TestConfiguration.class)
    class NestedWithConfiguration {

        @Test
        void shouldApplyConfiguration(FacesContext facesContext) {
            assertNotNull(facesContext);
            var contextPath = facesContext.getExternalContext().getRequestContextPath();
            assertEquals("/testPath", contextPath);
        }

        @Nested
        @EnableJsfEnvironment
        @JsfTestConfiguration(OverrideTestConfiguration.class)
        class NestedWithOverrideConfiguration {

            @Test
            void shouldOverrideConfigurationWithNestedClassAnnotation(FacesContext facesContext) {
                assertNotNull(facesContext);
                var contextPath = facesContext.getExternalContext().getRequestContextPath();
                assertEquals("/overridePath", contextPath);
            }
        }

        @Test
        @EnableJsfEnvironment(useIdentityResourceBundle = false)
        void shouldOverrideIdentityResourceBundleWithMethodAnnotation(FacesContext facesContext) {
            var application = facesContext.getApplication();
            assertNotNull(application);
            assertFalse(((ConfigurableApplication) application).isUseIdentityResourceBundle());
        }

        @Test
        @EnableJsfEnvironment
        @JsfTestConfiguration(MethodLevelTestConfiguration.class)
        void shouldApplyMethodLevelConfiguration(FacesContext facesContext) {
            assertNotNull(facesContext);
            var contextPath = facesContext.getExternalContext().getRequestContextPath();
            assertEquals("/methodLevelPath", contextPath);
        }
    }

    /**
     * Test configuration for nested class.
     */
    public static class TestConfiguration implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            decorator.setContextPath("/testPath");
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
     * Test configuration for method-level override.
     */
    public static class OverrideTestConfiguration implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            decorator.setContextPath("/overridePath");
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
    public static class MethodLevelTestConfiguration implements ApplicationConfigurator, ComponentConfigurator, RequestConfigurator {
        @Override
        public void configureApplication(ApplicationConfigDecorator decorator) {
            decorator.setContextPath("/methodLevelPath");
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
