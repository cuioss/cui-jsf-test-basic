package de.icw.cui.test.jsf.mocks;

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
        assertTrue(called == 0, "Has been called " + called + " times");
    }

}
