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

import de.cuioss.test.jsf.util.ConfigurableFacesTest;
import jakarta.faces.component.search.ComponentNotFoundException;
import jakarta.faces.component.search.SearchExpressionHint;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;
import static org.junit.jupiter.api.Assertions.*;

class CuiSearchExpressionHandlerMockTest extends ConfigurableFacesTest {

    private static final String EXPRESSION = "expression";

    @Test
    void shouldBeProvided() {
        assertNotNull(CuiMockSearchExpressionHandler.retrieve(getFacesContext()));
    }

    @Test
    void shouldIgnoreHint() {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
            Collections.emptySet(), mutableSet(SearchExpressionHint.IGNORE_NO_RESULT));
        var callback = new CuiMockContextCallback();

        assertNull(new CuiMockSearchExpressionHandler().resolveClientId(context, EXPRESSION));
        assertTrue(new CuiMockSearchExpressionHandler().resolveClientIds(context, EXPRESSION).isEmpty());

        new CuiMockSearchExpressionHandler().resolveComponent(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();

        new CuiMockSearchExpressionHandler().resolveComponents(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();
    }

    @Test
    void shouldFailOnMissingHint() {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
            Collections.emptySet(), Collections.emptySet());
        var handler = new CuiMockSearchExpressionHandler();
        var callback = new CuiMockContextCallback();
        assertThrows(ComponentNotFoundException.class, () -> handler.resolveComponents(context, EXPRESSION, callback));
    }

    @Test
    void shouldCallBack() {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
            Collections.emptySet(), Collections.emptySet());
        var callback = new CuiMockContextCallback();

        var component = new CuiMockComponent();

        var handler = new CuiMockSearchExpressionHandler();
        handler.setResolvedComponent(component);

        handler.resolveComponent(context, EXPRESSION, callback);
        callback.assertCalledAtLeastOnce();

        callback = new CuiMockContextCallback();
        handler = new CuiMockSearchExpressionHandler();
        handler.setResolvedComponents(mutableList(component));

        handler.resolveComponents(context, EXPRESSION, callback);
        callback.assertCalledAtLeastOnce();
    }
}
