package de.icw.cui.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.context.FacesContext;

import org.apache.myfaces.test.mock.MockFacesContext22;
import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.config.JsfTestConfiguration;
import de.icw.cui.test.jsf.defaults.BasicApplicationConfiguration;

@EnableJsfEnvironment
@JsfTestConfiguration(BasicApplicationConfiguration.class)
class JsfSetupExtensionTestWOEnvironmentConsumer {

    @Test
    void shouldBootstrapJsf() {
        assertNotNull(FacesContext.getCurrentInstance());
        assertEquals(MockFacesContext22.class, FacesContext.getCurrentInstance().getClass());
    }

}
