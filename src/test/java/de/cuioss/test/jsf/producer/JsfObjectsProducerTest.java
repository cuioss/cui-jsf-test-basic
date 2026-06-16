/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.producer;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.application.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@DisplayName("JsfObjectsProducer Tests")
class JsfObjectsProducerTest {

    @Test
    @DisplayName("Should produce all JSF objects from the current FacesContext")
    void shouldProduceAllJsfObjects() {
        var producer = new JsfObjectsProducer();

        assertAll("Produced JSF objects",
            () -> assertNotNull(producer.getApplicationMap(), "Application map should be produced"),
            () -> assertNotNull(producer.getExternalContext(), "External context should be produced"),
            () -> assertNotNull(producer.getFacesContext(), "Faces context should be produced"),
            () -> assertNotNull(producer.getHeaderMap(), "Header map should be produced"),
            () -> assertNotNull(producer.getHeaderValuesMap(), "Header values map should be produced"),
            () -> assertNotNull(producer.getInitParameter(), "Init parameter map should be produced"),
            () -> assertNotNull(producer.getRequest(), "Request should be produced"),
            () -> assertNotNull(producer.getRequestCookieMap(), "Request cookie map should be produced"),
            () -> assertNotNull(producer.getRequestParameterMap(), "Request parameter map should be produced"),
            () -> assertNotNull(producer.getSessionMap(), "Session map should be produced"),
            () -> assertNotNull(producer.getViewMap(), "View map should be produced"),
            () -> assertNotNull(producer.getViewRoot(), "View root should be produced"),
            () -> assertInstanceOf(Application.class, producer.produceApplication(), "Application should be produced")
        );
    }

}
