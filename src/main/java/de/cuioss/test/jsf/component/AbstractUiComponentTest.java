package de.cuioss.test.jsf.component;

import javax.faces.component.UIComponent;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

/**
 * Extension to {@link AbstractComponentTest} that implicitly tests a number of standard attributes
 * like 'style', 'styleClass', 'rendered'
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested, at least an {@link UIComponent}
 */
@VerifyComponentProperties(of = { "style", "styleClass", "rendered" })
public abstract class AbstractUiComponentTest<T extends UIComponent>
        extends AbstractComponentTest<T> {

}
