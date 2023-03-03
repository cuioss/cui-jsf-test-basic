package de.cuioss.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsfEnabledTestEnvironmentTest extends JsfEnabledTestEnvironment {

    @Test
    void shouldSetupJsfEnvironment() {
        assertNotNull(getFacesContext());
    }

}
