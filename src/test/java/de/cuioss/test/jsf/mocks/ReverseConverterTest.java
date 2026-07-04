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
package de.cuioss.test.jsf.mocks;

import de.cuioss.test.jsf.converter.AbstractConverterTest;
import de.cuioss.test.jsf.converter.TestItems;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReverseConverterTest extends AbstractConverterTest<ReverseConverter, String> {

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addRoundtripValues("123");
    }

    @Test
    void getAsObjectShouldReturnNullForNullOrEmptyInput(FacesContext facesContext) {
        var converter = new ReverseConverter();
        var component = new HtmlInputText();

        // MOCK-9: getAsObject must return null for null/empty input
        assertNull(converter.getAsObject(facesContext, component, null), "null input must convert to null");
        assertNull(converter.getAsObject(facesContext, component, ""), "empty input must convert to null");
        assertEquals("cba", converter.getAsObject(facesContext, component, "abc"),
            "non-empty input must be reversed");
    }
}
