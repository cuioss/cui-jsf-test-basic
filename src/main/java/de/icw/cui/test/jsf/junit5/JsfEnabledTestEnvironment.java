package de.icw.cui.test.jsf.junit5;

import de.icw.cui.test.jsf.util.JsfEnvironmentConsumer;
import de.icw.cui.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for creating unit-tests with a wired / configured JSF-environment being annotated with
 * {@link EnableJsfEnvironment} and implementing {@link JsfEnvironmentConsumer}
 *
 * @author Oliver Wolff
 *
 */
@EnableJsfEnvironment(useIdentityResourceBundle = true)
public class JsfEnabledTestEnvironment implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;
}
