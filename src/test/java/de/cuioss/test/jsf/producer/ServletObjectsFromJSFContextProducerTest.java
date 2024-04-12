/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@EnableJsfEnvironment
class ServletObjectsFromJSFContextProducerTest {

    @Test
    void shouldProduce() {
        var producer = new ServletObjectsFromJSFContextProducer();
        assertInstanceOf(HttpServletRequest.class, producer.produceServletRequest());
        assertInstanceOf(HttpServletResponse.class, producer.produceServletResponse());
        assertInstanceOf(ServletContext.class, producer.produceServletContext());
    }

}
