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

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.tools.property.PropertyUtil;
import jakarta.faces.component.html.HtmlInputText;
import org.jdom2.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@DisplayName("CommonRendererAsserts")
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
    @DisplayName("Should detect the id attribute on the rendered element")
    void shouldHandleIdAttribute() {
        verifyContract(CommonRendererAsserts.ID, NESTED_DIV_WITH_ID_ATTRIBUTE,
            CommonRendererAsserts.ID.getAttributeTraceValue());
    }

    @Test
    @DisplayName("Should detect the style attribute on the rendered element")
    void shouldHandleStyleAttribute() {
        verifyContract(CommonRendererAsserts.STYLE, NESTED_DIV_WITH_STYLE_ATTRIBUTE,
            CommonRendererAsserts.STYLE.getAttributeTraceValue());
    }

    @Test
    @DisplayName("Should detect the style-class attribute on the rendered element")
    void shouldHandleStyleClassAttribute() {
        verifyContract(CommonRendererAsserts.STYLE_CLASS, NESTED_DIV_WITH_STYLE_CLASS_ATTRIBUTE,
            CommonRendererAsserts.STYLE_CLASS.getAttributeTraceValue());
    }

    @Test
    @DisplayName("Should detect the rendered attribute on the rendered element")
    void shouldHandleRenderedAttribute() {
        verifyContract(CommonRendererAsserts.RENDERED, EMPTY_ELEMENT,
            CommonRendererAsserts.RENDERED.getAttributeTraceValue());
    }

    @Test
    @DisplayName("Should apply the passthrough attribute to the component")
    void componentContainsPassThroughAttribute() {
        var component = new HtmlInputText();

        CommonRendererAsserts.PASSTHROUGH.applyAttribute(component);

        assertEquals(CommonRendererAsserts.PASSTHROUGH.getAttributeTraceValue(),
            component.getPassThroughAttributes().get(CommonRendererAsserts.PASSTHROUGH.getAttributeName()),
            "Passthrough attribute should be present with the trace value");
    }

    @Test
    @DisplayName("Should fail when the passthrough attribute is missing from the dom tree")
    void detectMissingPassThroughAttribute() {
        var docRoot = DomUtils.htmlStringToDocument(NESTED_DIV).getRootElement();

        var exception = assertThrows(AssertionError.class,
            () -> CommonRendererAsserts.PASSTHROUGH.assertAttributeSet(docRoot),
            "Missing passthrough attribute should trigger an assertion error");

        assertEquals("""
            The expected attribute with name=data-passthrough-test and traceValue=passthroughTraceValue \
            was not found in the resulting dom-tree. ==> expected: <false> but was: <true>""",
            exception.getMessage(), "Assertion error should describe the missing attribute");
    }

    @Test
    @DisplayName("Should succeed when the passthrough attribute is present in the dom tree")
    void detectExistingPassThroughAttribute() {
        var nestedDiv = DomUtils.htmlStringToDocument(NESTED_DIV_WITH_PT_ATTRIBUTE).getRootElement();

        assertDoesNotThrow(() -> CommonRendererAsserts.PASSTHROUGH.assertAttributeSet(nestedDiv),
            "Present passthrough attribute should not trigger an assertion error");
    }

    private static void verifyContract(final RendererAttributeAssert attributeAssert, final String positiveHtml,
        final Serializable traceAttributeValue) {
        var component = new HtmlInputText();
        attributeAssert.applyAttribute(component);
        assertEquals(traceAttributeValue, PropertyUtil.readProperty(component, attributeAssert.getAttributeName()),
            "Applied attribute should be readable as the trace value");
        var missing = DomUtils.htmlStringToDocument(NESTED_DIV).getRootElement();
        assertThrows(AssertionError.class, () -> attributeAssert.assertAttributeSet(missing),
            "Assert should fail when the attribute is absent");
        attributeAssert.assertAttributeSet(DomUtils.htmlStringToDocument(positiveHtml).getRootElement());
    }
}
