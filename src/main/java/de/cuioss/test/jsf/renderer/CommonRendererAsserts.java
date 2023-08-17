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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.Serializable;

import javax.faces.component.UIComponent;

import org.jdom2.Element;

import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.tools.property.PropertyUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum define the commonly used attributes to be tested.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public enum CommonRendererAsserts implements RendererAttributeAssert {

    /** Checks the attribute 'id', see {@link UIComponent#getId()} */
    ID("id", "traceId"),
    /**
     * Checks the attribute 'rendered', see {@link UIComponent#isRendered()}. Due to
     * its nature this test must not work all the time correctly and should
     * therefore considered as candidate for vetoing. The actual assert checks
     * whether the given component is {@code null} or does not contain any children.
     */
    RENDERED("rendered", Boolean.FALSE) {

        @Override
        public void assertAttributeSet(final Element element) {
            if (null == element) {
                return;
            }
            if (!element.getChildren().isEmpty()) {
                fail("Children found, although the rendered attribute is set to 'false'. This may be a tricky one, depending on your desired output");
            }
        }
    },
    /** Checks the attribute 'style' */
    STYLE("style", "traceStyle"),
    /** Checks the attribute 'style' */
    STYLE_CLASS("styleClass", "traceStyleClass") {

        @Override
        public void assertAttributeSet(final Element element) {
            var found = DomUtils.filterForAttributeContainingValue(element, "class",
                    getAttributeTraceValue().toString());
            assertFalse(found.isEmpty(), "The expected attribute with name='class' and traceValue="
                    + getAttributeTraceValue() + " was not found in the resulting dom-tree.");
        }
    },
    /**
     * Checks the passthrough-attributes.
     */
    PASSTHROUGH("data-passthrough-test", "passthroughTraceValue") {

        @Override
        public void applyAttribute(final UIComponent component) {
            component.getPassThroughAttributes(true).put(getAttributeName(), getAttributeTraceValue());
        }
    };

    @Getter
    private final String attributeName;

    @Getter
    private final Serializable attributeTraceValue;

    @Override
    public void applyAttribute(final UIComponent component) {
        PropertyUtil.writeProperty(component, attributeName, getAttributeTraceValue());
    }

    @Override
    public void assertAttributeSet(final Element element) {
        var found = DomUtils.filterForAttributeContainingValue(element, getAttributeName(),
                getAttributeTraceValue().toString());
        assertFalse(found.isEmpty(), "The expected attribute with name=" + getAttributeName() + " and traceValue="
                + getAttributeTraceValue() + " was not found in the resulting dom-tree.");
    }
}
