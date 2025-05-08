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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyConfigs;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.test.valueobjects.util.ReflectionHelper;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.SortedSet;

/**
 * Base class for JSF-tests that is capable of dealing
 * with properties and generator like {@link ValueObjectTest}.
 * It is annotated with EnableJsfEnvironment
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Handling of Property Generators using annotations, see
 * {@link de.cuioss.test.valueobjects.api.generator}</li>
 * <li>Reflection and annotation-based property handling, see
 * {@link de.cuioss.test.valueobjects.api.property}</li>
 * <li>EnableGeneratorController</li>
 * </ul>
 * <p>
 * In case the actual test-class implements {@link TypedGenerator} itself it
 * will implicitly register as {@link TypedGenerator} at
 * {@link TypedGeneratorRegistry}
 * </p>
 *
 * @param <T> identifying the concrete type to be tested
 * @author Oliver Wolff
 */
@EnableJsfEnvironment(useIdentityResourceBundle = true)
@EnableGeneratorController
@EnableGeneratorRegistry
public abstract class AbstractPropertyAwareFacesTest<T>
    implements ConfigurationCallBackHandler<T>, GeneratorRegistry {

    @Getter
    private Class<T> targetBeanClass;

    @Getter
    private List<PropertyMetadata> propertyMetadata;

    /**
     * Initializes all contracts, properties and generator
     */
    @BeforeEach
    public void initializeAbstractPropertyAwareFacesTest() {
        targetBeanClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        propertyMetadata = resolvePropertyMetadata();
    }

    /**
     * Resolves the {@link PropertyMetadata} by using reflections and the
     * annotations {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link SortedSet} of {@link PropertyMetadata} defining the base
     * line for the configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
