package de.cuioss.test.jsf.mocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CuiMockResourceHandlerTest {

    @Test
    void shouldCreateResourceSuffix() {
        assertEquals("ressource-my", CuiMockResourceHandler.createResourceMapKey("my", "ressource"));
        assertEquals("notThere-notThere", CuiMockResourceHandler.createResourceMapKey("", ""));
    }

}
