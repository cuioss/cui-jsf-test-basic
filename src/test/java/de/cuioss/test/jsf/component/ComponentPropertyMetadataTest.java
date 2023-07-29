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
@VerifyConstructor(of = { "delegate", "ignoreOnValueExpresssion" })
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
