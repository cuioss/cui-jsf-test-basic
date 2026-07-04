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

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("HtmlTreeAsserts")
class HtmlTreeAssertsTest {

    private static final String SPAN = "span";

    private static final String SOME_NAME = "someName";

    private static final String SOME_TEXT_CONTENT = "Some text is here";

    private static final String NAME = "name";

    private static final String SOME_ID = "someId";

    private static final String ID = "id";

    private static final String DIV = "div";

    private static final String ROOT = "root";

    @Nested
    @DisplayName("when trees are equal")
    class EqualTrees {

        @Test
        @DisplayName("Should treat two simple div trees as equal")
        void shouldAssertSimpleDivCorrectly() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();

            assertDoesNotThrow(() -> {
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
            }, "Identical div trees should be considered equal");
        }

        @Test
        @DisplayName("Should treat two div trees with the same attribute as equal")
        void shouldAssertSimpleDivWithAttributeCorrectly() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            expected.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());
            actual.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());

            assertDoesNotThrow(() -> {
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
            }, "Div trees carrying the same attribute should be considered equal");
        }

        @Test
        @DisplayName("Should ignore attribute order when comparing div trees")
        void shouldAssertSimpleDivWithAttributeUnordered() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            var expectedAttribute = expected.getRootElement().getChild(DIV).getAttributes();
            expectedAttribute.add(createIdAttribute());
            expectedAttribute.add(createNameAttribute());
            var actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
            actualAttributes.add(createNameAttribute());
            actualAttributes.add(createIdAttribute());

            assertDoesNotThrow(() -> {
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
                HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
            }, "Attribute order should not affect equality");
        }

        @Test
        @DisplayName("Should not mutate the attribute order of the argument documents (ASSERT-1)")
        void shouldNotMutateAttributeOrderOfArguments() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            // Add attributes in non-sorted order (name before id)
            var expectedAttributes = expected.getRootElement().getChild(DIV).getAttributes();
            expectedAttributes.add(createNameAttribute());
            expectedAttributes.add(createIdAttribute());
            var actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
            actualAttributes.add(createNameAttribute());
            actualAttributes.add(createIdAttribute());

            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);

            // The live attribute list of the argument must retain its original order,
            // i.e. the comparison must sort copies, not the caller's documents.
            var afterAttributes = expected.getRootElement().getChild(DIV).getAttributes();
            assertEquals(NAME, afterAttributes.get(0).getName(),
                "The comparison must not sort (mutate) the caller's attribute list");
            assertEquals(ID, afterAttributes.get(1).getName(),
                "The comparison must not sort (mutate) the caller's attribute list");
        }

        @Test
        @DisplayName("Should treat trees with equal text content as equal")
        void shouldAssertWithTextNode() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
            actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);

            assertDoesNotThrow(() -> HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual),
                "Trees with equal text content should be considered equal");
        }
    }

    @Nested
    @DisplayName("when trees differ")
    class DifferingTrees {

        @Test
        @DisplayName("Should fail when attribute values differ")
        void shouldFailSimpleDivWithDifferentAttributeValues() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            expected.getRootElement().getChild(DIV).getAttributes().add(new Attribute(ID, SOME_ID));
            actual.getRootElement().getChild(DIV).getAttributes().add(new Attribute(ID, SOME_NAME));

            assertThrows(AssertionError.class, () -> HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual),
                "Differing attribute values should trigger an assertion error");
        }

        @Test
        @DisplayName("Should fail when element types differ")
        void shouldFailSimpleMixedElements() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            expected.getRootElement().getChildren().add(createDivElement());
            actual.getRootElement().getChildren().add(createSpanElement());

            assertThrows(AssertionError.class, () -> HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual),
                "Differing element types should trigger an assertion error");
        }

        @Test
        @DisplayName("Should fail when child element order differs")
        void shouldFailWithElementsUnordered() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            var expectedChildren = expected.getRootElement().getChild(DIV).getChildren();
            expectedChildren.add(createDivElement());
            expectedChildren.add(createSpanElement());
            var actualChildren = actual.getRootElement().getChild(DIV).getChildren();
            actualChildren.add(createSpanElement());
            actualChildren.add(createDivElement());

            assertThrows(AssertionError.class, () -> HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual),
                "Differing child element order should trigger an assertion error");
        }

        @Test
        @DisplayName("Should fail when text content differs")
        void shouldFailWithUnequalTextNode() {
            var expected = createDocumentWithDivChild();
            var actual = createDocumentWithDivChild();
            expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
            actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT + "hey");

            assertThrows(AssertionError.class, () -> HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual),
                "Differing text content should trigger an assertion error");
        }
    }

    private static Document createDocumentWithRoot() {
        var root = new Element(ROOT);
        return new Document(root);
    }

    private static Document createDocumentWithDivChild() {
        var document = createDocumentWithRoot();
        document.getRootElement().getChildren().add(createDivElement());
        return document;
    }

    private static Element createDivElement() {
        return new Element(DIV);
    }

    private static Element createSpanElement() {
        return new Element(SPAN);
    }

    private static Attribute createIdAttribute() {
        return new Attribute(ID, SOME_ID);
    }

    private static Attribute createNameAttribute() {
        return new Attribute(NAME, SOME_NAME);
    }
}
