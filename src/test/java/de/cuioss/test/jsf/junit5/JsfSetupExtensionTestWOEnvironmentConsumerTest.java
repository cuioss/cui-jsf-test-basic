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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.context.FacesContext;

import org.apache.myfaces.test.mock.MockFacesContext22;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;

@EnableJsfEnvironment
@JsfTestConfiguration(BasicApplicationConfiguration.class)
class JsfSetupExtensionTestWOEnvironmentConsumerTest {

    @Test
    void shouldBootstrapJsf() {
        assertNotNull(FacesContext.getCurrentInstance());
        assertEquals(MockFacesContext22.class, FacesContext.getCurrentInstance().getClass());
    }

}
