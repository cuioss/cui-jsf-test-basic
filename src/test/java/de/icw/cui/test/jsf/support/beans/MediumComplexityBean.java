package de.icw.cui.test.jsf.support.beans;

import static io.cui.test.valueobjects.generator.JavaTypesGenerator.BOOLEANS;
import static io.cui.test.valueobjects.generator.JavaTypesGenerator.BOOLEANS_PRIMITIVE;
import static io.cui.test.valueobjects.generator.JavaTypesGenerator.STRINGS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.tools.property.PropertyMemberInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("javadoc")
@EqualsAndHashCode(exclude = { "noObjectIdentitiyString" })
@ToString(exclude = { "noObjectIdentitiyString" })
public class MediumComplexityBean implements Serializable {

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
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_STRING_SORTED_SET)
                .collectionType(CollectionType.SORTED_SET).build());
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_STRING_COLLECTION)
                .collectionType(CollectionType.COLLECTION).build());
        metadata.add(BOOLEANS.metadata(ATTRIBUTE_BOOLEAN_OBJECT));
        metadata.add(BOOLEANS_PRIMITIVE.metadata(ATTRIBUTE_BOOLEAN_PRIMITIVE));
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_STRING_WITH_DEFAULT)
                .defaultValue(true).build());
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_TRANSIENT_STRING)
                .propertyMemberInfo(PropertyMemberInfo.TRANSIENT).build());
        metadata.add(STRINGS.metadataBuilder(ATTRIBUTE_NO_OBJECT_IDENTITY_STRING)
                .propertyMemberInfo(PropertyMemberInfo.NO_IDENTITY).build());
        return metadata;
    }
}
