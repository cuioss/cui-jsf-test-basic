package de.cuioss.test.jsf.component;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.impl.CollectionGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Implements {@link PropertyMetadata} but in addition provides the attribute
 * ignoreOnValueExpresssion
 *
 * @author Oliver Wolff
 */
@EqualsAndHashCode(doNotUseGetters = true)
@ToString
@RequiredArgsConstructor
public class ComponentPropertyMetadata implements PropertyMetadata {

    @NonNull
    private final PropertyMetadata delegate;

    @Getter
    private final boolean ignoreOnValueExpresssion;

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getName()
     */
    @Override
    public String getName() {
        return delegate.getName();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getPropertyClass()
     */
    @Override
    public Class<?> getPropertyClass() {
        return delegate.getPropertyClass();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#next()
     */
    @Override
    public Object next() {
        return delegate.next();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#resolveActualClass()
     */
    @Override
    public Class<?> resolveActualClass() {
        return delegate.resolveActualClass();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#resolveCollectionGenerator()
     */
    @Override
    public CollectionGenerator<?> resolveCollectionGenerator() {
        return delegate.resolveCollectionGenerator();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getGenerator()
     */
    @Override
    public TypedGenerator<?> getGenerator() {
        return delegate.getGenerator();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#isDefaultValue()
     */
    @Override
    public boolean isDefaultValue() {
        return delegate.isDefaultValue();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#isRequired()
     */
    @Override
    public boolean isRequired() {
        return delegate.isRequired();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getPropertyAccessStrategy()
     */
    @Override
    public PropertyAccessStrategy getPropertyAccessStrategy() {
        return delegate.getPropertyAccessStrategy();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getCollectionType()
     */
    @Override
    public CollectionType getCollectionType() {
        return delegate.getCollectionType();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getPropertyMemberInfo()
     */
    @Override
    public PropertyMemberInfo getPropertyMemberInfo() {
        return delegate.getPropertyMemberInfo();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getPropertyReadWrite()
     */
    @Override
    public PropertyReadWrite getPropertyReadWrite() {
        return delegate.getPropertyReadWrite();
    }

    /**
     * @see de.cuioss.test.valueobjects.property.PropertyMetadata#getAssertionStrategy()
     */
    @Override
    public AssertionStrategy getAssertionStrategy() {
        return delegate.getAssertionStrategy();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(PropertyMetadata o) {
        return delegate.compareTo(o);
    }

}
