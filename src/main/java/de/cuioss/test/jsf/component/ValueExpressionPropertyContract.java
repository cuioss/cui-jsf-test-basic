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

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import org.apache.myfaces.test.el.MockValueExpression;

import java.util.List;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;

/**
 * Tests all given properties according to the given List of
 * {@link ComponentPropertyMetadata}
 *
 * @param <T> The Rule does not apply to annotations: There is no inheritance
 * @author Oliver Wolff
 */
public class ValueExpressionPropertyContract<T extends UIComponent> implements TestContract<T> {

    /**
     * Wraps textual names to EL-expressions.
     */
    @SuppressWarnings("el-syntax")
    public static final String EL_WRAPPER = "#{%s}";
    /**
     * String constant used for checking if a given String is EL-Expression.
     */
    @SuppressWarnings("el-syntax")
    public static final String EL_START = "#{";
    private static final CuiLogger log = new CuiLogger(ValueExpressionPropertyContract.class);
    private static final String NAME_MUST_NOT_BE_NULL = "Name must not be null";
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

    /**
     * In case the beanKey is not an el expression (starting not with '#{') this
     * method wraps the expression accordingly.
     *
     * @param managedBeanKey must not be null or empty
     * @return the key wrapped as an EL-Expression if needed, otherwise the given
     * key.
     */
    private static String checkManagedBeanKey(final String managedBeanKey) {
        requireNonNull(emptyToNull(managedBeanKey), NAME_MUST_NOT_BE_NULL);
        var elKey = managedBeanKey;
        if (!elKey.startsWith(EL_START)) {
            elKey = EL_WRAPPER.formatted(elKey);
        }
        return elKey;
    }

    @Override
    public void assertContract() {
        var names = filteredMetadata.stream().map(ComponentPropertyMetadata::getName).toList();
        log.info("Verifying " + getClass().getName() + "\nWith properties: " + String.join(" ", names));

        checkGetterAndSetterContract();
    }

    private void checkGetterAndSetterContract() {
        final var supportList = filteredMetadata.stream().map(PropertySupport::new).toList();
        final UIComponent target = getInstantiator().newInstanceMinimal();

        for (final PropertySupport support : supportList) {
            var expression = new MockValueExpression(checkManagedBeanKey(support.getName()),
                support.getPropertyMetadata().resolveActualClass());
            target.setValueExpression(support.getName(), expression);
            expression.setValue(facesContext.getELContext(), support.generateTestValue());

            support.assertValueSet(target);
        }
    }
}
