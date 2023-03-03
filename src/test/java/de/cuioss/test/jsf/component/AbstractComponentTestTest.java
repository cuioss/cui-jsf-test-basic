package de.cuioss.test.jsf.component;

import javax.faces.component.html.HtmlInputText;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

@VerifyComponentProperties(of = { "rendered", "styleClass", "style" })
class AbstractComponentTestTest extends AbstractComponentTest<HtmlInputText> {

}
