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

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import jakarta.faces.convert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * Hybrid class that acts as an {@link TypedGenerator} and
 * {@link ComponentConfigurator}.
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
    public static final TypedGenerator<ConverterDescriptor> CONVERTER_CLASS_GERNERATOR = Generators
        .fixedValues(JSF_CONVERTER);

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
        return (Class<Converter>) CONVERTER_CLASS_GERNERATOR.next().getConverterClass();
    }

    @Override
    public Class<Class> getType() {
        return Class.class;
    }

}
