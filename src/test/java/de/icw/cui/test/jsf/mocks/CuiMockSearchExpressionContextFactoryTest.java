package de.icw.cui.test.jsf.mocks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import javax.faces.component.search.SearchExpressionContext;

import org.junit.Test;

import de.icw.cui.test.jsf.util.ConfigurableFacesTest;

@SuppressWarnings("javadoc")
class CuiMockSearchExpressionContextFactoryTest extends ConfigurableFacesTest {

    @Test
    public void shouldBeProvidedBySetup() {
        assertNotNull(CuiMockSearchExpressionContextFactory.retrieve());
    }

    @Test
    public void shouldHandleSearchExpressionContext() {
        CuiMockSearchExpressionContextFactory factory = CuiMockSearchExpressionContextFactory.retrieve();

        SearchExpressionContext inlineCreated = factory.getSearchExpressionContext(getFacesContext(),
                new CuiMockComponent(), Collections.emptySet(), Collections.emptySet());

        assertNotNull(inlineCreated.getVisitHints());

        factory.setSearchExpressionContext(
                new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(), null, null));

        SearchExpressionContext preconfigured = factory.getSearchExpressionContext(getFacesContext(),
                new CuiMockComponent(), Collections.emptySet(), Collections.emptySet());

        assertNull(preconfigured.getVisitHints());
    }

}
