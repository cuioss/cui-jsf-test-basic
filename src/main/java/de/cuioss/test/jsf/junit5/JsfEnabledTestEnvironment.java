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

import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for creating unit-tests with a wired / configured JSF-environment
 * being annotated with {@link EnableJsfEnvironment} and implementing
 * {@link JsfEnvironmentConsumer}
 * <p>
 * <strong>Deprecation Notice:</strong> This class is deprecated in favor of using parameter resolution
 * for JSF-related objects in test methods. Instead of extending this class and accessing JSF objects
 * through getter methods, you can now directly declare the JSF objects you need as parameters in your test methods.
 * <p>
 * Migration example:
 * <pre>
 * <code>
 * // Old approach (deprecated)
 * &#64;EnableJsfEnvironment
 * class MyTest extends JsfEnabledTestEnvironment {
 *
 *     &#64;Test
 *     void testSomething() {
 *         FacesContext facesContext = getFacesContext();
 *         // Test code using facesContext
 *     }
 * }
 *
 * // New approach (recommended)
 * &#64;EnableJsfEnvironment
 * class MyTest {
 *
 *     &#64;Test
 *     void testSomething(FacesContext facesContext) {
 *         // Test code using facesContext
 *     }
 * }
 * </code>
 * </pre>
 *
 * @author Oliver Wolff
 * @deprecated since 4.1 - Use parameter resolution instead. See class documentation for migration examples.
 */
@EnableJsfEnvironment(useIdentityResourceBundle = true)
@Deprecated(since = "4.1", forRemoval = true)
public class JsfEnabledTestEnvironment implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;
}