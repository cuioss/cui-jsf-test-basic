package de.icw.cui.test.jsf.component;

import java.util.List;

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
        List<ComponentPropertyMetadata> properties =
            ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
                    new MultiValuedComponent());

        CallbackAwareInstantiator<MultiValuedComponent> instantiator =
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
