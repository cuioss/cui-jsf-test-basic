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
package de.cuioss.test.jsf.generator;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import jakarta.faces.convert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * A {@link TypedGenerator} providing the converter classes shipped with JSF. The
 * available descriptors are exposed via {@link #JSF_CONVERTER} and the accompanying
 * generators.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("rawtypes")
public class JsfProvidedConverter implements TypedGenerator<Class> {

    /**
     * Defines all converter types provided by JSF.
     */
    @SuppressWarnings("java:S2386") // owolff: false positive -> immutableList
    public static final List<ConverterDescriptor> JSF_CONVERTER = immutableList(
        new ConverterDescriptor(BigDecimalConverter.class, BigDecimal.class, BigDecimalConverter.CONVERTER_ID),
        new ConverterDescriptor(BigIntegerConverter.class, BigInteger.class, BigIntegerConverter.CONVERTER_ID),
        new ConverterDescriptor(BooleanConverter.class, Boolean.class, BooleanConverter.CONVERTER_ID),
        new ConverterDescriptor(ByteConverter.class, Byte.class, ByteConverter.CONVERTER_ID),
        new ConverterDescriptor(CharacterConverter.class, Character.class, CharacterConverter.CONVERTER_ID),
        new ConverterDescriptor(DateTimeConverter.class, Date.class, DateTimeConverter.CONVERTER_ID),
        new ConverterDescriptor(DoubleConverter.class, Double.class, DoubleConverter.CONVERTER_ID),
        new ConverterDescriptor(EnumConverter.class, Enum.class, EnumConverter.CONVERTER_ID),
        new ConverterDescriptor(FloatConverter.class, Float.class, FloatConverter.CONVERTER_ID),
        new ConverterDescriptor(IntegerConverter.class, Integer.class, IntegerConverter.CONVERTER_ID),
        new ConverterDescriptor(LongConverter.class, Long.class, LongConverter.CONVERTER_ID),
        new ConverterDescriptor(NumberConverter.class, Number.class, NumberConverter.CONVERTER_ID),
        new ConverterDescriptor(ShortConverter.class, Short.class, ShortConverter.CONVERTER_ID));

    /**
     * Defines a generator for all converter types provided by JSF.
     */
    public static final TypedGenerator<ConverterDescriptor> CONVERTER_CLASS_GENERATOR = Generators
        .fixedValues(JSF_CONVERTER);

    /**
     * Defines a generator for all converter types provided by JSF.
     *
     * @deprecated misspelled; use {@link #CONVERTER_CLASS_GENERATOR}. Retained for one
     * release and scheduled for removal in the next major.
     */
    @Deprecated
    public static final TypedGenerator<ConverterDescriptor> CONVERTER_CLASS_GERNERATOR = CONVERTER_CLASS_GENERATOR;

    /**
     * A generator for every id of registered generator.
     */
    public static final TypedGenerator<String> CONVERTER_ID_GENERATOR = Generators
        .fixedValues(JSF_CONVERTER.stream().map(ConverterDescriptor::getConverterId).toList());

    /**
     * A generator for a subset of types a generator is registered as default.
     */
    public static final TypedGenerator<Class<?>> TARGET_TYPE_GENERATOR = Generators
        .fixedValues(immutableList(Integer.class, Double.class, Float.class, Boolean.class, BigInteger.class));

    @SuppressWarnings("unchecked")
    @Override
    public Class<Converter> next() {
        return (Class<Converter>) CONVERTER_CLASS_GENERATOR.next().getConverterClass();
    }

    @Override
    public Class<Class> getType() {
        return Class.class;
    }

}
