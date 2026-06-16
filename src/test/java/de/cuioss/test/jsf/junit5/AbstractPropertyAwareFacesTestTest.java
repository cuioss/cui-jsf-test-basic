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

import de.cuioss.test.jsf.support.beans.MediumComplexityBean;
import de.cuioss.test.jsf.util.ConfigurableApplication;
import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractPropertyAwareFacesTestTest extends AbstractPropertyAwareFacesTest<MediumComplexityBean> {

    @Test
    @DisplayName("Should scan the target bean and expose its property metadata")
    void shouldScanBean() {
        assertEquals(MediumComplexityBean.class, getTargetBeanClass(),
            "Target bean class should be the generic type argument");
        assertEquals(10, getPropertyMetadata().size(),
            "All bean properties should be discovered");
    }

    @Test
    @DisplayName("Should resolve FacesContext via parameter resolution")
    void shouldSetupJsfEnvironment(FacesContext facesContext) {
        assertNotNull(facesContext, "FacesContext should be resolved as parameter");
    }

    @Test
    @DisplayName("Should inherit the identity resource bundle setting")
    void shouldInheritUseIdentityBundle(Application application) {
        var configurableApplication = assertInstanceOf(ConfigurableApplication.class, application,
            "Application should be a ConfigurableApplication");

        assertTrue(configurableApplication.isUseIdentityResourceBundle(),
            "Identity resource bundle should be enabled by default");
    }
}
