package de.icw.cui.test.jsf.mocks;

import static io.cui.tools.collect.CollectionLiterals.mutableList;
import static io.cui.tools.collect.CollectionLiterals.mutableSet;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;

import javax.faces.component.search.ComponentNotFoundException;
import javax.faces.component.search.SearchExpressionHint;

import org.junit.Test;

import de.icw.cui.test.jsf.util.ConfigurableFacesTest;

@SuppressWarnings("javadoc")
class CuiSearchExpressionHandlerMockTest extends ConfigurableFacesTest {

    private static final String EXPRESSION = "expression";

    @Test
    public void shouldBeProvided() {
        assertNotNull(CuiMockSearchExpressionHandler.retrieve(getFacesContext()));
    }

    @Test
    public void shouldIgnoreHint() {
        CuiMockSearchExpressionContext context =
            new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
                    Collections.emptySet(), mutableSet(SearchExpressionHint.IGNORE_NO_RESULT));
        CuiMockContextCallback callback = new CuiMockContextCallback();

        assertNull(new CuiMockSearchExpressionHandler().resolveClientId(context, EXPRESSION));
        assertTrue(new CuiMockSearchExpressionHandler().resolveClientIds(context, EXPRESSION).isEmpty());

        new CuiMockSearchExpressionHandler().resolveComponent(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();

        new CuiMockSearchExpressionHandler().resolveComponents(context, EXPRESSION, callback);
        callback.assertNotCalledAtAll();
    }

    @Test(expected = ComponentNotFoundException.class)
    public void shouldFailOnMissingHint() {
        CuiMockSearchExpressionContext context =
            new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
                    Collections.emptySet(), Collections.emptySet());
        new CuiMockSearchExpressionHandler().resolveComponent(context, EXPRESSION, new CuiMockContextCallback());
    }

    @Test(expected = ComponentNotFoundException.class)
    public void shouldFailOnMissingHint2() {
        CuiMockSearchExpressionContext context =
            new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
                    Collections.emptySet(), Collections.emptySet());
        new CuiMockSearchExpressionHandler().resolveComponents(context, EXPRESSION, new CuiMockContextCallback());
    }

    @Test
    public void shouldCallBack() {
        CuiMockSearchExpressionContext context =
            new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(),
                    Collections.emptySet(), Collections.emptySet());
        CuiMockContextCallback callback = new CuiMockContextCallback();

        CuiMockComponent component = new CuiMockComponent();

        CuiMockSearchExpressionHandler handler = new CuiMockSearchExpressionHandler();
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
