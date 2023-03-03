package de.cuioss.test.jsf.converter;

import de.cuioss.test.jsf.validator.TestItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Extension to {@link TestItem} that contains the the additional field "stringValue"
 *
 * @author Oliver Wolff
 * @param <T> is the Type of value which will be checked
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConverterTestItem<T> extends TestItem<T> {

    @Getter
    @Setter
    private String stringValue;
}
