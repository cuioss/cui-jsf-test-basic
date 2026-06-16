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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.jsf.renderer.util.DomUtils.filterForAttribute;
import static de.cuioss.test.jsf.renderer.util.DomUtils.filterForAttributeContainingValue;
import static de.cuioss.test.jsf.renderer.util.DomUtils.htmlStringToDocument;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DomUtils")
class DomUtilsTest {

    private static final String FIRST_PART = "first";

    private static final String STYLE = "style";

    private static final String CLASS = "class";

    private static final String NESTED_DIV = "<div><div /></div>";

    private static final String NESTED_DIV_WITH_STYLE = "<div style=\"first\"><div style=\"second\"/></div>";

    private static final String NESTED_DIV_WITH_MULTIPLE = "<div style=\"first\" class=\"firstClass\"><div style=\"second\"/></div>";

    @Test
    @DisplayName("Should create a wrapped DOM document from an HTML string")
    void shouldCreateDomFromHtml() {
        var doc = htmlStringToDocument(NESTED_DIV);

        assertAll("Wrapped document structure",
            () -> assertNotNull(doc, "Document should be created"),
            () -> assertEquals("root", doc.getRootElement().getName(), "Root element should be the wrapper"),
            () -> assertEquals(1, doc.getRootElement().getChildren().size(), "Root should contain one child"),
            () -> assertEquals("div", doc.getRootElement().getChildren().getFirst().getName(),
                "First child should be the div element"));
    }

    @Test
    @DisplayName("Should recursively collect all attributes matching a given name")
    void shouldFilterAttributes() {
        var withoutStyle = htmlStringToDocument(NESTED_DIV).getRootElement();
        var withStyle = htmlStringToDocument(NESTED_DIV_WITH_STYLE).getRootElement();
        var withMultiple = htmlStringToDocument(NESTED_DIV_WITH_MULTIPLE).getRootElement();

        assertAll("Attribute filtering by name",
            () -> assertTrue(filterForAttribute(withoutStyle, STYLE).isEmpty(),
                "No style attributes should be found"),
            () -> assertEquals(2, filterForAttribute(withStyle, STYLE).size(),
                "Both style attributes should be found"),
            () -> assertEquals(2, filterForAttribute(withMultiple, STYLE).size(),
                "Both style attributes should be found when other attributes are present"),
            () -> assertEquals(1, filterForAttribute(withMultiple, CLASS).size(),
                "The single class attribute should be found"));
    }

    @Test
    @DisplayName("Should recursively collect attributes whose value contains a given fragment")
    void shouldFilterAttributesWithGivenContentPart() {
        var withoutStyle = htmlStringToDocument(NESTED_DIV).getRootElement();
        var withStyle = htmlStringToDocument(NESTED_DIV_WITH_STYLE).getRootElement();
        var withMultiple = htmlStringToDocument(NESTED_DIV_WITH_MULTIPLE).getRootElement();

        assertAll("Attribute filtering by name and value fragment",
            () -> assertTrue(filterForAttributeContainingValue(withoutStyle, STYLE, FIRST_PART).isEmpty(),
                "No matching attributes should be found"),
            () -> assertEquals(1, filterForAttributeContainingValue(withStyle, STYLE, FIRST_PART).size(),
                "Only the attribute containing the fragment should be found"),
            () -> assertEquals(1, filterForAttributeContainingValue(withMultiple, STYLE, FIRST_PART).size(),
                "Only the matching style attribute should be found"),
            () -> assertEquals(0, filterForAttributeContainingValue(withMultiple, STYLE, "not.there").size(),
                "No attributes should match an absent fragment"));
    }

    @Test
    @DisplayName("Should fail with an IllegalArgumentException on malformed HTML")
    void shouldFailOnInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> htmlStringToDocument("<a></b>"),
            "Malformed HTML should raise an IllegalArgumentException");
    }
}
