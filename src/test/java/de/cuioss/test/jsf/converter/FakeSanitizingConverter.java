package de.cuioss.test.jsf.converter;

import static java.util.Objects.requireNonNull;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import de.cuioss.test.jsf.mocks.ReverseConverter;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Oliver Wolff
 *
 */
public class FakeSanitizingConverter implements Converter<String> {

    @Getter
    @Setter
    private boolean fakeEscaping = true;

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        requireNonNull(component);
        requireNonNull(context);
        if (null == value) {
            return "";
        }
        if (fakeEscaping) {
            return new ReverseConverter().getAsObject(context, component, value);
        }
        return value;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, String value) {
        requireNonNull(component);
        requireNonNull(context);
        if (null == value) {
            return "";
        }
        if (fakeEscaping) {
            return new ReverseConverter().getAsObject(context, component, value);
        }
        return value;
    }

}
