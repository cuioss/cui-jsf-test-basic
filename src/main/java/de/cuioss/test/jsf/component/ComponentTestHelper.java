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

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.test.valueobjects.util.PropertyHelper;
import de.cuioss.tools.property.PropertyHolder;
import de.cuioss.tools.property.PropertyReadWrite;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.faces.component.UIComponent;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Objects.requireNonNull;

/**
 * Helper class providing utility methods for convenient filtering of
 * properties.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class ComponentTestHelper {

    /**
     * Filters the given metadata according to the annotations
     * {@link VerifyComponentProperties} found on the given annotated type
     *
     * @param annotated must not be null
     * @param instance  of the type to be checked.
     * @return the filtered list with {@link ComponentPropertyMetadata}
     */
    public static List<ComponentPropertyMetadata> filterPropertyMetadata(final Class<?> annotated,
        final UIComponent instance) {
        requireNonNull(annotated);
        requireNonNull(instance);

        var propertyConfigs = PropertyHelper.handlePropertyConfigAnnotations(annotated);

        var of = new ArrayList<String>();
        var defaultValued = new ArrayList<String>();
        var noVE = new ArrayList<String>();
        var unorderedCollection = new ArrayList<String>();
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

        var filtered = AnnotationHelper.modifyPropertyMetadata(map, defaultValued, Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), unorderedCollection);

        List<ComponentPropertyMetadata> found = new ArrayList<>();
        for (Entry<String, PropertyMetadata> entry : filtered.entrySet()) {
            found.add(new ComponentPropertyMetadata(entry.getValue(), noVE.contains(entry.getKey())));
        }
        return found;
    }

    /**
     * Creates a {@link PropertyMetadata} for the given propertyName on the given
     * type.
     *
     * @param instance       must not be null
     * @param configuredName must not be null
     * @return {@link PropertyMetadata} instance with the corresponding attributes.
     */
    public static PropertyMetadata resolvePropertyForConfiguredName(final UIComponent instance,
        final String configuredName) {
        Class<?> propertyType = null;
        var collectionType = CollectionType.NO_ITERABLE;
        propertyType = PropertyHolder.from(instance.getClass(), configuredName)
            .orElseThrow(() -> new IllegalStateException("Unable to determine property type for " + configuredName
                + ", use @PropertyConfig to define this property"))
            .getType();
        final var collectionTypeOption = CollectionType.findResponsibleCollectionType(propertyType);
        if (collectionTypeOption.isPresent()) {
            throw new IllegalStateException("""
                Unable to determine generic-type for %s, you need to \
                provide a custom @PropertyConfig for this field\
                """.formatted(configuredName));

        }
        return PropertyMetadataImpl.builder().propertyClass(propertyType).name(configuredName)
            .collectionType(collectionType).defaultValue(propertyType.isPrimitive())
            .generator(GeneratorResolver.resolveGenerator(propertyType))
            .propertyReadWrite(PropertyReadWrite.READ_WRITE).build();
    }

}
