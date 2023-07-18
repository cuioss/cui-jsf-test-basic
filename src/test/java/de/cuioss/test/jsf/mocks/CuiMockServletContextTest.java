package de.cuioss.test.jsf.mocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CuiMockServletContextTest {

    @Test
    void shouldHandleDefaultBehavior() {
        var context = new CuiMockServletContext();

        assertEquals(StandardCharsets.UTF_8.name(), context.getRequestCharacterEncoding());
        assertEquals(StandardCharsets.UTF_8.name(), context.getResponseCharacterEncoding());
        assertEquals(200, context.getSessionTimeout());
        assertEquals("virtual", context.getVirtualServerName());
        assertThrows(UnsupportedOperationException.class, () -> context.addJspFile("aa", "bb"));
    }

}
