package de.cuioss.test.jsf.mocks;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CuiMockViewHandlerTest {

    @Test
    void shouldReturnVDlForAnyParameter() {
        assertNotNull(new CuiMockViewHandler().getViewDeclarationLanguage(null, null));
        ;
    }

    @Test
    void shouldRegisterAnyCompositeComonent() {
        assertDoesNotThrow(() -> new CuiMockViewHandler().registerCompositeComponent(null, null, null));
    }

}
