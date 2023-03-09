package de.cuioss.test.jsf.producer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;

@EnableJsfEnvironment
class TestHttpServletResponseProducerTest {

    @Test
    void shouldProduceItems() {
        var producer = new TestHttpServletResponseProducer();
        assertNotNull(producer.getServletResponse());
    }
}
