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
package de.cuioss.test.jsf.renderer;

import jakarta.faces.component.UIComponent;
import org.jdom2.Element;

/**
 * This interface defines the testing of implicit single attribute contracts.
 *
 * @author Oliver Wolff
 */
public interface RendererAttributeAssert {

    /**
     * @return the name of the attribute to be checked
     */
    String getAttributeName();

    /**
     * Sets the attribute to be tested into the given component
     *
     * @param component where the attribute is to be applied to,
     */
    void applyAttribute(UIComponent component);

    /**
     * Asserts in the given render-result that the attribute was set properly
     *
     * @param element to be checked
     */
    void assertAttributeSet(Element element);
}
