package de.icw.cui.test.jsf.component;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.junit5.EnableJsfEnvironment;
import de.icw.cui.test.jsf.junit5.JsfEnabledTestEnvironment;
import io.cui.test.generator.junit.EnableGeneratorController;
import io.cui.test.valueobjects.contract.BeanPropertyContractImpl;
import io.cui.test.valueobjects.junit5.EnableGeneratorRegistry;
import io.cui.test.valueobjects.objects.ConfigurationCallBackHandler;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.CallbackAwareInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.objects.impl.ExceptionHelper;
import io.cui.test.valueobjects.util.GeneratorRegistry;
import io.cui.test.valueobjects.util.PropertyHelper;
import io.cui.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for testing {@link UIComponent}s.
 * <h3>Setup</h3>
 * <p>
 * The actual test must provide {@link EnableJsfEnvironment}, for the basic test-infrastructure. See
 * the class-documentation for details.
 * </p>
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Handling of Property Generators using annotations, see
 * {@link io.cui.test.valueobjects.api.generator}</li>
 * <li>Reflection and annotation based property handling, see
 * {@link io.cui.test.valueobjects.api.property}</li>
 * </ul>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after initialization and prior to
 * testing the method {@link #configure(Object)} will be called allowing the concrete
 * test-class to do some specific configuration e.g. calling init-methods and such.
 * </p>
 * <p>
 * You can easily access pre-configured instance by calling {@link #anyComponent()}.
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
        this.targetClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        try {
            filteredMetadata =
                ComponentTestHelper.filterPropertyMetadata(getClass(), targetClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Unable to instantiate component, due to "
                    + ExceptionHelper.extractCauseMessageFromThrowable(e));
        }

        PropertyHelper.logMessageForPropertyMetadata(filteredMetadata);
        instantiator =
            new CallbackAwareInstantiator<>(new BeanInstantiator<>(new DefaultInstantiator<>(getTargetClass()),
                    new RuntimeProperties(filteredMetadata)), this);
    }

    /**
     * Tests the individual properties directly, saying not {@link ValueExpression}s
     */
    @Test
    public void shouldHandleDirectProperties() {
        new BeanPropertyContractImpl<>(getInstantiator()).assertContract();
    }

    /**
     * Tests the individual properties accessed using {@link ValueExpression}s
     */
    @Test
    public void shouldHandleValueExpressions() {
        new ValueExpressionPropertyContract<>(instantiator, filteredMetadata, getFacesContext())
                .assertContract();
    }

    /**
     * @return a minimal configured instance of the {@link UIComponent} with
     *         {@link #configure(Object)} being already
     *         called. 'Minimal configured' hereby defined as all attributes that are required are
     *         set
     */
    public T anyComponent() {
        return getInstantiator().newInstanceMinimal();
    }

}
