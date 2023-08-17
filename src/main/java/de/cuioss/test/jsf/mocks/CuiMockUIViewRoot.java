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
package de.cuioss.test.jsf.mocks;

import static de.cuioss.tools.collect.CollectionLiterals.mutableMap;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/**
 * Mock variant of {@link UIViewRoot} providing a heloer for adding a
 * {@link UIComponent} at runtime
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockUIViewRoot extends UIViewRoot {

    private final Map<String, UIComponent> componentMap = mutableMap();

    /**
     * @param expr      must not be null
     * @param component must not be null
     */
    public void addUiComponent(String expr, UIComponent component) {
        componentMap.put(expr, component);
    }

    @Override
    public UIComponent findComponent(String expr) {
        if (componentMap.containsKey(expr)) {
            return componentMap.get(expr);
        }
        return super.findComponent(expr);
    }
}
