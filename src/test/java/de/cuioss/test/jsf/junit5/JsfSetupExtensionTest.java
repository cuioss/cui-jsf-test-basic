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
import de.cuioss.test.jsf.util.ConfigurableApplication;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@JsfTestConfiguration(BasicApplicationConfiguration.class)
@EnableJsfEnvironment
class JsfSetupExtensionTest implements JsfEnvironmentConsumer {

    public static final String TO_VIEW_JSF = "/to/view.jsf";
    public static final String OUTCOME = "outcome";

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Test
    void shouldBootstrapJsf() {
        assertNotNull(environmentHolder);
        assertNotNull(getApplication());
        assertNotNull(getApplicationConfigDecorator());
        assertNotNull(getComponentConfigDecorator());
        assertNotNull(getExternalContext());
        assertNotNull(getFacesContext());
        assertNotNull(getRequestConfigDecorator());
        assertNotNull(getResponse());
    }

    @Test
    void shouldApplyBasicConfiguration() {
        assertEquals(BasicApplicationConfiguration.FIREFOX,
            getExternalContext().getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }

    @Test
    void shouldFailForNoNavigationOutcome() {
        // No Navigation took place -> Assertion Error
        assertThrows(AssertionError.class, () ->
            assertNavigatedWithOutcome(OUTCOME));
    }

    @Test
    void shouldAssertNavigationOutcome() {
        getApplicationConfigDecorator().registerNavigationCase(OUTCOME, TO_VIEW_JSF);
        getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, OUTCOME);
        assertNavigatedWithOutcome(OUTCOME);
    }

    @Test
    void shouldFailForNoRedirect() {
        // No Navigation took place -> Assertion Error
        assertThrows(AssertionError.class, () ->
            assertRedirect(TO_VIEW_JSF));
    }

    @Test
    void shouldAssertRedirect() throws IOException {
        getExternalContext().redirect(TO_VIEW_JSF);
        assertRedirect(TO_VIEW_JSF);
    }

    @Test
    void shouldWrapConfigurableApplication() {
        assertEquals(ConfigurableApplication.class, getApplication().getClass());
    }

    @Test
    void shouldDefaultToMirrorResourceBundle() {
        ResourceBundleVarNames.addVarName("msg", "msg");
        var resourceBundle = getApplication().getResourceBundle(getFacesContext(), "msg");
        assertNotNull(resourceBundle);
        assertEquals("some.key", resourceBundle.getString("some.key"));
    }
}
