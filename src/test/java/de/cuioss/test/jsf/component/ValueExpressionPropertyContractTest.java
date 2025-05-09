/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.component;

import de.cuioss.test.jsf.support.componentproperty.MultiValuedComponent;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

class ValueExpressionPropertyContractTest extends AbstractComponentTest<MultiValuedComponent> {

    @Test
    void shouldTestGoodCase(FacesContext facesContext) {
        var properties = ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
            new MultiValuedComponent());

        var instantiator = new CallbackAwareInstantiator<>(new BeanInstantiator<>(
            new DefaultInstantiator<>(MultiValuedComponent.class), new RuntimeProperties(properties)), this);

        ValueExpressionPropertyContract<MultiValuedComponent> contract;
        contract = new ValueExpressionPropertyContract<>(instantiator, properties, facesContext);

        contract.assertContract();
    }
}
