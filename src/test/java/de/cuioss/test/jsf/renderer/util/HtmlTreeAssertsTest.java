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
package de.cuioss.test.jsf.renderer.util;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HtmlTreeAssertsTest {

    private static final String SPAN = "span";

    private static final String SOME_NAME = "someName";

    private static final String SOME_TEXT_CONTENT = "Some text is here";

    private static final String NAME = "name";

    private static final String SOME_ID = "someId";

    private static final String ID = "id";

    private static final String DIV = "div";

    public static final String ROOT = "root";

    @Test
    void shouldAssertSimpleDivCorrectly() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldAssertSimpleDivWithAttributeCorrectly() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());
        actual.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldAssertSimpleDivWithAttributeUnordered() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        var expectedAttribute = expected.getRootElement().getChild(DIV).getAttributes();
        expectedAttribute.add(createIdAttribute());
        expectedAttribute.add(createNameAttribute());
        var actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
        actualAttributes.add(createNameAttribute());
        actualAttributes.add(createIdAttribute());
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldFailSimpleDivWithDifferentAttributeValues() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        var expectedAttribute = expected.getRootElement().getChild(DIV).getAttributes();
        expectedAttribute.add(new Attribute(ID, SOME_ID));
        var actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
        actualAttributes.add(new Attribute(ID, SOME_NAME));
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldFailSimpleMixedElements() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        expected.getRootElement().getChildren().add(createDivElement());
        actual.getRootElement().getChildren().add(createSpanElement());
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldFailWithElementsUnordered() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        var expectedChildren = expected.getRootElement().getChild(DIV).getChildren();
        expectedChildren.add(createDivElement());
        expectedChildren.add(createSpanElement());
        var actualChildren = actual.getRootElement().getChild(DIV).getChildren();
        actualChildren.add(createSpanElement());
        actualChildren.add(createDivElement());
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldAssertWithTextNode() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldFailWithUnequalTextNode() {
        var expected = createDocumentWithDivChild();
        var actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT + "hey");
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
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
