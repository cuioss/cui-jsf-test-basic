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
package de.cuioss.test.jsf.support.componentproperty;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

@VerifyComponentProperties(of = {"someProperty"})
public class MultiValuedComponentWithInvalidELHandling extends MultiValuedComponent {

    private static final String INVALID_VE = "invalidVE";

    public void setSomeProperty(final String someProperty) {
        getStateHelper().put(INVALID_VE, someProperty);
    }

    public String getSomeProperty() {
        return (String) getStateHelper().eval(INVALID_VE);
    }
}
