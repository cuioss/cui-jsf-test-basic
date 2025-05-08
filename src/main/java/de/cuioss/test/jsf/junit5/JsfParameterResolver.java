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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.junit5.NavigationAsserts;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.application.Application;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.RequiredArgsConstructor;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Parameter resolver for JSF-related objects in JUnit 5 tests.
 * <p>
 * This resolver supports the following parameter types:
 * <ul>
 * <li>{@link JsfEnvironmentHolder} - the main holder object that contains all JSF-related objects</li>
 * <li>{@link FacesContext} - the central context object for JSF processing</li>
 * <li>{@link ExternalContext} - provides access to the external environment (request, response, etc.)</li>
 * <li>{@link Application} - represents the JSF application</li>
 * <li>{@link RequestConfigDecorator} - allows configuration of the request in tests</li>
 * <li>{@link ApplicationConfigDecorator} - allows configuration of the application in tests</li>
 * <li>{@link ComponentConfigDecorator} - allows configuration of components in tests</li>
 * <li>{@link MockHttpServletResponse} - provides access to the mock HTTP response</li>
 * <li>{@link MockHttpServletRequest} - provides access to the mock HTTP request</li>
 * <li>{@link NavigationAsserts} - provides utilities for asserting navigation outcomes and redirects</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>
 * {@code
 * @EnableJsfEnvironment
 * class MyTest {
 *
 *     @Test
 *     void testWithFacesContext(FacesContext facesContext) {
 *         assertNotNull(facesContext);
 *         // Test code using facesContext
 *     }
 *
 *     @Test
 *     void testWithMultipleParameters(
 *             FacesContext facesContext,
 *             Application application,
 *             ComponentConfigDecorator componentConfig) {
 *         // Test code using multiple JSF objects
 *     }
 * }
 * }
 * </pre>
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class JsfParameterResolver implements ParameterResolver {

    private final JsfEnvironmentHolder environmentHolder;

    private static final Set<Class<?>> SUPPORTED_TYPES = new HashSet<>();

    static {
        SUPPORTED_TYPES.add(JsfEnvironmentHolder.class);
        SUPPORTED_TYPES.add(FacesContext.class);
        SUPPORTED_TYPES.add(ExternalContext.class);
        SUPPORTED_TYPES.add(Application.class);
        SUPPORTED_TYPES.add(RequestConfigDecorator.class);
        SUPPORTED_TYPES.add(ApplicationConfigDecorator.class);
        SUPPORTED_TYPES.add(ComponentConfigDecorator.class);
        SUPPORTED_TYPES.add(MockHttpServletResponse.class);
        SUPPORTED_TYPES.add(MockHttpServletRequest.class);
        SUPPORTED_TYPES.add(NavigationAsserts.class);
    }

    /**
     * Determines if this resolver supports the given parameter.
     *
     * @param parameterContext the context for the parameter for which a resolver is being requested;
     *                        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked;
     *                        never {@code null}
     * @return {@code true} if this resolver can resolve the parameter
     * @throws ParameterResolutionException if an error occurs while determining whether the parameter
     *                                     can be resolved
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return SUPPORTED_TYPES.contains(parameterContext.getParameter().getType());
    }

    /**
     * Resolves the given parameter.
     *
     * @param parameterContext the context for the parameter to be resolved;
     *                        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked;
     *                        never {@code null}
     * @return the resolved parameter value; may be {@code null} but only if the parameter type
     *         is not a primitive
     * @throws ParameterResolutionException if an error occurs while resolving the parameter
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();

        if (JsfEnvironmentHolder.class.equals(parameterType)) {
            return environmentHolder;
        }
        if (FacesContext.class.equals(parameterType)) {
            return environmentHolder.getFacesContext();
        }
        if (ExternalContext.class.equals(parameterType)) {
            return environmentHolder.getExternalContext();
        }
        if (Application.class.equals(parameterType)) {
            return environmentHolder.getApplication();
        }
        if (RequestConfigDecorator.class.equals(parameterType)) {
            return environmentHolder.getRequestConfigDecorator();
        }
        if (ApplicationConfigDecorator.class.equals(parameterType)) {
            return environmentHolder.getApplicationConfigDecorator();
        }
        if (ComponentConfigDecorator.class.equals(parameterType)) {
            return environmentHolder.getComponentConfigDecorator();
        }
        if (MockHttpServletResponse.class.equals(parameterType)) {
            return environmentHolder.getResponse();
        }
        if (MockHttpServletRequest.class.equals(parameterType)) {
            return environmentHolder.getRequest();
        }
        if (NavigationAsserts.class.equals(parameterType)) {
            return new NavigationAsserts(
                environmentHolder.getFacesContext(),
                environmentHolder.getExternalContext(),
                environmentHolder.getApplicationConfigDecorator()
            );
        }

        throw new ParameterResolutionException("Unsupported parameter type: " + parameterType.getName());
    }
}
