package de.cuioss.test.jsf.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.faces.convert.Converter;

import org.junit.jupiter.api.Test;

/**
 * Extension of {@linkplain AbstractConverterTest} to also test the sanitizing inside the getAsString function.
 *
 * @param <C> identifying the concrete {@link Converter} to be tested.
 * @param <T> identifying the type of elements to be used for values to be given to the
 *            {@link Converter}
 */
public abstract class AbstractSanitizingConverterTest<C extends Converter, T> extends AbstractConverterTest<C, T> {

    /**
     * Create an instance of the object containing a given malicious content that is to be converted into a string by this converter.
     *
     * @param content
     * @return
     */
    protected abstract T createTestObjectWithMaliciousContent(String content);

    @Test
    void shouldSanitizeJavaScript() {
        var toConvert = createTestObjectWithMaliciousContent("<script>");
        var result = getConverter().getAsString(getFacesContext(), getComponent(), toConvert);
        assertFalse(result.contains("<script"));
    }

}
