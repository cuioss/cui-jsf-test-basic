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

import jakarta.faces.application.Resource;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Mock with all necessary values provided with setters
 *
 * @author Oliver Wolff
 */
@ToString(callSuper = false)
public class CuiMockResource extends Resource {

    @Getter
    @Setter
    private InputStream inputStream;

    @Getter
    @Setter
    private Map<String, String> responseHeaders;

    @Getter
    @Setter
    private String requestPath;

    @Setter
    @Getter
    private URL uRL;

    @Setter
    private boolean userAgentNeedsUpdate;

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        return userAgentNeedsUpdate;
    }

}
