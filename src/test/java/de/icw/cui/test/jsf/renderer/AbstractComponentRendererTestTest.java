package de.icw.cui.test.jsf.renderer;

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

import de.icw.cui.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.icw.cui.test.jsf.mocks.CuiMockRenderer;

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
