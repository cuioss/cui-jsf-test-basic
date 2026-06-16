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
package de.cuioss.test.jsf.renderer;

import de.cuioss.test.jsf.config.renderer.VerifyComponentRendererConfig;
import de.cuioss.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.cuioss.test.jsf.mocks.CuiMockRenderer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlForm;
import jakarta.faces.component.html.HtmlInputText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@VetoRenderAttributeAssert({CommonRendererAsserts.PASSTHROUGH, CommonRendererAsserts.STYLE,
    CommonRendererAsserts.STYLE_CLASS})
@VerifyComponentRendererConfig(wrapComponentInForm = true)
@DisplayName("AbstractComponentRendererTest with form-wrapping configuration")
class AbstractComponentRendererTestWithConfigTest extends AbstractComponentRendererTest<CuiMockRenderer> {

    @Test
    @DisplayName("Should retain only the non-vetoed render attribute asserts")
    void shouldDetectCorrectVetoes() {
        var activeAsserts = getActiveAsserts();

        assertEquals(2, activeAsserts.size(), "Two asserts should remain after vetoing three");
        assertTrue(activeAsserts.contains(CommonRendererAsserts.ID), "ID assert should remain active");
        assertTrue(activeAsserts.contains(CommonRendererAsserts.RENDERED), "RENDERED assert should remain active");
    }

    @Override
    protected UIComponent getComponent() {
        return new HtmlInputText();
    }

    @Test
    @DisplayName("Should wrap the component in a form when configuration enables it")
    void shouldHandleConfigAnnotation() {
        assertTrue(isWrapComponentInForm(), "Component should be wrapped in a form when configured");

        var wrapped = getWrappedComponent();

        assertNotNull(wrapped, "Wrapped component should not be null");
        assertNotNull(wrapped.getParent(), "Wrapped component should have a parent");
        assertEquals(HtmlForm.class, wrapped.getParent().getClass(), "Parent should be an HtmlForm");
    }
}
