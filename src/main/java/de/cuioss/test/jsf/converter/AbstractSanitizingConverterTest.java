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

import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Extension of {@linkplain AbstractConverterTest} to also test the sanitizing
 * inside the getAsString function.
 *
 * @param <C> identifying the concrete {@link Converter} to be tested.
 * @param <T> identifying the type of elements to be used for values to be given
 *            to the {@link Converter}
 */
public abstract class AbstractSanitizingConverterTest<C extends Converter<T>, T> extends AbstractConverterTest<C, T> {

    /**
     * Create an instance of the object containing a given malicious content that is
     * to be converted into a string by this converter.
     *
     * @param content
     * @return
     */
    protected abstract T createTestObjectWithMaliciousContent(String content);

    @Test
    protected void shouldSanitizeJavaScript(FacesContext facesContext) {
        var toConvert = createTestObjectWithMaliciousContent("<script>");
        var result = getConverter().getAsString(facesContext, getComponent(), toConvert);
        assertFalse(result.contains("<script"));
    }

}
