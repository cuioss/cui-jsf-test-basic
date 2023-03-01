package de.icw.cui.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.config.JsfTestConfiguration;
import de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration;

/**
 * Tests whether the annotation {@link JsfTestConfiguration} will be used from
 * JsfSetupExtensionTest
 *
 */
class NestedJsfSetupExtensionTest extends JsfSetupExtensionTest {

    @Test
    void shouldProvideBasicInformation() {
        assertEquals(BasicApplicationConfiguration.FIREFOX,
                getExternalContext().getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }
}
