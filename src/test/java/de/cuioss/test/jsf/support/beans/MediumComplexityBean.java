/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.support.beans;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.tools.property.PropertyMemberInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.*;

@EqualsAndHashCode(exclude = {"noObjectIdentitiyString"})
@ToString(exclude = {"noObjectIdentitiyString"})
public class MediumComplexityBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 2547385591383571419L;
    public static final String ATTRIBUTE_STRING = "string";
    public static final String ATTRIBUTE_TRANSIENT_STRING = "transientString";
    public static final String ATTRIBUTE_NO_OBJECT_IDENTITY_STRING = "noObjectIdentitiyString";
    public static final String ATTRIBUTE_STRING_WITH_DEFAULT = "stringWithDefault";
    public static final String STRING_WITH_DEFAULT_VALUE = "stringWithDefault";
    public static final String ATTRIBUTE_STRING_LIST = "stringList";
    public static final String ATTRIBUTE_STRING_SET = "stringSet";
    public static final String ATTRIBUTE_STRING_SORTED_SET = "stringSortedSet";
    public static final String ATTRIBUTE_STRING_COLLECTION = "stringCollection";
    public static final String ATTRIBUTE_BOOLEAN_OBJECT = "booleanObject";
    public static final String ATTRIBUTE_BOOLEAN_PRIMITIVE = "booleanPrimitive";

    @Getter
    @Setter
    private String string;

    @Getter
    @Setter
    private String noObjectIdentitiyString;

    @Getter
    @Setter
    private transient String transientString;

    @Getter
    @Setter
    private String stringWithDefault = STRING_WITH_DEFAULT_VALUE;

    @Setter
    private String badstring;

    @Getter
    @Setter
    private List<String> stringList;

    @Getter
    @Setter
    private Set<String> stringSet;

    @Getter
    @Setter
    private SortedSet<String> stringSortedSet;

    @Getter
    @Setter
    private Collection<String> stringCollection;

    @Getter
    @Setter
    private Boolean booleanObject;

    @Getter
    @Setter
    private boolean booleanPrimitive;

    /**
     * @return
     */
    public static List<PropertyMetadata> completeValidMetadata() {
        final List<PropertyMetadata> metadata = new ArrayList<>();
        metadata.add(STRINGS.metadata(ATTRIBUTE_STRING));
        metadata.add(STRINGS.metadata(ATTRIBUTE_STRING_LIST, CollectionType.LIST));
        metadata.add(STRINGS.metadata(ATTRIBUTE_STRING_SET, CollectionType.SET));
        metadata.add(
            STRINGS.metadataBuilder(ATTRIBUTE_STRING_SORTED_SET).collectionType(CollectionType.SORTED_SET).build());
        metadata.add(
            STRINGS.metadataBuilder(ATTRIBUTE_STRING_COLLECTION).collectionType(CollectionType.COLLECTION).build());
        metadata.add(BOOLEANS.metadata(ATTRIBUTE_BOOLEAN_OBJECT));
        metadata.add(BOOLEANS_PRIMITIVE.metadata(ATTRIBUTE_BOOLEAN_PRIMITIVE));
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_STRING_WITH_DEFAULT).defaultValue(true).build());
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_TRANSIENT_STRING)
            .propertyMemberInfo(PropertyMemberInfo.TRANSIENT).build());
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_NO_OBJECT_IDENTITY_STRING)
            .propertyMemberInfo(PropertyMemberInfo.NO_IDENTITY).build());
        return metadata;
    }
}
