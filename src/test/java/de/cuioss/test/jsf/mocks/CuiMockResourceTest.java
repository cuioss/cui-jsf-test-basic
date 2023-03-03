package de.cuioss.test.jsf.mocks;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@VerifyBeanProperty
@VetoObjectTestContract({ ObjectTestContracts.SERIALIZABLE, ObjectTestContracts.EQUALS_AND_HASHCODE })
class CuiMockResourceTest extends ValueObjectTest<CuiMockResource> {

}
