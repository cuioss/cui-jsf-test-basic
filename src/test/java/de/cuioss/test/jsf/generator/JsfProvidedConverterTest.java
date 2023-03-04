package de.cuioss.test.jsf.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.util.ConfigurableFacesTest;

class JsfProvidedConverterTest extends ConfigurableFacesTest {

    @Test
    void shouldRegisterConverter() {
        for (ConverterDescriptor descriptor : JsfProvidedConverter.JSF_CONVERTER) {
            assertEquals(descriptor.getConverterClass(),
                    getApplication().createConverter(descriptor.getConverterId()).getClass());
        }
    }

    @Test
    void shouldGenerateConverterSpecificTypes() {
        assertNotNull(new JsfProvidedConverter().next());
        assertNotNull(JsfProvidedConverter.CONVERTER_CLASS_GERNERATOR.next());
        assertNotNull(JsfProvidedConverter.CONVERTER_ID_GENERATOR.next());
        assertNotNull(JsfProvidedConverter.TARGET_TYPE_GENERATOR.next());
    }

}
