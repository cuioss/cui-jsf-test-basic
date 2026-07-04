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
package de.cuioss.test.jsf.renderer.util;

import org.jdom2.Attribute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AttributeComparator")
class AttributeComparatorTest {

    private final AttributeComparator comparator = new AttributeComparator();

    @Test
    @DisplayName("Should order attributes by their name")
    void shouldCompareByName() {
        var a = new Attribute("alpha", "1");
        var b = new Attribute("beta", "2");

        assertTrue(comparator.compare(a, b) < 0, "'alpha' should sort before 'beta'");
        assertTrue(comparator.compare(b, a) > 0, "'beta' should sort after 'alpha'");
        assertEquals(0, comparator.compare(a, new Attribute("alpha", "other")),
            "Attributes with the same name should be considered equal");
    }

    @Test
    @DisplayName("Should sort a list of attributes by name")
    void shouldSortByName() {
        List<Attribute> attributes = new ArrayList<>(List.of(
            new Attribute("zulu", "1"), new Attribute("mike", "2"), new Attribute("alpha", "3")));

        attributes.sort(comparator);

        assertEquals(List.of("alpha", "mike", "zulu"), attributes.stream().map(Attribute::getName).toList(),
            "Attributes should be sorted alphabetically by name");
    }
}
