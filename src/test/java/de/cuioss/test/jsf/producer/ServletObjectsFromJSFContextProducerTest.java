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
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@EnableJsfEnvironment
@DisplayName("ServletObjectsFromJSFContextProducer Tests")
class ServletObjectsFromJSFContextProducerTest {

    @Test
    @DisplayName("Should produce servlet objects derived from the current FacesContext")
    void shouldProduceServletObjectsFromContext() {
        var producer = new ServletObjectsFromJSFContextProducer();

        assertAll("Produced servlet objects",
            () -> assertInstanceOf(HttpServletRequest.class, producer.produceServletRequest(),
                "Servlet request should be produced"),
            () -> assertInstanceOf(HttpServletResponse.class, producer.produceServletResponse(),
                "Servlet response should be produced"),
            () -> assertInstanceOf(ServletContext.class, producer.produceServletContext(),
                "Servlet context should be produced")
        );
    }

}
