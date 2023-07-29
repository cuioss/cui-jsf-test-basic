package de.cuioss.test.jsf.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.config.renderer.VerifyComponentRendererConfig;
import de.cuioss.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.cuioss.test.jsf.mocks.CuiMockRenderer;

@VetoRenderAttributeAssert({ CommonRendererAsserts.PASSTHROUGH, CommonRendererAsserts.STYLE,
        CommonRendererAsserts.STYLE_CLASS })
@VerifyComponentRendererConfig(wrapComponentInForm = true)
class AbstractComponentRendererTestWithConfigTest extends AbstractComponentRendererTest<CuiMockRenderer> {

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
        assertTrue(super.isWrapComponentInForm());
        assertNotNull(getWrappedComponent());
        assertNotNull(getWrappedComponent().getParent());
        assertEquals(HtmlForm.class, getWrappedComponent().getParent().getClass());
    }
}
