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

    public static final String RENDER_RESULT = "<HtmlInputText id=\"j_id%s\" name=\"j_id%s\" type=\"text\"/>"
            .formatted("__v_0", "__v_0");

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
