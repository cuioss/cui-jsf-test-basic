package de.icw.cui.test.jsf.component;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.junit5.AbstractPropertyAwareFacesTest;
import de.icw.cui.test.jsf.support.componentproperty.MultiValuedComponentWithInvalidELHandling;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;

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
