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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Mock Variant of {@link ContextCallback}.
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockContextCallback implements ContextCallback {

    private int called = 0;

    @Override
    public void invokeContextCallback(FacesContext context, UIComponent target) {
        called++;
    }

    /**
     * Checks whether callback has been called at least one time
     */
    public void assertCalledAtLeastOnce() {
        assertTrue(called > 0, "Has not been called at all");
    }

    /**
     * Checks whether callback has been called at least one time
     */
    public void assertNotCalledAtAll() {
        assertEquals(0, called, "Has been called " + called + " times");
    }

}
