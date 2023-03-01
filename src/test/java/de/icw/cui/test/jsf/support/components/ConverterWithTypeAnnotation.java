package de.icw.cui.test.jsf.support.components;

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
