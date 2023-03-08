package de.cuioss.test.jsf.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.junit5.JsfEnabledTestEnvironment;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for testing implementations of {@link Converter} within a Junit 5 context
 * <h3>Setup</h3>
 * <p>
 * The actual test must provide {@link EnableJsfEnvironment}, for the basic test-infrastructure. See
 * the class-documentation for details.
 * </p>
 * <p>
 * {@link #initConverter()}: Instantiates the concrete {@link Converter} using reflection. After
 * this the method calls {@link #configure(Object)} that can be used for further configuration of
 * the {@link Converter}
 * </p>
 * <p>
 * In case you want you want to create the {@link Converter} yourself you can overwrite
 * {@link #getConverter()}
 * </p>
 * <p>
 * In case you want a pass another {@link UIComponent} as parameter to the {@link Converter} you can
 * overwrite {@link #getComponent()}
 * </p>
 * <h3>Test-Methods</h3>
 * The core test-methods are:
 * <ul>
 * <li>{@link #shouldFailOnInvalidObjects()}</li>
 * <li>{@link #shouldFailOnInvalidStrings()}</li>
 * <li>{@link #shouldPassOnValidObjects()}</li>
 * <li>{@link #shouldPassOnValidStrings()}</li>
 * <li>{@link #shouldRoundTripValidData()}</li>
 * </ul>
 * They call {@link #populate(TestItems)} in oder to create corresponding test-data. The
 * implementation is in the actual test-class.
 * <h3>API-Test</h3>
 * The api as defined within {@link Converter} is tested with the methods
 * <ul>
 * <li>{@link #shouldFailOnNullComponentOnGetAsObject()}</li>
 * <li>{@link #shouldFailOnNullComponentOnGetAsString()}</li>
 * <li>{@link #shouldFailOnNullFacesContextOnGetAsObject()}</li>
 * <li>{@link #shouldFailOnNullFacesContextOnGetAsString()}</li>
 * <li>{@link #shouldReturnEmptyStringOnNullValue()}</li>
 * </ul>
 * <h3>Example</h3>
 * Shows all variants of dealing with {@link TestItems}
 *
 * <pre>
 * <code>
 * &#64;EnableJsfEnvironment
 * class IntegerConverterTest extends AbstractConverterTest&lt;IntegerConverter, Integer&gt; {
 *
 * &#64;Override
 * public void populate(final TestItems&lt;Integer&gt; testItems) {
 * testItems.addRoundtripValues("1", "122", "2132121").addInvalidString("a")
 * .addInvalidStringWithMessage("a", "javax.faces.converter.IntegerConverter.INTEGER")
 * .addInvalidObject(Boolean.TRUE)
 * .addInvalidObjectWithMessage(Boolean.FALSE, "javax.faces.converter.STRING")
 * .addValidString("13").addValidStringWithObjectResult("17", Integer.valueOf(17))
 * .addValidObject(Integer.valueOf(2))
 * .addValidObjectWithStringResult(Integer.valueOf(14), "14");
 * }
 * </code>
 * </pre>
 *
 * @param <C> identifying the concrete {@link Converter} to be tested.
 * @param <T> identifying the type of elements to be used for values to be given to the
 *            {@link Converter}
 *
 * @author Oliver Wolff
 */
@SuppressWarnings({ "rawtypes", "unchecked" }) // owolff we need to migrate this aspect later
public abstract class AbstractConverterTest<C extends Converter, T> extends JsfEnabledTestEnvironment
        implements ConfigurationCallBackHandler<C>, ComponentConfigurator {

    private static final String SHOULD_HAVE_THROWN_CONVERTER_EXCEPTION =
        "Should have thrown ConverterException for invalid Value: ";

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
        final Class<C> klazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        converter = new DefaultInstantiator<>(klazz).newInstance();
        configure(converter);
        testItems = new TestItems<>();
        populate(testItems);
    }

    /**
     * Populates the test-items
     *
     * @param testItems to be populated, is never null
     */
    public abstract void populate(TestItems<T> testItems);

    /**
     * Callback method for interacting with the {@link ComponentConfigDecorator} at the correct
     * time.<br>
     * This method provide <b>extension point</b> to prepare needed test environment for your
     * converter test.
     * For example :
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
    @Override
    public void configureComponents(final ComponentConfigDecorator decorator) {
        // default do nothing
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link UIComponent}, see
     * {@link Converter#getAsObject(FacesContext, UIComponent, String)}
     */
    @Test
    void shouldFailOnNullComponentOnGetAsObject() {
        assertThrows(NullPointerException.class, () -> getConverter().getAsObject(getFacesContext(), null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link FacesContext}, see
     * {@link Converter#getAsObject(FacesContext, UIComponent, String)}
     */
    @Test
    void shouldFailOnNullFacesContextOnGetAsObject() {
        assertThrows(NullPointerException.class, () -> getConverter().getAsObject(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link UIComponent}, see
     * {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullComponentOnGetAsString() {
        assertThrows(NullPointerException.class, () -> getConverter().getAsString(getFacesContext(), null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link FacesContext}, see
     * {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullFacesContextOnGetAsString() {
        assertThrows(NullPointerException.class, () -> getConverter().getAsString(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for the actual value, see
     * {@link Converter#getAsString(FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldReturnEmptyStringOnNullValue() {
        assertEquals("", getConverter().getAsString(getFacesContext(), getComponent(), null));
    }

    /**
     * Core test for converter testing. It collects the test-data using
     * {@link TestItems} and iterates them for the individual test. For each
     * element there will be called the method
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)},
     * with the result again
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * with the result being checked against the initial value.
     */
    @Test
    void shouldRoundTripValidData() {
        for (final String value : getTestItems().getRoundtripValues()) {
            final var converted =
                (T) getConverter().getAsObject(getFacesContext(), getComponent(), value);
            final var roundTripped =
                getConverter().getAsString(getFacesContext(), getComponent(), converted);
            assertEquals(value, roundTripped);
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)}
     * with invalid objects, derived by {@link TestItems}
     */
    @Test
    void shouldFailOnInvalidObjects() {
        for (final ConverterTestItem<T> item : getTestItems().getInvalidObjectTestItems()) {
            try {
                getConverter().getAsString(getFacesContext(), getComponent(), item.getTestValue());
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
     * Tests the method {@link Converter#getAsString(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, Object)} with valid objects, derived by {@link TestItems}
     */
    @Test
    void shouldPassOnValidObjects() {
        for (final ConverterTestItem<T> item : getTestItems().getValidObjectTestItems()) {
            final var result =
                getConverter().getAsString(getFacesContext(), getComponent(), item.getTestValue());
            if (null != item.getStringValue()) {
                assertEquals(item.getStringValue(), result, item.toString());
            }
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * with invalid objects, derived by {@link TestItems}
     */
    @Test
    void shouldFailOnInvalidStrings() {
        for (final ConverterTestItem<T> item : getTestItems().getInvalidStringTestItems()) {
            try {
                getConverter().getAsObject(getFacesContext(), getComponent(), item.getStringValue());
                fail(SHOULD_HAVE_THROWN_CONVERTER_EXCEPTION + item);
            } catch (final ConverterException e) {
                verifyExpectedErrorMessage(item, e);
            }
        }
    }

    /**
     * Tests the method
     * {@link Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String)}
     * with valid String, derived by {@link TestItems}
     */
    @Test
    void shouldPassOnValidStrings() {
        for (final ConverterTestItem<T> item : getTestItems().getValidStringTestItems()) {
            final var result = (T) getConverter().getAsObject(getFacesContext(), getComponent(),
                    item.getStringValue());
            if (null != item.getTestValue()) {
                assertEquals(item.getTestValue(), result, item.toString());
            }

        }
    }
}
