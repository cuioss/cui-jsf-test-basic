package de.icw.cui.test.jsf.generator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;

/**
 * {@link TypedGenerator} for {@link Severity}
 *
 * @author Oliver Wolff
 *
 */
public class MessageSeverityGenerator implements TypedGenerator<Severity> {

    private final TypedGenerator<Severity> severities = Generators.fixedValues(FacesMessage.SEVERITY_ERROR,
            FacesMessage.SEVERITY_FATAL, FacesMessage.SEVERITY_INFO, FacesMessage.SEVERITY_WARN);

    @Override
    public Class<Severity> getType() {
        return Severity.class;
    }

    @Override
    public Severity next() {
        return severities.next();
    }

}
