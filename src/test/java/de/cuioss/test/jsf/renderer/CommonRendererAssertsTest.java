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

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.tools.property.PropertyUtil;
import jakarta.faces.component.html.HtmlInputText;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
class CommonRendererAssertsTest {

    public static final String NESTED_DIV = "<div><div /></div>";

    public static final String NESTED_DIV_WITH_ID_ATTRIBUTE = "<div id=\""
        + CommonRendererAsserts.ID.getAttributeTraceValue() + "\"><div /></div>";

    public static final String NESTED_DIV_WITH_STYLE_ATTRIBUTE = "<div style=\""
        + CommonRendererAsserts.STYLE.getAttributeTraceValue() + "\"><div /></div>";

    public static final String NESTED_DIV_WITH_STYLE_CLASS_ATTRIBUTE = "<div class=\""
        + CommonRendererAsserts.STYLE_CLASS.getAttributeTraceValue() + "\"><div /></div>";

    public static final String NESTED_DIV_WITH_PT_ATTRIBUTE = "<div data-passthrough-test=\""
        + CommonRendererAsserts.PASSTHROUGH.getAttributeTraceValue() + "\"><div /></div>";

    public static final String EMPTY_ELEMENT = "";

    @Test
    void shouldHandleIdAttribute() {
        verifyContract(CommonRendererAsserts.ID, NESTED_DIV_WITH_ID_ATTRIBUTE,
            CommonRendererAsserts.ID.getAttributeTraceValue());
    }

    @Test
    void shouldHandleStyleAttribute() {
        verifyContract(CommonRendererAsserts.STYLE, NESTED_DIV_WITH_STYLE_ATTRIBUTE,
            CommonRendererAsserts.STYLE.getAttributeTraceValue());
    }

    @Test
    void shouldHandleStyleClassAttribute() {
        verifyContract(CommonRendererAsserts.STYLE_CLASS, NESTED_DIV_WITH_STYLE_CLASS_ATTRIBUTE,
            CommonRendererAsserts.STYLE_CLASS.getAttributeTraceValue());
    }

    @Test
    void shouldHandleRenderedAttribute() {
        verifyContract(CommonRendererAsserts.RENDERED, EMPTY_ELEMENT,
            CommonRendererAsserts.RENDERED.getAttributeTraceValue());
    }

    @Test
    void componentContainsPassThroughAttribute() {
        HtmlInputText component = new HtmlInputText();

        CommonRendererAsserts.PASSTHROUGH.applyAttribute(component);

        assertEquals(CommonRendererAsserts.PASSTHROUGH.getAttributeTraceValue(),
            component.getPassThroughAttributes()
                .get(CommonRendererAsserts.PASSTHROUGH.getAttributeName()));
    }

    @Test
    void detectMissingPassThroughAttribute() {
        Element docRoot = DomUtils.htmlStringToDocument(NESTED_DIV).getRootElement();

        AssertionError ex = assertThrows(AssertionError.class, () ->
            CommonRendererAsserts.PASSTHROUGH.assertAttributeSet(docRoot));

        assertEquals("The expected attribute with name=data-passthrough-test and traceValue=passthroughTraceValue " +
            "was not found in the resulting dom-tree. ==> expected: <false> but was: <true>", ex.getMessage());
    }

    @Test
    void detectExistingPassThroughAttribute() {
        Element nestedDiv = DomUtils.htmlStringToDocument(NESTED_DIV_WITH_PT_ATTRIBUTE).getRootElement();

        assertDoesNotThrow(() -> CommonRendererAsserts.PASSTHROUGH.assertAttributeSet(nestedDiv));
    }

    private static void verifyContract(final RendererAttributeAssert attributeAssert, final String positiveHtml,
        final Serializable traceAttributeValue) {
        var component = new HtmlInputText();
        attributeAssert.applyAttribute(component);
        assertEquals(traceAttributeValue, PropertyUtil.readProperty(component, attributeAssert.getAttributeName()));
        // Should detect missing attribute
        var result = DomUtils.htmlStringToDocument(NESTED_DIV).getRootElement();
        assertThrows(AssertionError.class, () -> attributeAssert.assertAttributeSet(result));
        attributeAssert.assertAttributeSet(DomUtils.htmlStringToDocument(positiveHtml).getRootElement());
    }
}
