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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableJsfEnvironment
@JsfTestConfiguration(BasicApplicationConfiguration.class)
class JsfSetupExtensionTestWOEnvironmentConsumerTest {

    @Test
    @DisplayName("Should bootstrap JSF without implementing JsfEnvironmentConsumer")
    void shouldBootstrapJsf(FacesContext facesContext) {
        assertNotNull(facesContext, "FacesContext should be resolved as parameter");
        assertEquals(FacesContext.getCurrentInstance(), facesContext,
            "Resolved FacesContext should match the current thread-local instance");
        assertInstanceOf(MockFacesContext.class, facesContext,
            "FacesContext should be a MockFacesContext");
    }

}
