package de.cuioss.test.jsf.generator;

import javax.faces.convert.Converter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple helper class that bundles runtime information for a certain {@link Converter}, including
 * the targetType and the converterId
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
