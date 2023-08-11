package de.cuioss.test.jsf.producer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

class ServletMockObjectsProducerTest {

    @Test
    void shouldProduce() {
        var producer = new ServletMockObjectsProducer();
        assertInstanceOf(HttpServletRequest.class, producer.produceServletRequest());
        assertInstanceOf(HttpServletResponse.class, producer.produceServletResponse());
        assertInstanceOf(ServletContext.class, producer.produceServletContext());
    }
}
