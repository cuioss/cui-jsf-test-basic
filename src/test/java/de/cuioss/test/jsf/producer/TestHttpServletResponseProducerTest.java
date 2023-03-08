package de.cuioss.test.jsf.producer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import org.junit.jupiter.api.Test;

@EnableJsfEnvironment
class TestHttpServletResponseProducerTest {

    @Test
    void shouldProduceItems() {
        var producer = new TestHttpServletResponseProducer();
        assertNotNull(producer.getServletResponse());
    }
}
