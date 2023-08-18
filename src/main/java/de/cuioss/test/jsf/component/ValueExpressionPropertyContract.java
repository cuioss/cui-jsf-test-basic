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

import java.util.List;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.test.el.MockValueExpression;

import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;

/**
 * Tests all given properties according to the given List of
 * {@link ComponentPropertyMetadata}
 *
 * @author Oliver Wolff
 * @param <T> Rule does not apply to annotations: There is no inheritance
 */
public class ValueExpressionPropertyContract<T extends UIComponent> implements TestContract<T> {

    private static final CuiLogger log = new CuiLogger(ValueExpressionPropertyContract.class);

    @Getter
    private final ParameterizedInstantiator<T> instantiator;

    private final List<ComponentPropertyMetadata> filteredMetadata;

    private final FacesContext facesContext;

    /**
     * @param instantiator
     * @param metadatas
     * @param facesContext
     */
    public ValueExpressionPropertyContract(final ParameterizedInstantiator<T> instantiator,
            final List<ComponentPropertyMetadata> metadatas, final FacesContext facesContext) {
        this.instantiator = instantiator;
        filteredMetadata = metadatas.stream().filter(m -> !m.isIgnoreOnValueExpresssion()).toList();
        this.facesContext = facesContext;
    }

    @Override
    public void assertContract() {
        List<String> names = filteredMetadata.stream().map(ComponentPropertyMetadata::getName)
                .toList();
        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith properties: ").append(String.join(" ", names));
        log.info(builder.toString());

        checkGetterAndSetterContract();
    }

    private void checkGetterAndSetterContract() {
        final List<PropertySupport> supportList = filteredMetadata.stream().map(PropertySupport::new)
                .toList();
        final UIComponent target = getInstantiator().newInstanceMinimal();

        for (final PropertySupport support : supportList) {
            var expression = new MockValueExpression(BeanConfigDecorator.checkManagedBeanKey(support.getName()),
                    support.getPropertyMetadata().resolveActualClass());
            target.setValueExpression(support.getName(), expression);
            expression.setValue(facesContext.getELContext(), support.generateTestValue());

            support.assertValueSet(target);
        }
    }

}
