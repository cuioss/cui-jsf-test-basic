package de.cuioss.test.jsf.mocks;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import javax.faces.component.search.ComponentNotFoundException;
import javax.faces.component.search.SearchExpressionHint;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.util.ConfigurableFacesTest;

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
