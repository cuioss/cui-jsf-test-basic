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
package de.cuioss.test.jsf.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractSanitizingConverterTestTest extends AbstractSanitizingConverterTest<FakeSanitizingConverter, String> {

    @Override
    protected String createTestObjectWithMaliciousContent(String content) {
        return content;
    }

    @Override
    public void populate(TestItems<String> testItems) {
        // TODO Auto-generated method stub

    }

    @Override
    @Test
    protected void shouldSanitizeJavaScript() {
        // ignore, the tests are separate;
    }

    @Test
    void shouldDetectInvalidEscaping() {
        super.getConverter().setFakeEscaping(false);
        assertThrows(AssertionError.class, super::shouldSanitizeJavaScript);
    }

    @Test
    void shouldDetectValidEscaping() {
        super.getConverter().setFakeEscaping(true);
        assertDoesNotThrow(super::shouldSanitizeJavaScript);
    }

}
