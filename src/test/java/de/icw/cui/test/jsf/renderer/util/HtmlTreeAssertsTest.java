package de.icw.cui.test.jsf.renderer.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

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
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldAssertSimpleDivWithAttributeCorrectly() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());
        actual.getRootElement().getChild(DIV).getAttributes().add(createIdAttribute());
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldAssertSimpleDivWithAttributeUnordered() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        List<Attribute> expectedAttribute = expected.getRootElement().getChild(DIV).getAttributes();
        expectedAttribute.add(createIdAttribute());
        expectedAttribute.add(createNameAttribute());
        List<Attribute> actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
        actualAttributes.add(createNameAttribute());
        actualAttributes.add(createIdAttribute());
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, expected);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldFailSimpleDivWithDifferentAttributeValues() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        List<Attribute> expectedAttribute = expected.getRootElement().getChild(DIV).getAttributes();
        expectedAttribute.add(new Attribute(ID, SOME_ID));
        List<Attribute> actualAttributes = actual.getRootElement().getChild(DIV).getAttributes();
        actualAttributes.add(new Attribute(ID, SOME_NAME));
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldFailSimpleMixedElements() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        expected.getRootElement().getChildren().add(createDivElement());
        actual.getRootElement().getChildren().add(createSpanElement());
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldFailWithElementsUnordered() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        List<Element> expectedChildren = expected.getRootElement().getChild(DIV).getChildren();
        expectedChildren.add(createDivElement());
        expectedChildren.add(createSpanElement());
        List<Element> actualChildren = actual.getRootElement().getChild(DIV).getChildren();
        actualChildren.add(createSpanElement());
        actualChildren.add(createDivElement());
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    @Test
    void shouldAssertWithTextNode() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
    }

    @Test
    void shouldFailWithUnequalTextNode() {
        Document expected = createDocumentWithDivChild();
        Document actual = createDocumentWithDivChild();
        expected.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT);
        actual.getRootElement().getChild(DIV).addContent(SOME_TEXT_CONTENT + "hey");
        assertThrows(AssertionError.class, () -> {
            HtmlTreeAsserts.assertHtmlTreeEquals(expected, actual);
        });
    }

    private static Document createDocumentWithRoot() {
        Element root = new Element(ROOT);
        return new Document(root);
    }

    private static Document createDocumentWithDivChild() {
        Document document = createDocumentWithRoot();
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
