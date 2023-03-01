package de.icw.cui.test.jsf.renderer.util;

import static de.icw.cui.test.jsf.renderer.util.DomUtils.filterForAttribute;
import static de.icw.cui.test.jsf.renderer.util.DomUtils.filterForAttributeContainingValue;
import static de.icw.cui.test.jsf.renderer.util.DomUtils.htmlStringToDocument;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jdom2.Document;
import org.junit.jupiter.api.Test;

class DomUtilsTest {

    private static final String FIRST_PART = "first";

    private static final String STYLE = "style";

    private static final String CLASS = "class";

    public static final String NESTED_DIV = "<div><div /></div>";

    public static final String NESTED_DIV_WITH_STYLE = "<div style=\"first\"><div style=\"second\"/></div>";

    public static final String NESTED_DIV_WITH_MULITPLE =
        "<div style=\"first\" class=\"firstClass\"><div style=\"second\"/></div>";

    @Test
    void shouldCreateDomFromHml() {
        Document doc = htmlStringToDocument(NESTED_DIV);
        assertNotNull(doc);
        assertEquals("root", doc.getRootElement().getName());
        assertEquals(1, doc.getRootElement().getChildren().size());
        assertEquals("div", doc.getRootElement().getChildren().iterator().next().getName());
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
        assertThrows(IllegalArgumentException.class, () -> {
            htmlStringToDocument("<a></b>");
        });
    }
}
