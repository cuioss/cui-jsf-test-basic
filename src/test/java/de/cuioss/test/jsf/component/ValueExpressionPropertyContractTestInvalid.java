package de.cuioss.test.jsf.component;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.AbstractPropertyAwareFacesTest;
import de.cuioss.test.jsf.support.componentproperty.MultiValuedComponentWithInvalidELHandling;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;

class ValueExpressionPropertyContractTestInvalid
        extends AbstractPropertyAwareFacesTest<MultiValuedComponentWithInvalidELHandling> {

    @Test
    void shouldTestGoodCase() {
        var properties =
            ComponentTestHelper.filterPropertyMetadata(
                    MultiValuedComponentWithInvalidELHandling.class,
                    new MultiValuedComponentWithInvalidELHandling());

        var instantiator =
            new CallbackAwareInstantiator<>(
                    new BeanInstantiator<>(new DefaultInstantiator<>(MultiValuedComponentWithInvalidELHandling.class),
                            new RuntimeProperties(properties)),
                    this);

        ValueExpressionPropertyContract<MultiValuedComponentWithInvalidELHandling> contract;
        contract =
            new ValueExpressionPropertyContract<>(instantiator, properties, getFacesContext());

        assertThrows(AssertionError.class, () -> {
            contract.assertContract();
        });
    }
}
