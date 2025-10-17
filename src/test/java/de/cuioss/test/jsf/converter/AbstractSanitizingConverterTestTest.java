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
        // Not testing this

    }

    @Override
    @Test
    protected void shouldSanitizeJavaScript(FacesContext facesContext) {
        // ignore, the tests are separate;
    }

    @Test
    void shouldDetectInvalidEscaping(FacesContext facesContext) {
        super.getConverter().setFakeEscaping(false);
        assertThrows(AssertionError.class, () -> super.shouldSanitizeJavaScript(facesContext));
    }

    @Test
    void shouldDetectValidEscaping(FacesContext facesContext) {
        super.getConverter().setFakeEscaping(true);
        assertDoesNotThrow(() -> super.shouldSanitizeJavaScript(facesContext));
    }

}
