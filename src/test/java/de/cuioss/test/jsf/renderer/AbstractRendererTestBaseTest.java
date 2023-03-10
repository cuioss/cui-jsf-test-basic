package de.cuioss.test.jsf.renderer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.faces.component.html.HtmlInputText;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.mocks.CuiMockRenderer;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import lombok.Getter;

class AbstractRendererTestBaseTest extends AbstractRendererTestBase<CuiMockRenderer> {

    public static final String RENDER_RESULT =
        String.format("<HtmlInputText id=\"j_id%s\" name=\"j_id%s\" type=\"text\"/>", "__v_0", "__v_0");

    @Getter
    private HtmlInputText component;

    @BeforeEach
    void beforeTest() {
        component = new HtmlInputText();
    }

    @Test
    void shouldRenderGoodCase() {
        assertEquals(RENDER_RESULT, assertDoesNotThrow(() -> renderToString(component)));
        assertRenderResult(component, RENDER_RESULT);
        assertRenderResult(component, DomUtils.htmlStringToDocument(RENDER_RESULT));
    }

    @Test
    void assertEmptyRenderResult() {
        component.setRendered(false);

        assertEmptyRenderResult(component);
    }

    @Test
    void assertFailOnNonEmptyResult() {
        component.setRendered(true);
        assertThrows(AssertionError.class, () -> assertEmptyRenderResult(component));
    }
}
