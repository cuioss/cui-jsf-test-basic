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

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.component.search.ComponentNotFoundException;
import jakarta.faces.component.search.SearchExpressionHint;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;
import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@DisplayName("CuiMockSearchExpressionHandler")
class CuiSearchExpressionHandlerMockTest {

    private static final String EXPRESSION = "expression";

    @Test
    @DisplayName("Should be provided by the JSF test setup")
    void shouldBeProvided(FacesContext facesContext) {
        assertNotNull(CuiMockSearchExpressionHandler.retrieve(facesContext),
            "Handler should be registered by the test setup");
    }

    @Test
    @DisplayName("Should resolve to nothing when the ignore-no-result hint is set")
    void shouldIgnoreHint(FacesContext facesContext) {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), facesContext,
            Set.of(), mutableSet(SearchExpressionHint.IGNORE_NO_RESULT));
        var callback = new CuiMockContextCallback();

        assertNull(new CuiMockSearchExpressionHandler().resolveClientId(context, EXPRESSION),
            "resolveClientId should return null when no result is ignored");
        assertTrue(new CuiMockSearchExpressionHandler().resolveClientIds(context, EXPRESSION).isEmpty(),
            "resolveClientIds should be empty when no result is ignored");

        new CuiMockSearchExpressionHandler().resolveComponent(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();

        new CuiMockSearchExpressionHandler().resolveComponents(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();
    }

    @Test
    @DisplayName("Should fail when no component is found and the hint is missing")
    void shouldFailOnMissingHint(FacesContext facesContext) {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), facesContext,
            Set.of(), Set.of());
        var handler = new CuiMockSearchExpressionHandler();
        var callback = new CuiMockContextCallback();

        assertThrows(ComponentNotFoundException.class,
            () -> handler.resolveComponents(context, EXPRESSION, callback),
            "Missing component without ignore hint should throw");
    }

    @Test
    @DisplayName("Should invoke the callback for resolved components")
    void shouldCallBack(FacesContext facesContext) {
        var context = new CuiMockSearchExpressionContext(new CuiMockComponent(), facesContext,
            Set.of(), Set.of());
        var callback = new CuiMockContextCallback();
        var component = new CuiMockComponent();

        var handler = new CuiMockSearchExpressionHandler();
        handler.setResolvedComponent(component);

        handler.resolveComponent(context, EXPRESSION, callback);
        callback.assertCalledAtLeastOnce();

        var componentsCallback = new CuiMockContextCallback();
        var componentsHandler = new CuiMockSearchExpressionHandler();
        componentsHandler.setResolvedComponents(mutableList(component));

        componentsHandler.resolveComponents(context, EXPRESSION, componentsCallback);
        componentsCallback.assertCalledAtLeastOnce();
    }
}
