/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("ValueExpressionPropertyContract")
class ValueExpressionPropertyContractTest extends AbstractComponentTest<MultiValuedComponent> {

    @Test
    @DisplayName("Should pass the value-expression contract for a component with valid EL handling")
    void shouldPassContractForValidComponent(FacesContext facesContext) {
        var properties = ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
            new MultiValuedComponent());
        var instantiator = new CallbackAwareInstantiator<>(new BeanInstantiator<>(
            new DefaultInstantiator<>(MultiValuedComponent.class), new RuntimeProperties(properties)), this);
        var contract = new ValueExpressionPropertyContract<>(instantiator, properties, facesContext);

        assertDoesNotThrow(contract::assertContract,
            "Value-expression contract should hold for a component with valid EL handling");
    }
}
