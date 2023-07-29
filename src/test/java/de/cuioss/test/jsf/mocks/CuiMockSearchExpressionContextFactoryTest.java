package de.cuioss.test.jsf.mocks;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.util.ConfigurableFacesTest;

class CuiMockSearchExpressionContextFactoryTest extends ConfigurableFacesTest {

    @Test
    void shouldBeProvidedBySetup() {
        assertNotNull(CuiMockSearchExpressionContextFactory.retrieve());
    }

    @Test
    void shouldHandleSearchExpressionContext() {
        var factory = CuiMockSearchExpressionContextFactory.retrieve();

        var inlineCreated = factory.getSearchExpressionContext(getFacesContext(), new CuiMockComponent(),
                Collections.emptySet(), Collections.emptySet());

        assertNotNull(inlineCreated.getVisitHints());

        factory.setSearchExpressionContext(
                new CuiMockSearchExpressionContext(new CuiMockComponent(), getFacesContext(), null, null));

        var preconfigured = factory.getSearchExpressionContext(getFacesContext(), new CuiMockComponent(),
                Collections.emptySet(), Collections.emptySet());

        assertNull(preconfigured.getVisitHints());
    }

}
