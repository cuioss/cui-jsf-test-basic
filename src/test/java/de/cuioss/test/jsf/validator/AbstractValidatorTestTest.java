package de.cuioss.test.jsf.validator;

import javax.faces.validator.LengthValidator;

/**
 * @author Oliver Wolff
 *
 */
class AbstractValidatorTestTest extends AbstractValidatorTest<LengthValidator, String> {

    @Override
    public void populate(final TestItems<String> testItems) {
        testItems.addValid("1").addValid("abc").addInvalidWithMessage("123456",
                LengthValidator.MAXIMUM_MESSAGE_ID);
    }

    @Override
    public void configure(final LengthValidator validator) {
        validator.setMaximum(5);
    }
}
