package de.icw.cui.test.jsf.support.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("javadoc")
@FacesConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID)
public class ConverterWithConverterIdAnnotation implements Converter {

    public static final String CONVERTER_ID =
        "de.icw.cui.test.jsf.context.support.ConverterWithConverterIdAnnotation";

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component,
            final String value) {
        return null;
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component,
            final Object value) {
        return null;
    }

}
