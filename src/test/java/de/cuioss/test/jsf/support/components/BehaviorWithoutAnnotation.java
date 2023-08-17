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
package de.cuioss.test.jsf.support.components;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;

@SuppressWarnings("javadoc")
public class BehaviorWithoutAnnotation extends BehaviorBase implements ClientBehavior {

    public static final String BEHAVIOR_ID = "de.cuioss.test.jsf.support.BehaviorWithoutAnnotation";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        // not needed for test
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        // not needed for test
        return null;
    }

    @Override
    public String getScript(final ClientBehaviorContext behaviorContext) {
        // not needed for test
        return null;
    }
}
