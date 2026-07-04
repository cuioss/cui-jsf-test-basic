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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Exercises the {@link ConfigurableFacesTest} setup/teardown lifecycle (LIFE-3). The
 * inherited {@code @BeforeEach} builds the JSF runtime and the inherited
 * {@code @AfterEach} tears it down again; running two test methods ensures the runtime
 * is re-created cleanly for each test rather than leaking a released
 * {@code MockFacesContext} into the next.
 */
class ConfigurableFacesTestLifecycleTest extends ConfigurableFacesTest {

    @Test
    void shouldProvideConfiguredEnvironment() {
        assertNotNull(getFacesContext(), "FacesContext should be available");
        assertNotNull(getApplication(), "Application should be available");
        assertNotNull(getExternalContext(), "ExternalContext should be available");
    }

    @Test
    void shouldProvideFreshEnvironmentPerTest() {
        // A second test method verifies setup runs again after the previous teardown.
        assertNotNull(getFacesContext(), "FacesContext should be re-created for each test");
        assertNotNull(getRequestConfigDecorator(), "RequestConfigDecorator should be available");
    }
}
