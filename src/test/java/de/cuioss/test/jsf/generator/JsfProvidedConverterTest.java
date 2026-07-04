/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.generator;

import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.application.Application;
import jakarta.faces.convert.EnumConverter;
import jakarta.faces.convert.IntegerConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableJsfEnvironment
@DisplayName("JsfProvidedConverter Tests")
class JsfProvidedConverterTest {

    @Test
    @DisplayName("Should resolve every JSF-provided converter by its converter id")
    void shouldRegisterConverter(Application application, ComponentConfigDecorator componentConfig) {
        componentConfig.registerConverter(IntegerConverter.class, IntegerConverter.CONVERTER_ID);
        componentConfig.registerConverter(EnumConverter.class, EnumConverter.CONVERTER_ID);

        for (ConverterDescriptor descriptor : JsfProvidedConverter.JSF_CONVERTER) {
            assertEquals(descriptor.getConverterClass(),
                application.createConverter(descriptor.getConverterId()).getClass(),
                "Converter id " + descriptor.getConverterId() + " should resolve to "
                    + descriptor.getConverterClass().getName());
        }
    }

    @Test
    @DisplayName("Should generate non-null values for all converter-specific generators")
    void shouldGenerateConverterSpecificTypes() {
        assertNotNull(new JsfProvidedConverter().next(), "Hybrid generator should produce a converter class");
        assertNotNull(JsfProvidedConverter.CONVERTER_CLASS_GENERATOR.next(),
            "Converter-class generator should produce a descriptor");
        assertNotNull(JsfProvidedConverter.CONVERTER_ID_GENERATOR.next(),
            "Converter-id generator should produce an id");
        assertNotNull(JsfProvidedConverter.TARGET_TYPE_GENERATOR.next(),
            "Target-type generator should produce a class");
    }

}
