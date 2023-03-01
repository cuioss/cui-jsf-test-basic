package de.icw.cui.test.jsf.support.componentproperty;

import de.icw.cui.test.jsf.config.component.VerifyComponentProperties;
import lombok.Data;

@SuppressWarnings("javadoc")
@VerifyComponentProperties
@Data
public class WithOneParameter {

    private String someProperty;
}
