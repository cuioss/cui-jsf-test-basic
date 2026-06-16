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

import de.cuioss.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.cuioss.test.jsf.mocks.CuiMockRenderer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIForm;
import jakarta.faces.component.html.HtmlForm;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

@VetoRenderAttributeAssert({CommonRendererAsserts.PASSTHROUGH, CommonRendererAsserts.STYLE,
    CommonRendererAsserts.STYLE_CLASS})
@DisplayName("AbstractComponentRendererTest")
class AbstractComponentRendererTestTest extends AbstractComponentRendererTest<CuiMockRenderer> {

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
    @DisplayName("Should not wrap the component in a form when configuration is absent")
    void shouldHandleConfigAnnotation(FacesContext facesContext) {
        assertFalse(isWrapComponentInForm(), "Component should not be wrapped in a form by default");
        assertEquals(HtmlInputText.class, getWrappedComponent().getClass(),
            "Wrapped component should be the configured component");
        assertNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE),
            "No form renderer should be registered when wrapping is disabled");
    }

    @Test
    @DisplayName("Should leave the component without a parent when not nested in a form")
    void shouldNotBeNestedInForm() {
        assertNull(getWrappedComponent().getParent(), "Component should have no parent without form wrapping");
    }

    @Test
    @DisplayName("Should extract a queued event from the view root")
    void shouldHandleExtractEventsFromViewRoot(FacesContext facesContext) {
        var component = new HtmlInputText();
        component.setParent(facesContext.getViewRoot());
        component.queueEvent(new FacesEvent(component) {

            @Serial
            private static final long serialVersionUID = 591161343873329974L;

            @Override
            public void processListener(final FacesListener faceslistener) {
                // Noop, for test purposes
            }

            @Override
            public boolean isAppropriateListener(final FacesListener faceslistener) {
                return false;
            }
        });

        var events = extractEventsFromViewRoot(facesContext);

        assertEquals(1, events.size(), "Exactly one queued event should be extracted from the view root");
    }

    @Test
    @DisplayName("Should extract no events when the view root has none queued")
    void shouldExtractNoEventsWhenViewRootIsEmpty(FacesContext facesContext) {
        var component = new HtmlInputText();
        component.setParent(facesContext.getViewRoot());

        var events = extractEventsFromViewRoot(facesContext);

        assertTrue(events.isEmpty(), "No events should be extracted when none are queued");
    }
}
