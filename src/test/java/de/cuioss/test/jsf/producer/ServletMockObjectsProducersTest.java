package de.cuioss.test.jsf.producer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

class ServletMockObjectsProducersTest {

    @Test
    void shouldProduce() {
        var producer = new ServletMockObjectsProducers();
        assertInstanceOf(HttpServletRequest.class, producer.getServletRequest());
        assertInstanceOf(HttpServletResponse.class, producer.getServletResponse());
        assertInstanceOf(ServletContext.class, producer.getServletContext());
    }
}
