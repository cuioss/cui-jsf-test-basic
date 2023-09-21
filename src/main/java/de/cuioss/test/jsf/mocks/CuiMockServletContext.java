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
package de.cuioss.test.jsf.mocks;

import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;

import org.apache.myfaces.test.mock.MockServletContext;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CuiMockServletContext extends MockServletContext {

    @Override
    public Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException();
    }

    @Setter
    private int sessionTimeout = 200;

    @Setter
    private String requestCharacterEncoding = StandardCharsets.UTF_8.name();

    @Setter
    private String responseCharacterEncoding = StandardCharsets.UTF_8.name();

    @Setter
    private String virtualServerName = "virtual";

    @Setter
    private String contextPath = "mock-context";

    @Setter
    private SessionCookieConfig sessionCookieConfig;

}
