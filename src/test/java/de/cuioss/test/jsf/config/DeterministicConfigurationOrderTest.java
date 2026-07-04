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
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Regression test for LIFE-1: multiple {@link JsfTestConfiguration} annotations must
 * be applied in their declared order (deterministic last-wins). Before the fix the
 * purpose-built {@code LinkedHashSet} was re-collected into a {@code HashSet}, so when
 * two configurations touched the same navigation case the winner varied between runs.
 * <p>
 * Located in the {@code config} package because the {@code @Repeatable} container
 * {@code JsfTestConfigurations} is package-private.
 */
@EnableJsfEnvironment
@JsfTestConfiguration(DeterministicConfigurationOrderTest.ConfigA.class)
@JsfTestConfiguration(DeterministicConfigurationOrderTest.ConfigB.class)
class DeterministicConfigurationOrderTest {

    static final String OUTCOME = "sharedOutcome";

    @Test
    void lastConfigurationWinsDeterministically(FacesContext facesContext,
        ApplicationConfigDecorator applicationConfig) {
        var handler = applicationConfig.getMockNavigationHandler();
        var navigationCase = handler.getNavigationCase(facesContext, null, OUTCOME);

        assertNotNull(navigationCase, "The shared navigation case must be registered");
        assertEquals("/viewB.xhtml", navigationCase.getToViewId(facesContext),
            "The last-declared configuration (ConfigB) must win deterministically");
    }

    /** Registers the shared outcome to view A. */
    public static class ConfigA implements JsfTestSetup {
        @Override
        public void configureApplication(ApplicationConfigDecorator applicationConfig) {
            applicationConfig.registerNavigationCase(OUTCOME, "/viewA.xhtml");
        }
    }

    /** Registers the shared outcome to view B (declared last, must win). */
    public static class ConfigB implements JsfTestSetup {
        @Override
        public void configureApplication(ApplicationConfigDecorator applicationConfig) {
            applicationConfig.registerNavigationCase(OUTCOME, "/viewB.xhtml");
        }
    }
}
