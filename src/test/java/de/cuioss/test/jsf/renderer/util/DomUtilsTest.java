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
package de.cuioss.test.jsf.renderer.util;

import org.junit.jupiter.api.Test;

import static de.cuioss.test.jsf.renderer.util.DomUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class DomUtilsTest {

    private static final String FIRST_PART = "first";

    private static final String STYLE = "style";

    private static final String CLASS = "class";

    public static final String NESTED_DIV = "<div><div /></div>";

    public static final String NESTED_DIV_WITH_STYLE = "<div style=\"first\"><div style=\"second\"/></div>";

    public static final String NESTED_DIV_WITH_MULITPLE = "<div style=\"first\" class=\"firstClass\"><div style=\"second\"/></div>";

    @Test
    void shouldCreateDomFromHml() {
        var doc = htmlStringToDocument(NESTED_DIV);
        assertNotNull(doc);
        assertEquals("root", doc.getRootElement().getName());
        assertEquals(1, doc.getRootElement().getChildren().size());
        assertEquals("div", doc.getRootElement().getChildren().getFirst().getName());
    }

    @Test
    void shouldFilterAttributes() {
        assertTrue(filterForAttribute(htmlStringToDocument(NESTED_DIV).getRootElement(), STYLE).isEmpty());
        assertEquals(2, filterForAttribute(htmlStringToDocument(NESTED_DIV_WITH_STYLE).getRootElement(), STYLE).size());
        assertEquals(2,
            filterForAttribute(htmlStringToDocument(NESTED_DIV_WITH_MULITPLE).getRootElement(), STYLE).size());
        assertEquals(1,
            filterForAttribute(htmlStringToDocument(NESTED_DIV_WITH_MULITPLE).getRootElement(), CLASS).size());
    }

    @Test
    void shouldFilterAttributesWithGivenContentPart() {
        assertTrue(
            filterForAttributeContainingValue(htmlStringToDocument(NESTED_DIV).getRootElement(), STYLE, FIRST_PART)
                .isEmpty());
        assertEquals(1, filterForAttributeContainingValue(htmlStringToDocument(NESTED_DIV_WITH_STYLE).getRootElement(),
            STYLE, FIRST_PART).size());
        assertEquals(1,
            filterForAttributeContainingValue(htmlStringToDocument(NESTED_DIV_WITH_MULITPLE).getRootElement(),
                STYLE, FIRST_PART).size());
        assertEquals(0,
            filterForAttributeContainingValue(htmlStringToDocument(NESTED_DIV_WITH_MULITPLE).getRootElement(),
                STYLE, "not.there").size());
    }

    @Test
    void shouldFailOnInvalidString() {
        assertThrows(IllegalArgumentException.class, () ->
            htmlStringToDocument("<a></b>"));
    }
}
