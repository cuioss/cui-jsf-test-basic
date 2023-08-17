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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.faces.context.FacesContext;

import org.junit.jupiter.api.extension.ExtendWith;

import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.util.IdentityResourceBundle;

/**
 * Using this annotations at type-level of a junit 5 test enable the
 * myfaces-test-based test-framework for unit-tests. The configuration is
 * implemented using some kind of decorator pattern (roughly). The actual
 * configuration relies on two sources:
 * <ul>
 * <li>It scans for the annotations of type {@link JsfTestConfiguration}
 * instantiates the corresponding classes and configures the environment
 * accordingly</li>
 * <li>It checks whether the actual test-class implements one of
 * {@link ApplicationConfigurator}, {@link BeanConfigurator},
 * {@link RequestConfigurator} or {@link ComponentConfigurator} and calls the
 * corresponding methods accordingly, <em>after</em> the configurator derived by
 * the annotations</li>
 * </ul>
 * <h3>Using</h3>
 * <p>
 * Simple Sample: Use a preconfigured JSF-context for the test providing
 * BasicApplicationConfiguration
 * </p>
 *
 * <pre>
 * <code>
     &#64;EnableJsfEnvironment
     &#64;JsfTestConfiguration(BasicApplicationConfiguration.class)
     class JSFEnableTest {
</code>
 * </pre>
 *
 * <p>
 * Complex Sample: Use a preconfigured JSF-context with access to the context
 * information like {@link FacesContext}, {@link ComponentConfigurator}, ... as
 * defined within {@link JsfEnvironmentHolder} With this setup you can
 * programmatically access the element
 * </p>
 *
 * <pre>
 * <code>
     &#64;EnableJsfEnvironment
     &#64;JsfTestConfiguration(BasicApplicationConfiguration.class)
     class JsfSetupExtensionTest implements JsfEnvironmentConsumer {

        &#64;Setter
        &#64;Getter
        private JsfEnvironmentHolder environmentHolder;</code>
 * </pre>
 *
 * <p>
 * In addition there is a new way of dealing with localized messages for
 * unit-tests. In essence there is the {@link IdentityResourceBundle}
 * configured: This is helpful for tests where you want to ensure that a certain
 * message key is used to create a message but do not want to test the actual
 * ResourceBundle mechanism itself. It will always return the given key itself.
 * As default this mechanism is active, you can change this by setting
 * {@link #useIdentityResourceBundle()}. If it is active it is used as well for
 * resolving the MessageBundle. In case of many annotations of
 * {@link EnableJsfEnvironment} are in the type hierarchy, the one on the most
 * concrete Type will be chosen, usually on the actual unit-test.
 * </p>
 *
 * @author Oliver Wolff
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(JsfSetupExtension.class)
@Repeatable(EnableJsfEnvironments.class)
public @interface EnableJsfEnvironment {

    /**
     * @return boolean
     */
    boolean useIdentityResourceBundle() default true;

}
