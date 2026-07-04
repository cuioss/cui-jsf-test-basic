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
package de.cuioss.test.jsf.component;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PropertyReflectionConfig(skip = true)
@PropertyConfig(name = "delegate", propertyClass = PropertyMetadata.class, required = true, propertyReadWrite = PropertyReadWrite.WRITE_ONLY)
@PropertyConfig(name = "ignoreOnValueExpression", propertyClass = boolean.class)
@VerifyConstructor(of = {"delegate", "ignoreOnValueExpression"})
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class ComponentPropertyMetadataTest extends ValueObjectTest<ComponentPropertyMetadata>
    implements TypedGenerator<PropertyMetadata> {

    @Override
    public PropertyMetadata next() {
        return PropertyMetadataImpl.builder().name(Generators.letterStrings(2, 5).next()).propertyClass(String.class)
            .generator(Generators.nonEmptyStrings()).build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }

    @Test
    @SuppressWarnings("deprecation")
    void deprecatedGetterMatchesCorrectlySpelledGetter() {
        var metadata = new ComponentPropertyMetadata(next(), true);

        assertTrue(metadata.isIgnoreOnValueExpression(), "The correctly-spelled getter must return the value");
        assertEquals(metadata.isIgnoreOnValueExpression(), metadata.isIgnoreOnValueExpresssion(),
            "The deprecated getter must match the correctly-spelled one (API-1)");
    }

}
