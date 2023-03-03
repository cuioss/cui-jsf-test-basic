package de.cuioss.test.jsf.support.componentproperty;

import java.util.List;

import javax.faces.component.html.HtmlInputText;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

@SuppressWarnings("javadoc")
@VerifyComponentProperties(of = { "stringList" })
public class ComponentWithCollection extends HtmlInputText {

    public static final String STRING_LIST_KEY = "stringList";

    public void setStringList(final List<String> value) {
        getStateHelper().put(STRING_LIST_KEY, value);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList() {
        return (List<String>) getStateHelper().eval(STRING_LIST_KEY);
    }
}
