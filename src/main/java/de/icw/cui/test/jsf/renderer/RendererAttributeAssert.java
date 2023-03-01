package de.icw.cui.test.jsf.renderer;

import javax.faces.component.UIComponent;

import org.jdom2.Element;

/**
 * This interface defines the testing of implicit single attribute contracts.
 *
 * @author Oliver Wolff
 */
public interface RendererAttributeAssert {

    /**
     * @return the name of the attribute to be checked
     */
    String getAttributeName();

    /**
     * Sets the attribute to be tested into the given component
     *
     * @param component where the attribute is to be applied to,
     */
    void applyAttribute(UIComponent component);

    /**
     * Asserts in the given render-result that the attribute was set properly
     *
     * @param element to be checked
     */
    void assertAttributeSet(Element element);
}
