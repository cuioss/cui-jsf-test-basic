package de.icw.cui.test.jsf.component;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.faces.component.UIComponent;

import de.icw.cui.test.jsf.config.component.VerifyComponentProperties;
import io.cui.test.valueobjects.generator.dynamic.GeneratorResolver;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.util.AnnotationHelper;
import io.cui.test.valueobjects.util.PropertyHelper;
import io.cui.tools.property.PropertyHolder;
import io.cui.tools.property.PropertyReadWrite;
import io.cui.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

/**
 * Helper class providing utility methods for convenient filtering of properties.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class ComponentTestHelper {

    /**
     * Filters the given metadata according to the annotations {@link VerifyComponentProperties}
     * found on the given annotated type
     *
     * @param annotated must not be null
     * @param instance of the type to be checked.
     * @return the filtered list with {@link ComponentPropertyMetadata}
     */
    public static List<ComponentPropertyMetadata> filterPropertyMetadata(
            final Class<?> annotated,
            final UIComponent instance) {
        requireNonNull(annotated);
        requireNonNull(instance);

        Set<PropertyMetadata> propertyConfigs =
                PropertyHelper.handlePropertyConfigAnnotations(annotated);

        ArrayList<String> of = new ArrayList<>();
        ArrayList<String> defaultValued = new ArrayList<>();
        ArrayList<String> noVE = new ArrayList<>();
        ArrayList<String> unorderedCollection = new ArrayList<>();
        for (VerifyComponentProperties property : MoreReflection.extractAllAnnotations(annotated,
                VerifyComponentProperties.class)) {
            of.addAll(Arrays.asList(property.of()));
            defaultValued.addAll(Arrays.asList(property.defaultValued()));
            noVE.addAll(Arrays.asList(property.noValueExpression()));
            unorderedCollection.addAll(Arrays.asList(property.assertUnorderedCollection()));
        }

        final Map<String, PropertyMetadata> map = new HashMap<>();
        propertyConfigs.forEach(pc -> map.put(pc.getName(), pc));
        for (String configuredName : of) {
            map.put(configuredName, resolvePropertyForConfiguredName(instance, configuredName));
        }

        Map<String, PropertyMetadata> filtered = AnnotationHelper.modifyPropertyMetadata(map,
                defaultValued, Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                unorderedCollection);

        List<ComponentPropertyMetadata> found = new ArrayList<>();
        for (Entry<String, PropertyMetadata> entry : filtered.entrySet()) {
            found.add(
                    new ComponentPropertyMetadata(entry.getValue(), noVE.contains(entry.getKey())));
        }
        return found;
    }

    /**
     * Creates a {@link PropertyMetadata} for the given propertyName on the given type.
     *
     * @param instance must not be null
     * @param configuredName must not be null
     * @return {@link PropertyMetadata} instance with the corresponding attributes.
     */
    public static PropertyMetadata resolvePropertyForConfiguredName(final UIComponent instance,
            final String configuredName) {
        Class<?> propertyType = null;
        CollectionType collectionType = CollectionType.NO_ITERABLE;
        propertyType = PropertyHolder.from(instance.getClass(), configuredName)
                .orElseThrow(
                        () -> new IllegalStateException("Unable to determine property type for " + configuredName
                                + ", use @PropertyConfig to define this property"))
                .getType();
        final Optional<CollectionType> collectionTypeOption =
                CollectionType.findResponsibleCollectionType(propertyType);
        if (collectionTypeOption.isPresent()) {
            throw new IllegalStateException(
                    String.format(
                            "Unable to determine generic-type for %s, you need to "
                                    + "provide a custom @PropertyConfig for this field",
                                    configuredName));

        }
        return PropertyMetadataImpl.builder().propertyClass(propertyType).name(configuredName)
                .collectionType(collectionType).defaultValue(propertyType.isPrimitive())
                .generator(GeneratorResolver.resolveGenerator(propertyType))
                .propertyReadWrite(PropertyReadWrite.READ_WRITE).build();
    }

}
