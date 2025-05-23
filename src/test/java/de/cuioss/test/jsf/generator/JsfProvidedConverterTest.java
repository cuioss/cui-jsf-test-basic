/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.generator;

import de.cuioss.test.jsf.util.ConfigurableFacesTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
