package de.icw.cui.test.jsf.component;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.tools.property.PropertyReadWrite;

@PropertyReflectionConfig(skip = true)
@PropertyConfig(name = "delegate", propertyClass = PropertyMetadata.class, required = true,
        propertyReadWrite = PropertyReadWrite.WRITE_ONLY)
@PropertyConfig(name = "ignoreOnValueExpresssion", propertyClass = boolean.class)
@VerifyConstructor(of = { "delegate", "ignoreOnValueExpresssion" })
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class ComponentPropertyMetadataTest extends ValueObjectTest<ComponentPropertyMetadata>
        implements TypedGenerator<PropertyMetadata> {

    @Override
    public PropertyMetadata next() {
        return PropertyMetadataImpl.builder().name(Generators.letterStrings(2, 5).next())
                .propertyClass(String.class).generator(Generators.nonEmptyStrings())
                .build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }

}
