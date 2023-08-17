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

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.inject.Named;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.ObjectContractTestSupport;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.contract.BeanPropertyContractImpl;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.test.valueobjects.util.ObjectContractHelper;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for testing {@link Named} beans. The test runs implicitly
 * {@link VerifyBeanProperty}, therefore the annotation is optional, but it can
 * be used for further configuration.
 * <p>
 * As default all {@link ObjectTestContracts} a run, see
 * {@link #shouldImplementObjectContracts()}
 * </p>
 * <p>
 * Documentation on how to setup the generator can be found at
 * {@link AbstractPropertyAwareFacesTest}
 * </p>
 * <p>
 * Documentation on the setup of the JSF-related test-infrastructure can be
 * found at {@link EnableJsfEnvironment}
 * </p>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after
 * initialization and prior to testing the method {@link #configure(Object)}
 * will be called allowing the concrete test-class to do some specific
 * configuration e.g. calling init-methods and such.
 * </p>
 * <p>
 * You can easily access pre-configured instance by calling {@link #anyBean()}.
 * </p>
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least {@link Serializable}.
 */
public abstract class AbstractBeanTest<T> extends AbstractPropertyAwareFacesTest<T>
        implements ObjectContractTestSupport {

    /** The active object-contracts to be tested */
    private Set<ObjectTestContracts> activeObjectContracts;

    /**
     * Needed {@link ParameterizedInstantiator} for creating test Objects or
     * {@link #shouldImplementObjectContracts()}
     */
    @Getter(AccessLevel.PROTECTED)
    private ParameterizedInstantiator<T> instantiator;

    /**
     * Initializes the object-test-contracts and the corresponding
     * {@link ParameterizedInstantiator}
     */
    @BeforeEach
    void before() {
        activeObjectContracts = ObjectContractHelper.handleVetoedContracts(getClass());
        Optional<VerifyBeanProperty> annotation = MoreReflection.extractAnnotation(getClass(),
                VerifyBeanProperty.class);
        ParameterizedInstantiator<T> inlineInstantiator;
        if (annotation.isPresent()) {
            var meta = AnnotationHelper.handleMetadataForPropertyTest(getClass(), getPropertyMetadata());
            inlineInstantiator = new BeanInstantiator<>(new DefaultInstantiator<>(getTargetBeanClass()),
                    new RuntimeProperties(meta));
        } else {
            inlineInstantiator = new BeanInstantiator<>(new DefaultInstantiator<>(getTargetBeanClass()),
                    new RuntimeProperties(getPropertyMetadata()));
        }
        instantiator = new CallbackAwareInstantiator<>(inlineInstantiator, this);
    }

    @Override
    @Test
    public void shouldImplementObjectContracts() {
        final var objectTestConfig = this.getClass().getAnnotation(ObjectTestConfig.class);
        for (final ObjectTestContracts objectTestContracts : activeObjectContracts) {
            objectTestContracts.newObjectTestInstance().assertContract(getInstantiator(), objectTestConfig);
        }
    }

    /**
     * Tests the individually found properties, see {@link VerifyBeanProperty}
     */
    @Test
    void shouldImplementBeanContract() {
        new BeanPropertyContractImpl<>(getInstantiator()).assertContract();
    }

    /**
     * @return a fully configured instance of the bean with
     *         {@link #configure(Object)} being already called. 'Fully configured'
     *         is defined as all attributes that can be generated by corresponding
     *         generators are set
     */
    public T anyBean() {
        return getInstantiator().newInstanceFull();
    }

}
