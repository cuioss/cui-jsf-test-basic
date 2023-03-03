package de.cuioss.test.jsf.support.componentproperty;

import javax.faces.component.html.HtmlInputText;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

@SuppressWarnings("javadoc")
@VerifyComponentProperties(of = { "rendered", "styleClass", "style" })
public class MultiValuedComponent extends HtmlInputText {

}
