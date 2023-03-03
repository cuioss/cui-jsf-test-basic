package de.cuioss.test.jsf.mocks;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VerifyBeanProperty(exclude = { "invokedParams", "parmetersProvided" })
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class CuiMockMethodExpressionTest extends ValueObjectTest<CuiMockMethodExpression> {

}
