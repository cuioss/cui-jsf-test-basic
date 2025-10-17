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
package de.cuioss.test.jsf.validator;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base Class for testing implementations of {@link Validator} within a Junit 5
 * context
 * <h3>Setup</h3>
 * <p>
 * It uses {@link EnableJsfEnvironment}, for the basic test-infrastructure.See
 * the class-documentation for details.
 * </p>
 * <p>
 * {@link #initValidator()}: Instantiates the concrete {@link Validator} using
 * reflection. After this the method calls {@link #configure(Object)} that can
 * be used for further configuration of the {@link Validator}
 * </p>
 * <p>
 * In case you want you want to create the {@link Validator} yourself you can
 * overwrite the getValidator() method
 * </p>
 * <p>
 * In case you want a pass another {@link UIComponent} as parameter to the
 * {@link Validator} you can overwrite the getComponent() method
 * </p>
 * <h3>Test-Methods</h3>
 * <p>
 * The core test-methods are {@link #shouldFailOnInvalidTestdata(FacesContext)} and
 * {@link #shouldHandleValidTestdata(FacesContext)}. They call {@link #populate(TestItems)}
 * in oder to create corresponding test-data. The implementation is in the
 * actual test-class.
 * </p>
 * <h3>API-Test</h3>
 * <p>
 * The api as defined within {@link Validator} is tested with the methods
 * {@link #shouldFailOnNullComponent(FacesContext)}, {@link #shouldFailOnNullFacesContext()}
 * and {@link #shouldHandleNullValue(FacesContext)}
 * </p>
 * <h3>Example</h3>
 *
 * <pre>
 * <code>
 * &#64;EnableJsfEnvironment
 * class LengthValidatorTest extends AbstractValidatorTest&lt;LengthValidator, String&gt; {
 *
 * &#64;Override
 * public void populate(final TestItems&lt;String&gt; testItems) {
 * testItems.addValid("1").addValid("abc").addInvalidWithMessage("123456",
 * LengthValidator.MAXIMUM_MESSAGE_ID);
 * }
 *
 * &#64;Override
 * public void configure(final LengthValidator validator) {
 * validator.setMaximum(5);
 * }
 * }
 * </code>
 * </pre>
 *
 * @param <V> identifying the concrete {@link Validator} to be tested.
 * @param <T> identifying the type of elements to be passed into the validator
 * @author Oliver Wolff
 */
@SuppressWarnings({"rawtypes", "unchecked"})
// owolff we need to migrate this aspect later
@EnableGeneratorController
@EnableJsfEnvironment
public abstract class AbstractValidatorTest<V extends Validator, T>
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
        final Class<V> clazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        validator = new DefaultInstantiator<>(clazz).newInstance();
        configure(validator);
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link UIComponent}, see
     * {@link Validator#validate(jakarta.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullComponent(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> getValidator().validate(facesContext, null, null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for
     * {@link FacesContext}, see
     * {@link Validator#validate(jakarta.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldFailOnNullFacesContext() {
        assertThrows(NullPointerException.class, () -> getValidator().validate(null, getComponent(), null));
    }

    /**
     * Checks the api contract regarding {@code null} as parameter for the actual
     * value, see
     * {@link Validator#validate(jakarta.faces.context.FacesContext, UIComponent, Object)}
     */
    @Test
    void shouldHandleNullValue(FacesContext facesContext) {
        getValidator().validate(facesContext, getComponent(), null);
    }

    /**
     * Tests whether the valid {@link TestItems} are validated without
     * {@link ValidatorException}
     */
    @Test
    void shouldHandleValidTestdata(FacesContext facesContext) {
        final var items = new TestItems<T>();
        populate(items);
        items.allValid().forEach(item -> validator.validate(facesContext, getComponent(), item.getTestValue()));
    }

    /**
     * Tests whether the invalid {@link TestItems} fail to validate by throwing
     * {@link ValidatorException}. In case the single {@link TestItems} provide a
     * message, it will be compared as well.
     */
    @Test
    void shouldFailOnInvalidTestdata(FacesContext facesContext) {
        final var items = new TestItems<T>();
        populate(items);
        for (final TestItem<T> item : items.allInvalid()) {
            try {
                validator.validate(facesContext, getComponent(), item.getTestValue());
                fail("Validation should have thrown a ValidatorException for testValue" + item);
            } catch (final ValidatorException e) {
                assertEquals(FacesMessage.SEVERITY_ERROR, e.getFacesMessage().getSeverity());
                if (null != item.getErrorMessage()) {
                    assertEquals(item.getErrorMessage(), e.getFacesMessage().getSummary(),
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
