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

import de.cuioss.test.jsf.mocks.CuiMockRenderer;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AbstractRendererTestBase")
class AbstractRendererTestBaseTest extends AbstractRendererTestBase<CuiMockRenderer> {

    public static final String RENDER_RESULT = "<HtmlInputText id=\"j_id%s\" name=\"j_id%s\" type=\"text\"/>"
        .formatted("__v_0", "__v_0");

    @Getter
    private HtmlInputText component;

    @BeforeEach
    void beforeTest() {
        component = new HtmlInputText();
    }

    @Test
    @DisplayName("Should render the component and match the expected result in all forms")
    void shouldRenderGoodCase(FacesContext facesContext) {
        var rendered = assertDoesNotThrow(() -> renderToString(component, facesContext),
            "Rendering the component should not throw");

        assertEquals(RENDER_RESULT, rendered, "Rendered output should match the expected markup");
        assertDoesNotThrow(() -> assertRenderResult(component, RENDER_RESULT, facesContext),
            "String-based render assertion should succeed");
        assertDoesNotThrow(() -> assertRenderResult(component, DomUtils.htmlStringToDocument(RENDER_RESULT), facesContext),
            "Document-based render assertion should succeed");
    }

    @Test
    @DisplayName("Should assert an empty render result when the component is not rendered")
    void shouldAssertEmptyRenderResult(FacesContext facesContext) {
        component.setRendered(false);

        assertDoesNotThrow(() -> assertEmptyRenderResult(component, facesContext),
            "An unrendered component should produce an empty render result");
    }

    @Test
    @DisplayName("Should fail the empty-result assertion when the component renders output")
    void shouldFailOnNonEmptyResult(FacesContext facesContext) {
        component.setRendered(true);

        assertThrows(AssertionError.class, () -> assertEmptyRenderResult(component, facesContext),
            "A rendered component should fail the empty-result assertion");
    }
}
