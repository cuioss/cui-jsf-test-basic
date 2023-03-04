package de.cuioss.test.jsf.mocks;

import static java.util.Objects.requireNonNull;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

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
    public String getAsObject(final FacesContext context, final UIComponent component,
            final String value) {
        return reverse(context, component, value);
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component,
            final String value) {
        return reverse(context, component, value);
    }

    private String reverse(final FacesContext context, final UIComponent component, final String value) {
        requireNonNull(component);
        requireNonNull(context);
        if (null == value) {
            return "";
        }
        return new StringBuilder(value).reverse().toString();
    }

}
