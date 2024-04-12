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
package de.cuioss.test.jsf.config;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation can be used to configure certain aspects of the
 * configuration of the jsf-runtime for unit-testing. For simple cases the
 * test-class can implement the corresponding interfaces itself:
 * {@link ApplicationConfigurator}, {@link ComponentConfigurator},
 * {@link RequestConfigurator}. The base-class will
 * ensure that the corresponding methods will be called at setup-time. This is
 * useful for cases, where there is no need to reuse a certain configuration.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(JsfTestConfigurations.class)
public @interface JsfTestConfiguration {

    /**
     * @return one or more concrete instances of {@link JsfTestContextConfigurator}
     */
    Class<? extends JsfTestContextConfigurator>[] value();

}
