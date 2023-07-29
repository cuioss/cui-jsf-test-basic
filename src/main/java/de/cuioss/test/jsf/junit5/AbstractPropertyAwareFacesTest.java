package de.cuioss.test.jsf.junit5;

import java.util.List;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;

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

/**
 * Extension to {@link JsfEnabledTestEnvironment} that is capable of dealing
 * with properties and generator like {@link ValueObjectTest}.
 * <h3>Supported Contracts / Configurations</h3>
 * <ul>
 * <li>Faces Mock configuration as defined within
 * {@link EnableJsfEnvironment}</li>
 * <li>Handling of Property Generators using annotations, see
 * {@link de.cuioss.test.valueobjects.api.generator}</li>
 * <li>Reflection and annotation based property handling, see
 * {@link de.cuioss.test.valueobjects.api.property}</li>
 * <li>EnableGeneratorController</li>
 * </ul>
 * <p>
 * In case the actual test-class implements {@link TypedGenerator} itself it
 * will implicitly registered as {@link TypedGenerator} at
 * {@link TypedGeneratorRegistry}
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
     * Resolves the {@link PropertyMetadata} by using reflections and the
     * annotations {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link SortedSet} of {@link PropertyMetadata} defining the base
     *         line for the configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
