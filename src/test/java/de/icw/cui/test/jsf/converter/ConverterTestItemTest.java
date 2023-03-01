package de.icw.cui.test.jsf.converter;

import de.icw.cui.test.jsf.generator.MessageSeverityGenerator;
import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;
import io.cui.test.valueobjects.api.generator.PropertyGenerator;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;

@VerifyBeanProperty
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
@PropertyGenerator(MessageSeverityGenerator.class)
class ConverterTestItemTest extends ValueObjectTest<ConverterTestItem<String>> {

}
