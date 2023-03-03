package de.cuioss.test.jsf.support.componentproperty;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;
import lombok.Data;

@SuppressWarnings("javadoc")
@VerifyComponentProperties
@Data
public class WithOneParameter {

    private String someProperty;
}
