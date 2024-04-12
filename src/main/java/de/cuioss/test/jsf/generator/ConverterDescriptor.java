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
package de.cuioss.test.jsf.generator;

import jakarta.faces.convert.Converter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple helper class that bundles runtime information for a certain
 * {@link Converter}, including the targetType and the converterId
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ConverterDescriptor {

    @Getter
    private final Class<? extends Converter> converterClass;

    @Getter
    private final Class<?> targetType;

    @Getter
    private final String converterId;

}
