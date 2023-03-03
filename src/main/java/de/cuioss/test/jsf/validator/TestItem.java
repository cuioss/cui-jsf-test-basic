package de.cuioss.test.jsf.validator;

import javax.faces.application.FacesMessage.Severity;

import lombok.Data;

/**
 * Object which represents one Test Item which will be checked by
 *
 * @author i000576
 * @param <T> is the Type of value which will be checked
 */
@Data
public class TestItem<T> {

    private boolean valid;

    private T testValue;

    private String errorMessage;

    private Severity severity;

}
