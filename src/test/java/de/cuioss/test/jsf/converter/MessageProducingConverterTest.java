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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import static java.util.Objects.requireNonNull;

/**
 * Regression test for ASSERT-6 / ASSERT-8: the converter failure loop must verify the
 * severity and message of the {@link ConverterException}'s {@link FacesMessage} (and
 * fail meaningfully rather than NPE if it is missing). This converter throws a properly
 * populated {@link FacesMessage} for the invalid input so the strengthened assertions in
 * {@link AbstractConverterTest} are exercised.
 */
class MessageProducingConverterTest
    extends AbstractConverterTest<MessageProducingConverterTest.MessageProducingConverter, String> {

    static final String INVALID = "bad";
    static final String MESSAGE_KEY = "some.converter.KEY";

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addValidString("ok")
            .addInvalidStringWithMessage(INVALID, MESSAGE_KEY);
    }

    /** Converter that raises a fully populated {@link ConverterException} for {@link #INVALID}. */
    public static class MessageProducingConverter implements Converter<String> {

        @Override
        public String getAsObject(FacesContext context, UIComponent component, String value) {
            requireNonNull(context);
            requireNonNull(component);
            if (INVALID.equals(value)) {
                throw new ConverterException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_KEY, MESSAGE_KEY));
            }
            return value;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, String value) {
            requireNonNull(context);
            requireNonNull(component);
            return value == null ? "" : value;
        }
    }
}
