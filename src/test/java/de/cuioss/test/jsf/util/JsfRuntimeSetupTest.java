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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Direct unit test for {@link JsfRuntimeSetup} covering the setup/teardown lifecycle
 * and the LIFE-8 hardening: a repeated {@code setUp()} without an intervening
 * {@code tearDown()} must not overwrite the saved thread-context classloader.
 */
@DisplayName("JsfRuntimeSetup")
class JsfRuntimeSetupTest {

    @Test
    @DisplayName("setUp builds the JSF runtime and tearDown releases it")
    void shouldSetUpAndTearDown() {
        var originalClassLoader = Thread.currentThread().getContextClassLoader();
        var setup = new JsfRuntimeSetup();
        try {
            setup.setUp();

            assertNotNull(setup.getFacesContext(), "FacesContext should be created");
            assertNotNull(setup.getApplication(), "Application should be created");
            assertNotNull(setup.getExternalContext(), "ExternalContext should be created");
            assertNotSame(originalClassLoader, Thread.currentThread().getContextClassLoader(),
                "A dedicated thread-context classloader should be installed");

            // LIFE-8: a second setUp without tearDown must not overwrite the saved
            // original classloader (the guard makes it a no-op for the classloader).
            assertDoesNotThrow(setup::setUp, "A repeated setUp must be tolerated");
        } finally {
            setup.tearDown();
        }

        assertNull(setup.getFacesContext(), "tearDown should release the FacesContext");
        assertNull(setup.getFacesContextFactory(), "tearDown should null the FacesContextFactory");
        assertSame(originalClassLoader, Thread.currentThread().getContextClassLoader(),
            "tearDown should restore the original thread-context classloader");
    }
}
