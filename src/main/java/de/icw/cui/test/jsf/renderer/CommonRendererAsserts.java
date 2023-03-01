package de.icw.cui.test.jsf.renderer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;

import org.jdom2.Attribute;
import org.jdom2.Element;

import de.icw.cui.test.jsf.renderer.util.DomUtils;
import io.cui.tools.property.PropertyUtil;
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
     * Checks the attribute 'rendered', see {@link UIComponent#isRendered()}. Due to its nature this
     * test must not work all the time correctly and should therefore considered as candidate for
     * vetoing. The actual assert checks whether the given component is {@code null} or does not
     * contain any children.
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
            List<Attribute> found =
                    DomUtils.filterForAttributeContainingValue(element, "class", getAttributeTraceValue().toString());
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
        List<Attribute> found = DomUtils.filterForAttributeContainingValue(element, getAttributeName(),
                getAttributeTraceValue().toString());
        assertFalse(found.isEmpty(), "The expected attribute with name=" + getAttributeName() + " and traceValue="
                + getAttributeTraceValue() + " was not found in the resulting dom-tree.");
    }
}
