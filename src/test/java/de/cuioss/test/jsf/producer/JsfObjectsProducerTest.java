package de.cuioss.test.jsf.producer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.application.Application;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;

@EnableJsfEnvironment
class JsfObjectsProducerTest {

    @Test
    void shouldProduceItems() {
        var producer = new JsfObjectsProducer();
        assertNotNull(producer.getApplicationMap());
        assertNotNull(producer.getExternalContext());
        assertNotNull(producer.getFacesContext());
        assertNotNull(producer.getHeaderMap());
        assertNotNull(producer.getHeaderValuesMap());
        assertNotNull(producer.getInitParameter());
        assertNotNull(producer.getRequest());
        assertNotNull(producer.getRequestCookieMap());
        assertNotNull(producer.getRequestParameterMap());
        assertNotNull(producer.getSessionMap());
        assertNotNull(producer.getSessionMap());
        assertNotNull(producer.getViewMap());
        assertNotNull(producer.getViewRoot());
        assertInstanceOf(Application.class, producer.produceApplication());
    }

}
