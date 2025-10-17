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

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares two {@link Attribute} elements by name
 *
 * @author Oliver Wolff
 */
public class AttributeComparator implements Comparator<Attribute>, Serializable {

    @Serial
    private static final long serialVersionUID = 2093668555465640881L;

    @Override
    public int compare(final Attribute o1, final Attribute o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
