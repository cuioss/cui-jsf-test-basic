package de.cuioss.test.jsf.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.validator.ValidatorException;

/**
 * TestData Store for Test Items which will be used by {@link AbstractValidatorTest}-<br/>
 * Class is prepared to be used as Fluent Interface
 *
 * @param <T> type of Test Item value
 */
public class TestItems<T> {

    private final List<TestItem<T>> localItems = new ArrayList<>();

    /**
     * Add to TestData Store a Test item which must fail with {@link ValidatorException}
     *
     * @param value T invalid value which should cause a {@link ValidatorException}
     * @return TestItems reference to this object
     */
    public TestItems<T> addInvalid(final T value) {
        return this.addItem(false, value, FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Add to TestData Store a Test item which must fail with {@link ValidatorException}
     *
     * @param value T invalid value which should cause a {@link ValidatorException}
     * @param message which should be set within the {@link ValidatorException}
     * @return TestItems reference to this object
     */
    public TestItems<T> addInvalidWithMessage(final T value, final String message) {
        return this.addItem(false, value, FacesMessage.SEVERITY_ERROR, message);
    }

    /**
     * Add to TestData Store a Test item which must fail with {@link ValidatorException}
     *
     * @param value T invalid value which should cause a {@link ValidatorException}
     * @return TestItems reference to this object
     */
    public TestItems<T> addValid(final T value) {
        return this.addItem(true, value, null, null);
    }

    /**
     * Add to TestData Store a Test item
     *
     * @param valid indicating whether it is a valid or invalid item
     * @param value T value to be validated
     * @param level {@link Severity} represent message severity, usually
     *            {@link FacesMessage#SEVERITY_ERROR}
     * @param message which should be set within the {@link ValidatorException}
     * @return TestItems reference to this object
     */
    private TestItems<T> addItem(final boolean valid, final T value, final Severity level,
            final String message) {
        final var item = new TestItem<T>();
        item.setTestValue(value);
        item.setValid(valid);
        item.setErrorMessage(message);
        item.setSeverity(level);
        localItems.add(item);
        return this;
    }

    /**
     * @return all valid test-items
     */
    List<TestItem<T>> allValid() {
        return localItems.stream().filter(TestItem::isValid).collect(Collectors.toList());
    }

    /**
     * @return all invalid test-items
     */
    List<TestItem<T>> allInvalid() {
        return localItems.stream().filter(item -> !item.isValid()).collect(Collectors.toList());
    }
}
