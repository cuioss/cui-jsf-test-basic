package de.cuioss.test.jsf.converter;

import de.cuioss.test.jsf.generator.MessageSeverityGenerator;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@VerifyBeanProperty
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
@PropertyGenerator(MessageSeverityGenerator.class)
class ConverterTestItemTest extends ValueObjectTest<ConverterTestItem<String>> {

}
