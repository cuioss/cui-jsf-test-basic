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

import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletContext;

import org.apache.myfaces.test.mock.MockHttpSession;

import lombok.Getter;
import lombok.Setter;

/**
 * Extension to {@link MockHttpSession} that provides the programmatic setting
 * of 'maxInactiveInterval'
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockHttpSession extends MockHttpSession {

    @Getter
    @Setter
    private int maxInactiveInterval;

    /**
     * Constructor.
     *
     * @param servletContext must not be null
     */
    public CuiMockHttpSession(final ServletContext servletContext) {
        super(servletContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void invalidate() {
        Collection<String> names = Collections.list(super.getAttributeNames());
        names.forEach(super::removeAttribute);
    }

}
