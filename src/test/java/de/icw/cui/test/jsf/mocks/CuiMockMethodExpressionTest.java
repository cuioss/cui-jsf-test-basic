package de.icw.cui.test.jsf.mocks;

import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VerifyBeanProperty(exclude = { "invokedParams", "parmetersProvided" })
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class CuiMockMethodExpressionTest extends ValueObjectTest<CuiMockMethodExpression> {

}
