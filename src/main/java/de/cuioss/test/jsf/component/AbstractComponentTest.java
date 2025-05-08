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

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.contract.BeanPropertyContractImpl;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.test.valueobjects.util.PropertyHelper;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Base class for testing {@link UIComponent}s.
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Handling of Property Generators using annotations from the
 * de.cuioss.test.valueobjects.api.generator package</li>
 * <li>Reflection and annotation-based property handling from the
 * de.cuioss.test.valueobjects.api.property package</li>
 * </ul>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after
 * initialization and prior to testing the method {@link #configure(Object)}
 * will be called allowing the concrete test-class to do some specific
 * configuration e.g., calling init-methods and such.
 * </p>
 * <p>
 * You can access pre-configured instance by calling
 * {@link #anyComponent()}.
 * </p>
 *
 * @param <T> identifying the type to be tested, at least an {@link UIComponent}
 * @author Oliver Wolff
 */
@EnableJsfEnvironment
@EnableGeneratorController
@EnableGeneratorRegistry
public abstract class AbstractComponentTest<T extends UIComponent>
    implements ConfigurationCallBackHandler<T>, GeneratorRegistry {

    @Getter
    private Class<T> targetClass;

    /**
     * Needed {@link ParameterizedInstantiator} for creating test Objects
     */
    @Getter(AccessLevel.PROTECTED)
    private ParameterizedInstantiator<T> instantiator;

    private List<ComponentPropertyMetadata> filteredMetadata;

    /**
     * Initializes all contracts, properties and generator
     */
    @BeforeEach
    public void initializeBaseClass() {
        targetClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        filteredMetadata = ComponentTestHelper.filterPropertyMetadata(getClass(),
            new DefaultInstantiator<>(targetClass).newInstance());

        PropertyHelper.logMessageForPropertyMetadata(filteredMetadata);
        instantiator = new CallbackAwareInstantiator<>(new BeanInstantiator<>(
            new DefaultInstantiator<>(getTargetClass()), new RuntimeProperties(filteredMetadata)), this);
    }

    /**
     * Tests the individual properties directly, saying not {@link ValueExpression}s
     */
    @Test
    void shouldHandleDirectProperties() {
        new BeanPropertyContractImpl<>(getInstantiator()).assertContract();
    }

    /**
     * Tests the individual properties accessed using {@link ValueExpression}s
     */
    @Test
    void shouldHandleValueExpressions(FacesContext facesContext) {
        new ValueExpressionPropertyContract<>(instantiator, filteredMetadata, facesContext).assertContract();
    }

    /**
     * @return a minimal configured instance of the {@link UIComponent} with
     * {@link #configure(Object)} being already called. 'Minimal configured'
     * hereby defined as all attributes that are required are set
     */
    public T anyComponent() {
        return getInstantiator().newInstanceMinimal();
    }

}
