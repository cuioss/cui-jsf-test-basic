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

import de.cuioss.tools.string.MoreStrings;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import static java.util.Objects.requireNonNull;

/**
 * For testing converter
 *
 * @author Matthias Walliczek
 */
@FacesConverter(ReverseConverter.CONVERTER_ID)
public class ReverseConverter implements Converter<String> {

    /**
     * <p>
     * The standard converter id for this converter.
     * </p>
     */
    public static final String CONVERTER_ID = "de.cuioss.test.jsf.mocks.ReserveConverter";

    @Override
    public String getAsObject(final FacesContext context, final UIComponent component, final String value) {
        requireNonNull(component);
        requireNonNull(context);
        // Converter contract: return null for null/empty input.
        if (MoreStrings.isEmpty(value)) {
            return null;
        }
        return reverse(value);
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component, final String value) {
        requireNonNull(component);
        requireNonNull(context);
        // Converter contract: return a zero-length String for a null value.
        if (null == value) {
            return "";
        }
        return reverse(value);
    }

    private static String reverse(final String value) {
        return new StringBuilder(value).reverse().toString();
    }

}
