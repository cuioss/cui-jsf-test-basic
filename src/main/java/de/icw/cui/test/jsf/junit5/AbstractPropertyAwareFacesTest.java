package de.icw.cui.test.jsf.junit5;

import java.util.List;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.generator.junit.EnableGeneratorController;
import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyConfigs;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.junit5.EnableGeneratorRegistry;
import io.cui.test.valueobjects.objects.ConfigurationCallBackHandler;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.util.GeneratorRegistry;
import io.cui.test.valueobjects.util.ReflectionHelper;
import io.cui.tools.reflect.MoreReflection;
import lombok.Getter;

/**
 * Extension to {@link JsfEnabledTestEnvironment} that is capable of dealing with properties and
 * generator like {@link ValueObjectTest}.
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Faces Mock configuration as defined within {@link EnableJsfEnvironment}</li>
 * <li>Handling of Property Generators using annotations, see
 * {@link io.cui.test.valueobjects.api.generator}</li>
 * <li>Reflection and annotation based property handling, see
 * {@link io.cui.test.valueobjects.api.property}</li>
 * <li>EnableGeneratorController</li>
 * </ul>
 * <p>
 * In case the actual test-class implements {@link TypedGenerator} itself it will implicitly
 * registered as {@link TypedGenerator} at {@link TypedGeneratorRegistry}
 * </p>
 *
 * @author Oliver Wolff
 * @param <T> identifying the concrete type to be tested
 */
@EnableGeneratorController
@EnableGeneratorRegistry
public abstract class AbstractPropertyAwareFacesTest<T> extends JsfEnabledTestEnvironment
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
        this.targetBeanClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        this.propertyMetadata = resolvePropertyMetadata();
    }

    /**
     * Resolves the {@link PropertyMetadata} by using reflections and the annotations
     * {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link SortedSet} of {@link PropertyMetadata} defining the base line for the
     *         configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
