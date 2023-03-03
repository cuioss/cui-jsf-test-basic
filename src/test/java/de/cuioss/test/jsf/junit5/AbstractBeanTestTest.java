package de.cuioss.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.support.beans.MediumComplexityBean;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;

@PropertyReflectionConfig(
        defaultValued = { MediumComplexityBean.STRING_WITH_DEFAULT_VALUE })
@ObjectTestConfig(
        equalsAndHashCodeExclude = MediumComplexityBean.ATTRIBUTE_NO_OBJECT_IDENTITY_STRING)
class AbstractBeanTestTest extends AbstractBeanTest<MediumComplexityBean> {

    @Test
    void shouldInstantiate() {
        assertNotNull(anyBean());
    }
}
