/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.generator;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;

/**
 * {@link TypedGenerator} for {@link Severity}
 *
 * @author Oliver Wolff
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
