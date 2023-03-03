package de.cuioss.test.jsf.mocks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Test;

import de.cuioss.test.jsf.util.ConfigurableFacesTest;

@SuppressWarnings("javadoc")
class CuiMockSearchExpressionContextFactoryTest extends ConfigurableFacesTest {

    @Test
    public void shouldBeProvidedBySetup() {
        assertNotNull(CuiMockSearchExpressionContextFactory.retrieve());
    }

    @Test
    public void shouldHandleSearchExpressionContext() {
        var factory = CuiMockSearchExpressionContextFactory.retrieve();

        var inlineCreated = factory.getSearchExpressionContext(getFacesContext(),
                new CuiMockComponent(), Collections.emptySet(), Collections.emptySet());

        assertNotNull(inlineCreated.getVisitHints());

        factory.setSearchExpressionContext(
                new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(), null, null));

        var preconfigured = factory.getSearchExpressionContext(getFacesContext(),
                new CuiMockComponent(), Collections.emptySet(), Collections.emptySet());

        assertNull(preconfigured.getVisitHints());
    }

}
