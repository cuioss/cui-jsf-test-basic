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
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
    void shouldRenderGoodCase(FacesContext facesContext) throws IOException {
        assertEquals(RENDER_RESULT, assertDoesNotThrow(() -> renderToString(component, facesContext)));
        assertRenderResult(component, RENDER_RESULT, facesContext);
        assertRenderResult(component, DomUtils.htmlStringToDocument(RENDER_RESULT), facesContext);
    }

    @Test
    void assertEmptyRenderResult(FacesContext facesContext) {
        component.setRendered(false);

        assertEmptyRenderResult(component, facesContext);
    }

    @Test
    void assertFailOnNonEmptyResult(FacesContext facesContext) {
        component.setRendered(true);
        assertThrows(AssertionError.class, () -> assertEmptyRenderResult(component, facesContext));
    }
}
