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

@PropertyReflectionConfig(skip = true)
@PropertyConfig(name = "delegate", propertyClass = PropertyMetadata.class, required = true, propertyReadWrite = PropertyReadWrite.WRITE_ONLY)
@PropertyConfig(name = "ignoreOnValueExpresssion", propertyClass = boolean.class)
@VerifyConstructor(of = {"delegate", "ignoreOnValueExpresssion"})
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

}
