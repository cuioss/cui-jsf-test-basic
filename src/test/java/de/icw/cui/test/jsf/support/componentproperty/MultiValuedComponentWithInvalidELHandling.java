package de.icw.cui.test.jsf.support.componentproperty;

import de.icw.cui.test.jsf.config.component.VerifyComponentProperties;

@SuppressWarnings("javadoc")
@VerifyComponentProperties(of = { "someProperty" })
public class MultiValuedComponentWithInvalidELHandling extends MultiValuedComponent {

    private static final String INVALID_VE = "invalidVE";

    public void setSomeProperty(final String someProperty) {
        getStateHelper().put(INVALID_VE, someProperty);
    }

    public String getSomeProperty() {
        return (String) getStateHelper().eval(INVALID_VE);
    }
}
