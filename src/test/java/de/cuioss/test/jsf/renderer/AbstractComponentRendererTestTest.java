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
package de.cuioss.test.jsf.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.cuioss.test.jsf.mocks.CuiMockRenderer;

@VetoRenderAttributeAssert({ CommonRendererAsserts.PASSTHROUGH, CommonRendererAsserts.STYLE,
        CommonRendererAsserts.STYLE_CLASS })
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
    void shouldHandleConfigAnnotation() {
        assertFalse(super.isWrapComponentInForm());
        assertEquals(HtmlInputText.class, getWrappedComponent().getClass());
        assertNull(getFacesContext().getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE));
    }

    @Test
    void shouldNotBeNestedInForm() {
        assertNull(getWrappedComponent().getParent());
    }

    @Test
    void shouldHandleExtractEventsFromViewRoot() {
        assertTrue(extractEventsFromViewRoot().isEmpty());
        var component = new HtmlInputText();
        component.setParent(getFacesContext().getViewRoot());
        assertTrue(extractEventsFromViewRoot().isEmpty());
        component.queueEvent(new FacesEvent(component) {

            private static final long serialVersionUID = 591161343873329974L;

            @Override
            public void processListener(final FacesListener faceslistener) {
            }

            @Override
            public boolean isAppropriateListener(final FacesListener faceslistener) {
                return false;
            }
        });
        assertEquals(1, extractEventsFromViewRoot().size());
    }
}
