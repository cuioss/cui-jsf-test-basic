package de.icw.cui.test.jsf.support.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@SuppressWarnings("javadoc")
@FacesValidator(ValidatorWithAnnotation.VALIDATOR_ID)
public class ValidatorWithAnnotation implements Validator {

    public static final String VALIDATOR_ID =
        "de.icw.cui.test.jsf.context.support.ValidatorWithAnnotation";

    @Override
    public void validate(final FacesContext context, final UIComponent component,
            final Object value)
        throws ValidatorException {

    }

}
