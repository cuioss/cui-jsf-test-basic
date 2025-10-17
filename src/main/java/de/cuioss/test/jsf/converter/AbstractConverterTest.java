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
package de.cuioss.test.jsf.converter;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base class for testing implementations of {@link Converter} within a Junit 5
 * context
 * <h3>Setup</h3>
 * <p>
 * {@link #initConverter()}: Instantiates the concrete {@link Converter} using
 * reflection. After this the method calls {@link #configure(Object)} that can
 * be used for further configuration of the {@link Converter}
 * </p>
 * <p>
 * In case you want to create the {@link Converter} yourself you can
 * overwrite the getConverter() method
 * </p>
 * <p>
 * In case you want a pass another {@link UIComponent} as parameter to the
 * {@link Converter} you can overwrite the getComponent() method
 * </p>
 * <h3>Test-Methods</h3> The core test-methods are:
 * <ul>
 * <li>{@link #shouldFailOnInvalidObjects(FacesContext facesContext)}</li>
 * <li>{@link #shouldFailOnInvalidStrings(FacesContext facesContext)}</li>
 * <li>{@link #shouldPassOnValidObjects(FacesContext facesContext)}</li>
 * <li>{@link #shouldPassOnValidStrings(FacesContext facesContext)}</li>
 * <li>{@link #shouldRoundTripValidData(FacesContext facesContext)}</li>
 * </ul>
 * They call {@link #populate(TestItems)} in oder to create corresponding
 * test-data. The implementation is in the actual test-class.
 * <h3>API-Test</h3> The api as defined within {@link Converter} is tested with
 * the methods
 * <ul>
 * <li>{@link #shouldFailOnNullComponentOnGetAsObject(FacesContext facesContext)}</li>
 * <li>{@link #shouldFailOnNullComponentOnGetAsString(FacesContext facesContext)}</li>
 * <li>{@link #shouldFailOnNullFacesContextOnGetAsObject(FacesContext facesContext)}</li>
 * <li>{@link #shouldFailOnNullFacesContextOnGetAsString()}</li>
 * <li>{@link #shouldReturnEmptyStringOnNullValue(FacesContext facesContext)}</li>
 * </ul>
 * <h3>Example</h3> Shows all variants of dealing with {@link TestItems}
 *
 * <pre>
 * <code>
 * &#64;EnableJsfEnvironment
 * class IntegerConverterTest extends AbstractConverterTest&lt;IntegerConverter, Integer&gt; {
 *
 * &#64;Override
 * public void populate(final TestItems&lt;Integer&gt; testItems) {
 * testItems.addRoundtripValues("1", "122", "2132121").addInvalidString("a")
 * .addInvalidStringWithMessage("a", "jakarta.faces.converter.IntegerConverter.INTEGER")
 * .addInvalidObject(Boolean.TRUE)
 * .addInvalidObjectWithMessage(Boolean.FALSE, "jakarta.faces.converter.STRING")
 * .addValidString("13").addValidStringWithObjectResult("17", Integer.valueOf(17))
 * .addValidObject(Integer.valueOf(2))
 * .addValidObjectWithStringResult(Integer.valueOf(14), "14");
 * }
 * </code>
 * </pre>
 *
 * @param <C> identifying the concrete {@link Converter} to be tested.
 * @param <T> identifying the type of elements to be used for values to be given
 *            to the {@link Converter}
 * @author Oliver Wolff
 */
@SuppressWarnings({"rawtypes", "unchecked"})
// owolff we need to migrate this aspect later
@EnableGeneratorController
@EnableJsfEnvironment
public abstract class AbstractConverterTest<C extends Converter, T>
    implements ConfigurationCallBackHandler<C> {

    private static final String SHOULD_HAVE_THROWN_CONVERTER_EXCEPTION = "Should have thrown ConverterException for invalid Value: ";

    @Getter
    @Setter
    private UIComponent component = new HtmlInputText();

    @Getter
    private C converter;

    @Getter(AccessLevel.PROTECTED)
    private TestItems<T> testItems;

    /**
     * Instantiates and initially configures a concrete {@link Converter}
     */
    @BeforeEach
    protected void initConverter() {
        final Class<C> clazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        converter = new DefaultInstantiator<>(clazz).newInstance();
        configure(converter);
        testItems = new TestItems<>();
        populate(testItems);
    }

    /**
     * Populates the test-items
     *
     * @param testItems to be populated is never null
     */
    public abstract void populate(TestItems<T> testItems);

    /**
     * Callback method for interacting with the {@link ComponentConfigDecorator} at
     * the correct time.<br>
     * This method provides <b>extension point</b> to prepare needed test environment
     * for your converter test. For example :
     *
     * <pre>
     *    // register some converter
     *    decorator.registerConverter(MyRequiredConverter.class);
     *    // register UIComponent
     *      ...
     * </pre>
     *
     * @param decorator {@link ComponentConfigDecorator} is never null
     */
    protected void configureComponents(final ComponentConfigDecorator decorator) {
        decorator.registerMockRenderer(HtmlInputText.COMPONENT_FAMILY, "jakarta.faces.Text");
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link UIComponent}, see
     * {@link Converter#getAsObject(FacesContext, UIComponent, String)}
     */
    @Test
    void shouldFailOnNullComponentOnGetAsObject(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> getConverter().getAsObject(facesContext, null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link FacesContext}, see
     * {@link Converter#getAsObject(FacesContext, UIComponent, String)}
     */
    @Test
    void shouldFailOnNullFacesContextOnGetAsObject(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> getConverter().getAsObject(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link UIComponent}, see
     * {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullComponentOnGetAsString(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> getConverter().getAsString(facesContext, null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link FacesContext}, see
     * {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullFacesContextOnGetAsString() {
        assertThrows(NullPointerException.class, () -> getConverter().getAsString(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for the actual
     * value, see {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldReturnEmptyStringOnNullValue(FacesContext facesContext) {
        assertEquals("", getConverter().getAsString(facesContext, getComponent(), null));
    }

    /**
     * Core test for converter testing. It collects the test-data using
     * {@link TestItems} and iterates them for the individual test. For each element
     * there will be called the method
     * {@link Converter#getAsObject(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, String)},
     * with the result again
     * {@link Converter#getAsString(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, Object)}
     * with the result being checked against the initial value.
     */
    @Test
    void shouldRoundTripValidData(FacesContext facesContext) {
        for (final String value : getTestItems().getRoundtripValues()) {
            final var converted = (T) getConverter().getAsObject(facesContext, getComponent(), value);
            final var roundTripped = getConverter().getAsString(facesContext, getComponent(), converted);
            assertEquals(value, roundTripped);
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsString(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, Object)}
     * with invalid objects, derived by {@link TestItems}
     */
    @Test
    void shouldFailOnInvalidObjects(FacesContext facesContext) {
        for (final ConverterTestItem<T> item : getTestItems().getInvalidObjectTestItems()) {
            try {
                getConverter().getAsString(facesContext, getComponent(), item.getTestValue());
                fail(SHOULD_HAVE_THROWN_CONVERTER_EXCEPTION + item);
            } catch (final ConverterException e) {
                verifyExpectedErrorMessage(item, e);
            }
        }
    }

    private void verifyExpectedErrorMessage(final ConverterTestItem<T> item, final ConverterException e) {
        // Check message
        if (null != item.getErrorMessage()) {
            assertEquals(item.getErrorMessage(), e.getFacesMessage().getSummary(),
                "Wrong error message detected. TestItem was : " + item);
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsString(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, Object)}
     * with valid objects, derived by {@link TestItems}
     */
    @Test
    void shouldPassOnValidObjects(FacesContext facesContext) {
        for (final ConverterTestItem<T> item : getTestItems().getValidObjectTestItems()) {
            final var result = getConverter().getAsString(facesContext, getComponent(), item.getTestValue());
            if (null != item.getStringValue()) {
                assertEquals(item.getStringValue(), result, item.toString());
            }
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsObject(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, String)}
     * with invalid objects, derived by {@link TestItems}
     */
    @Test
    void shouldFailOnInvalidStrings(FacesContext facesContext) {
        for (final ConverterTestItem<T> item : getTestItems().getInvalidStringTestItems()) {
            try {
                getConverter().getAsObject(facesContext, getComponent(), item.getStringValue());
                fail(SHOULD_HAVE_THROWN_CONVERTER_EXCEPTION + item);
            } catch (final ConverterException e) {
                verifyExpectedErrorMessage(item, e);
            }
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsObject(jakarta.faces.context.FacesContext, jakarta.faces.component.UIComponent, String)}
     * with valid String, derived by {@link TestItems}
     */
    @Test
    void shouldPassOnValidStrings(FacesContext facesContext) {
        for (final ConverterTestItem<T> item : getTestItems().getValidStringTestItems()) {
            final var result = (T) getConverter().getAsObject(facesContext, getComponent(), item.getStringValue());
            if (null != item.getTestValue()) {
                assertEquals(item.getTestValue(), result, item.toString());
            }

        }
    }

}
