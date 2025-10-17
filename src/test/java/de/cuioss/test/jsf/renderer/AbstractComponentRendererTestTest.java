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
import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

@VetoRenderAttributeAssert({CommonRendererAsserts.PASSTHROUGH, CommonRendererAsserts.STYLE,
    CommonRendererAsserts.STYLE_CLASS})
class AbstractComponentRendererTestTest extends AbstractComponentRendererTest<CuiMockRenderer> {

    @Test
    void shouldDetectCorrextVetoes() {
        assertEquals(2, getActiveAsserts().size());
        assertTrue(getActiveAsserts().contains(CommonRendererAsserts.ID));
        assertTrue(getActiveAsserts().contains(CommonRendererAsserts.RENDERED));
    }

    @Override
    protected UIComponent getComponent() {
        return new HtmlInputText();
    }

    @Test
    void shouldHandleConfigAnnotation(FacesContext facesContext) {
        assertFalse(super.isWrapComponentInForm());
        assertEquals(HtmlInputText.class, getWrappedComponent().getClass());
        assertNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE));
    }

    @Test
    void shouldNotBeNestedInForm() {
        assertNull(getWrappedComponent().getParent());
    }

    @Test
    void shouldHandleExtractEventsFromViewRoot(FacesContext facesContext) {
        assertTrue(extractEventsFromViewRoot(facesContext).isEmpty());
        var component = new HtmlInputText();
        component.setParent(facesContext.getViewRoot());
        assertTrue(extractEventsFromViewRoot(facesContext).isEmpty());
        component.queueEvent(new FacesEvent(component) {

            @Serial private static final long serialVersionUID = 591161343873329974L;

            @Override
            public void processListener(final FacesListener faceslistener) {
                // Noop, for test purposes
            }

            @Override
            public boolean isAppropriateListener(final FacesListener faceslistener) {
                return false;
            }
        });
        assertEquals(1, extractEventsFromViewRoot(facesContext).size());
    }
}
