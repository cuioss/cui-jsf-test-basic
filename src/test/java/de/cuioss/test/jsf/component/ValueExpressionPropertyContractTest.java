package de.cuioss.test.jsf.component;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.support.componentproperty.MultiValuedComponent;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;

class ValueExpressionPropertyContractTest extends AbstractComponentTest<MultiValuedComponent> {

    @Test
    void shouldTestGoodCase() {
        var properties = ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
                new MultiValuedComponent());

        var instantiator = new CallbackAwareInstantiator<>(new BeanInstantiator<>(
                new DefaultInstantiator<>(MultiValuedComponent.class), new RuntimeProperties(properties)), this);

        ValueExpressionPropertyContract<MultiValuedComponent> contract;
        contract = new ValueExpressionPropertyContract<>(instantiator, properties, getFacesContext());

        contract.assertContract();
    }
}
