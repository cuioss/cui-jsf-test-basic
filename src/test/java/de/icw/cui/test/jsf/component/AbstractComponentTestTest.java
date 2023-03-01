package de.icw.cui.test.jsf.component;

import javax.faces.component.html.HtmlInputText;

import de.icw.cui.test.jsf.config.component.VerifyComponentProperties;

@VerifyComponentProperties(of = { "rendered", "styleClass", "style" })
class AbstractComponentTestTest extends AbstractComponentTest<HtmlInputText> {

}
