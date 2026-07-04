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
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Regression test for LIFE-2: a class-level {@link JsfTestConfiguration} must be
 * applied exactly once per test. Before the fix, {@code beforeEach} re-applied the
 * class-level configuration that {@code postProcessTestInstance} had already applied,
 * so a non-idempotent configurator ran twice per test.
 * <p>
 * The class deliberately declares a single test method so the static counter reflects
 * exactly one test instance.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(SingleClassConfigurationApplicationTest.CountingConfig.class)
class SingleClassConfigurationApplicationTest {

    @Test
    void classLevelConfigurationAppliedExactlyOnce(FacesContext facesContext) {
        assertEquals(1, CountingConfig.applicationCount,
            "A class-level @JsfTestConfiguration must be applied exactly once per test");
    }

    /** Counts how often {@code configureApplication} is invoked. */
    public static class CountingConfig implements JsfTestSetup {
        static int applicationCount;

        @Override
        public void configureApplication(ApplicationConfigDecorator applicationConfig) {
            applicationCount++;
        }
    }
}
