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

import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import de.cuioss.test.jsf.mocks.CuiMockServletContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.myfaces.test.mock.MockHttpServletResponse;

/**
 * Produces a
 * <ul>
 * <li>{@link RequestScoped} {@link HttpServletResponse}</li>
 * <li>{@link RequestScoped} {@link HttpServletRequest}.</li>
 * <li>{@link Dependent} {@link ServletContext}</li>
 * </ul>
 * <p>
 * In contrast to {@link ServletObjectsFromJSFContextProducer} the mocks are
 * instantiated directly
 * <p>
 * It is designed as 'opt-in'. Use with {@code @AddBeanClasses}.
 */
@ApplicationScoped
public class ServletMockObjectsProducer {

    @Getter
    @Setter
    private CuiMockHttpServletRequest servletRequest = new CuiMockHttpServletRequest();

    @Getter
    @Setter
    private MockHttpServletResponse servletResponse = new MockHttpServletResponse();

    @Getter
    @Setter
    private CuiMockServletContext servletContext = new CuiMockServletContext();

    @Produces
    @Typed({HttpServletRequest.class})
    @RequestScoped
    HttpServletRequest produceServletRequest() {
        return servletRequest;
    }

    @Produces
    @Typed({HttpServletResponse.class})
    @RequestScoped
    HttpServletResponse produceServletResponse() {
        return servletResponse;
    }

    @Produces
    @Typed({ServletContext.class})
    @Dependent
    ServletContext produceServletContext() {
        return servletContext;
    }

}
