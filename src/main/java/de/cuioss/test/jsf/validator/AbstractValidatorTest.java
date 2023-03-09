package de.cuioss.test.jsf.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.junit5.JsfEnabledTestEnvironment;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.Getter;

/**
 * Base Class for testing implementations of {@link Validator} within a Junit 5 context
 * <h3>Setup</h3>
 * <p>
 * It uses {@link EnableJsfEnvironment}, for the basic test-infrastructure.See the
 * class-documentation for details.
 * </p>
 * <p>
 * {@link #initValidator()}: Instantiates the concrete {@link Validator} using reflection. After
 * this the method calls {@link #configure(Object)} that can be used for further
 * configuration of the {@link Validator}
 * </p>
 * <p>
 * In case you want you want to create the {@link Validator} yourself you can overwrite
 * {@link #getValidator()}
 * </p>
 * <p>
 * In case you want a pass another {@link UIComponent} as parameter to the {@link Validator} you can
 * overwrite {@link #getComponent()}
 * </p>
 * <h3>Test-Methods</h3>
 * <p>
 * The core test-methods are {@link #shouldFailOnInvalidTestdata()} and
 * {@link #shouldHandleValidTestdata()}. They call {@link #populate(TestItems)} in oder to
 * create corresponding test-data. The implementation is in the actual test-class.
 * </p>
 * <h3>API-Test</h3>
 * <p>
 * The api as defined within {@link Validator} is tested with the methods
 * {@link #shouldFailOnNullComponent()}, {@link #shouldFailOnNullFacesContext()} and
 * {@link #shouldHandleNullValue()}
 * </p>
 * <h3>Example</h3>
 *
 * <pre>
 * <code>
 * &#64;EnableJsfEnvironment
 * class LengthValidatorTest extends AbstractValidatorTest&lt;LengthValidator, String&gt; {

    &#64;Override
    public void populate(final TestItems&lt;String&gt; testItems) {
        testItems.addValid("1").addValid("abc").addInvalidWithMessage("123456",
                LengthValidator.MAXIMUM_MESSAGE_ID);
    }

    &#64;Override
    public void configure(final LengthValidator validator) {
        validator.setMaximum(5);
    }
   }
 * </code>
 * </pre>
 * <p>
 *
 * @author Oliver Wolff
 * @param <V> identifying the concrete {@link Validator} to be tested.
 * @param <T> identifying the type of elements to be passed into the validator
 */
@SuppressWarnings({ "rawtypes", "unchecked" }) // owolff we need to migrate this aspect later
@EnableGeneratorController
public abstract class AbstractValidatorTest<V extends Validator, T> extends JsfEnabledTestEnvironment
        implements ConfigurationCallBackHandler<V> {

    @Getter
    private final UIComponent component = new HtmlInputText();

    @Getter
    private V validator;

    /**
     * Instantiates and initially configures a concrete {@link Validator}
     */
    @BeforeEach
    void initValidator() {
        final Class<V> klazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        validator = new DefaultInstantiator<>(klazz).newInstance();
        configure(validator);
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link UIComponent}, see
     * {@link Validator#validate(javax.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullComponent() {
        assertThrows(NullPointerException.class, () -> getValidator().validate(getFacesContext(), null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for {@link FacesContext}, see
     * {@link Validator#validate(javax.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullFacesContext() {
        assertThrows(NullPointerException.class, () -> getValidator().validate(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for the actual value, see
     * {@link Validator#validate(javax.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldHandleNullValue() {
        getValidator().validate(getFacesContext(), getComponent(), null);
    }

    /**
     * Tests whether the valid {@link TestItems} are validated without
     * {@link ValidatorException}
     */
    @Test
    void shouldHandleValidTestdata() {
        final var items = new TestItems<T>();
        populate(items);
        items.allValid().forEach(
                item -> validator.validate(getFacesContext(), getComponent(), item.getTestValue()));
    }

    /**
     * Tests whether the invalid {@link TestItems} fail to validate by throwing
     * {@link ValidatorException}. In case the single {@link TestItems} provide a message,
     * it will be compared as well.
     */
    @Test
    void shouldFailOnInvalidTestdata() {
        final var items = new TestItems<T>();
        populate(items);
        for (final TestItem<T> item : items.allInvalid()) {
            try {
                validator.validate(getFacesContext(), getComponent(), item.getTestValue());
                fail("Validation should have thrown a ValidatorException for testValue" + item);
            } catch (final ValidatorException e) {
                assertEquals(FacesMessage.SEVERITY_ERROR, e.getFacesMessage().getSeverity());
                if (null != item.getErrorMessage()) {
                    assertEquals(
                            item.getErrorMessage(), e.getFacesMessage().getSummary(),
                            "The validation failed as expected, but the messages are not equal as expected");
                }
            }
        }
    }

    /**
     * Populates the test-items
     *
     * @param testItems to be populated, is never null
     */
    public abstract void populate(TestItems<T> testItems);

}
