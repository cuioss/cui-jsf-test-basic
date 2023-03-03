package de.cuioss.test.jsf.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * TestData Store for Test Items which will be used by
 * {@link AbstractConverterTest}<br/>
 * Class is prepared to be used as Fluent Interface
 *
 * @param <T> type of Test Item value
 */
public class TestItems<T> {

    /**
     * Items that are used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     */
    @Getter(AccessLevel.MODULE)
    private final List<ConverterTestItem<T>> validObjectTestItems = new ArrayList<>();

    /**
     * Items that are used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * but are invalid {@link Object}s
     */
    @Getter(AccessLevel.MODULE)
    private final List<ConverterTestItem<T>> invalidObjectTestItems = new ArrayList<>();

    /**
     * Items that are used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, String)}
     */
    @Getter(AccessLevel.MODULE)
    private final List<ConverterTestItem<T>> validStringTestItems = new ArrayList<>();

    /**
     * Items that are used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, String)}
     */
    @Getter(AccessLevel.MODULE)
    private final List<ConverterTestItem<T>> invalidStringTestItems = new ArrayList<>();

    @Getter(AccessLevel.MODULE)
    private final Set<String> roundtripValues = new HashSet<>();

    /**
     * Adds roundtrip String values to be tested. See
     * {@link AbstractConverterTest#shouldRoundTripValidData()}
     *
     * @param roundtripValue the values to be roundtrip converted
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addRoundtripValues(final String... roundtripValue) {
        roundtripValues.addAll(Arrays.asList(roundtripValue));
        return this;
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * Test item must fail with {@link ConverterException}
     *
     * @param value T invalid value which should cause a {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addInvalidObject(final T value) {
        return this.addinValidObjectTestItem(value, null, FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * Test item must fail with {@link ConverterException}
     *
     * @param value T invalid value which should cause a {@link ConverterException}
     * @param message which should be set within the {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addInvalidObjectWithMessage(final T value, final String message) {
        return this.addinValidObjectTestItem(value, null, FacesMessage.SEVERITY_ERROR,
                message);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * Test item should pass without {@link ConverterException}
     *
     * @param value valid value which should cause a {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addValidObject(final T value) {
        return this.addValidObjectTestItem(value, null, null, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * Test item should pass without {@link ConverterException} and the result should be the same as
     * the given converterResult
     *
     * @param value valid value which should cause a {@link ConverterException}
     * @param converterResult the String to be returned by
     *            {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addValidObjectWithStringResult(final T value,
            final String converterResult) {
        return this.addValidObjectTestItem(value, converterResult, null, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * Test item must fail with {@link ConverterException}
     *
     * @param value invalid String-value which should cause a {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addInvalidString(final String value) {
        return this.addStringTestItem(false, null, value, FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * Test item must fail with {@link ConverterException}
     *
     * @param value invalid String-value which should cause a {@link ConverterException}
     * @param message which should be set within the {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addInvalidStringWithMessage(final String value, final String message) {
        return this.addStringTestItem(false, null, value, FacesMessage.SEVERITY_ERROR, message);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * Test item should pass without {@link ConverterException}
     *
     * @param value valid String-value which should pass without {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addValidString(final String value) {
        return this.addStringTestItem(true, null, value, null, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * Test item should pass without {@link ConverterException} and the result should be the same as
     * the given converterResult
     *
     * @param value valid String-value which should pass without {@link ConverterException}
     * @param converterResult the String to be returned by
     *            {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * @return TestItems<T> reference to this object
     */
    public TestItems<T> addValidStringWithObjectResult(final String value,
            final T converterResult) {
        return this.addStringTestItem(true, converterResult, value, null, null);
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * that defines a valid Object
     *
     * @param value T value to be validated
     * @param converterResult the String to be returned by
     *            {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * @param level {@link Severity} represent message severity, usually
     *            {@link FacesMessage#SEVERITY_ERROR}
     * @param message which should be set within the {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    private TestItems<T> addValidObjectTestItem(final T value,
            final String converterResult,
            final Severity level,
            final String message) {
        final var item = new ConverterTestItem<T>();
        item.setTestValue(value);
        item.setStringValue(converterResult);
        item.setValid(true);
        item.setErrorMessage(message);
        item.setSeverity(level);
        validObjectTestItems.add(item);
        return this;
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * that defines an invalid Object
     *
     * @param value T value to be validated
     * @param converterResult the String to be returned by
     *            {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * @param level {@link Severity} represent message severity, usually
     *            {@link FacesMessage#SEVERITY_ERROR}
     * @param message which should be set within the {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    private TestItems<T> addinValidObjectTestItem(final T value,
            final String converterResult,
            final Severity level,
            final String message) {
        final var item = new ConverterTestItem<T>();
        item.setTestValue(value);
        item.setStringValue(converterResult);
        item.setValid(false);
        item.setErrorMessage(message);
        item.setSeverity(level);
        invalidObjectTestItems.add(item);
        return this;
    }

    /**
     * Adds testData to be used for testing
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     *
     * @param valid indicating whether it is a valid or invalid item
     * @param value T value to be validated
     * @param converterResult the String to be returned by
     *            {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * @param level {@link Severity} represent message severity, usually
     *            {@link FacesMessage#SEVERITY_ERROR}
     * @param message which should be set within the {@link ConverterException}
     * @return TestItems<T> reference to this object
     */
    private TestItems<T> addStringTestItem(final boolean valid, final T value,
            final String converterResult,
            final Severity level,
            final String message) {
        final var item = new ConverterTestItem<T>();
        item.setTestValue(value);
        item.setStringValue(converterResult);
        item.setValid(valid);
        item.setErrorMessage(message);
        item.setSeverity(level);
        if (valid) {
            validStringTestItems.add(item);
        } else {
            invalidStringTestItems.add(item);
        }
        return this;
    }
}
