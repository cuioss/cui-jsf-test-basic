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
package de.cuioss.test.jsf.support.components;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("javadoc")
@FacesConverter(forClass = Serializable.class)
public class ConverterWithTypeAnnotation implements Converter {

    public static final Class<?> FOR_CLASS = Serializable.class;

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
        return null;
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
        return null;
    }

}
