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

import jakarta.faces.validator.LengthValidator;

/**
 * @author Oliver Wolff
 *
 */
class AbstractValidatorTestTest extends AbstractValidatorTest<LengthValidator, String> {

    @Override
    public void populate(final TestItems<String> testItems) {
        testItems.addValid("1").addValid("abc").addInvalidWithMessage("123456", LengthValidator.MAXIMUM_MESSAGE_ID);
    }

    @Override
    public void configure(final LengthValidator validator) {
        validator.setMaximum(5);
    }
}
