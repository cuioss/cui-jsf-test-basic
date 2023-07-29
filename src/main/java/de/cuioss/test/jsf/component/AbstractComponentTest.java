package de.cuioss.test.jsf.component;

import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.junit5.JsfEnabledTestEnvironment;
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
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for testing {@link UIComponent}s.
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Handling of Property Generators using annotations, see
 * {@link de.cuioss.test.valueobjects.api.generator}</li>
 * <li>Reflection and annotation based property handling, see
 * {@link de.cuioss.test.valueobjects.api.property}</li>
 * </ul>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after
 * initialization and prior to testing the method {@link #configure(Object)}
 * will be called allowing the concrete test-class to do some specific
 * configuration e.g. calling init-methods and such.
 * </p>
 * <p>
 * You can easily access pre-configured instance by calling
 * {@link #anyComponent()}.
 * </p>
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested, at least an {@link UIComponent}
 */
@EnableGeneratorController
@EnableGeneratorRegistry
public abstract class AbstractComponentTest<T extends UIComponent> extends JsfEnabledTestEnvironment
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
    void shouldHandleValueExpressions() {
        new ValueExpressionPropertyContract<>(instantiator, filteredMetadata, getFacesContext()).assertContract();
    }

    /**
     * @return a minimal configured instance of the {@link UIComponent} with
     *         {@link #configure(Object)} being already called. 'Minimal configured'
     *         hereby defined as all attributes that are required are set
     */
    public T anyComponent() {
        return getInstantiator().newInstanceMinimal();
    }

}
