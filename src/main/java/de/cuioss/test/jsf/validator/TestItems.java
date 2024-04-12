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
package de.cuioss.test.jsf.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.validator.ValidatorException;

import java.util.ArrayList;
import java.util.List;

/**
 * TestData Store for Test Items which will be used by
 * {@link AbstractValidatorTest}-<br>
 * Class is prepared to be used as Fluent Interface
 *
 * @param <T> type of Test Item value
 */
public class TestItems<T> {

    private final List<TestItem<T>> localItems = new ArrayList<>();

    /**
     * Add to TestData Store a Test item which must fail with
     * {@link ValidatorException}
     *
     * @param value T invalid value which should cause a {@link ValidatorException}
     * @return TestItems reference to this object
     */
    public TestItems<T> addInvalid(final T value) {
        return this.addItem(false, value, FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Add to TestData Store a Test item which must fail with
     * {@link ValidatorException}
     *
     * @param value   T invalid value which should cause a
     *                {@link ValidatorException}
     * @param message which should be set within the {@link ValidatorException}
     * @return TestItems reference to this object
     */
    public TestItems<T> addInvalidWithMessage(final T value, final String message) {
        return this.addItem(false, value, FacesMessage.SEVERITY_ERROR, message);
    }

    /**
     * Add to TestData Store a Test item which must fail with
     * {@link ValidatorException}
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
     * @param valid   indicating whether it is a valid or invalid item
     * @param value   T value to be validated
     * @param level   {@link Severity} represent message severity, usually
     *                {@link FacesMessage#SEVERITY_ERROR}
     * @param message which should be set within the {@link ValidatorException}
     * @return TestItems reference to this object
     */
    private TestItems<T> addItem(final boolean valid, final T value, final Severity level, final String message) {
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
        return localItems.stream().filter(TestItem::isValid).toList();
    }

    /**
     * @return all invalid test-items
     */
    List<TestItem<T>> allInvalid() {
        return localItems.stream().filter(item -> !item.isValid()).toList();
    }
}
