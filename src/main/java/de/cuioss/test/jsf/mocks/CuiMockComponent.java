/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.mocks;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;

/**
 * Simple Mock component to be used for cases where you need an "any-component"
 *
 * @author Oliver Wolff
 */
@FacesComponent(CuiMockComponent.COMPONENT_TYPE)
public class CuiMockComponent extends UIComponentBase {

    /**
     * "mockFamily"
     */
    public static final String FAMILY = "mockFamily";

    /**
     * "mockRendererType"
     */
    public static final String RENDERER_TYPE = "mockRendererType";

    /**
     * "mockComponentType"
     */
    public static final String COMPONENT_TYPE = "mockComponentType";

    @Override
    public String getFamily() {
        return FAMILY;
    }

    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }

}
