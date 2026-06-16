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

import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
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
        // No TestItems are required: this self-test exercises the sanitizing
        // assertions of the base class directly via shouldSanitizeJavaScript.
    }

    @Override
    @Test
    protected void shouldSanitizeJavaScript(FacesContext facesContext) {
        // Disabled here: the inherited sanitizing assertion is exercised explicitly
        // from shouldDetectInvalidEscaping / shouldDetectValidEscaping instead.
    }

    @Test
    @DisplayName("Should fail the sanitizing assertion when escaping is disabled")
    void shouldDetectInvalidEscaping(FacesContext facesContext) {
        getConverter().setFakeEscaping(false);

        assertThrows(AssertionError.class, () -> super.shouldSanitizeJavaScript(facesContext),
            "Sanitizing assertion must fail when the converter does not escape");
    }

    @Test
    @DisplayName("Should pass the sanitizing assertion when escaping is enabled")
    void shouldDetectValidEscaping(FacesContext facesContext) {
        getConverter().setFakeEscaping(true);

        assertDoesNotThrow(() -> super.shouldSanitizeJavaScript(facesContext),
            "Sanitizing assertion must pass when the converter escapes");
    }

}
