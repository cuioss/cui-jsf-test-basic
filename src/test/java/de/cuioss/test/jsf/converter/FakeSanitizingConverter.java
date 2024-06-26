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

import de.cuioss.test.jsf.mocks.ReverseConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Oliver Wolff
 *
 */
public class FakeSanitizingConverter implements Converter<String> {

    @Getter
    @Setter
    private boolean fakeEscaping = true;

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        requireNonNull(component);
        requireNonNull(context);
        if (null == value) {
            return "";
        }
        if (fakeEscaping) {
            return new ReverseConverter().getAsObject(context, component, value);
        }
        return value;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, String value) {
        requireNonNull(component);
        requireNonNull(context);
        if (null == value) {
            return "";
        }
        if (fakeEscaping) {
            return new ReverseConverter().getAsObject(context, component, value);
        }
        return value;
    }

}
