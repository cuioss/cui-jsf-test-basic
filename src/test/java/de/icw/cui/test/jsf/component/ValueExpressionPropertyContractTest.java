package de.icw.cui.test.jsf.component;

import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.support.componentproperty.MultiValuedComponent;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;

class ValueExpressionPropertyContractTest
        extends AbstractComponentTest<MultiValuedComponent> {

    @Test
    void shouldTestGoodCase() {
        var properties =
            ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
                    new MultiValuedComponent());

        var instantiator =
            new CallbackAwareInstantiator<>(
                    new BeanInstantiator<>(new DefaultInstantiator<>(MultiValuedComponent.class),
                            new RuntimeProperties(properties)),
                    this);

        ValueExpressionPropertyContract<MultiValuedComponent> contract;
        contract =
            new ValueExpressionPropertyContract<>(instantiator, properties, getFacesContext());

        contract.assertContract();
    }
}
