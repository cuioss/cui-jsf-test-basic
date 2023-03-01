package de.icw.cui.test.jsf.support.componentproperty;

import javax.faces.component.html.HtmlInputText;

import de.icw.cui.test.jsf.config.component.VerifyComponentProperties;

@SuppressWarnings("javadoc")
@VerifyComponentProperties(of = { "rendered", "styleClass", "style" })
public class MultiValuedComponent extends HtmlInputText {

}
