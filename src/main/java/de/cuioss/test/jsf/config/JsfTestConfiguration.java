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
package de.cuioss.test.jsf.config;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation can be used to configure certain aspects of the
 * configuration of the jsf-runtime for unit-testing. Configuration classes
 * should implement {@link JsfTestSetup} and override only the methods they
 * need. This is useful for cases where a certain configuration is reused
 * across multiple test classes.
 *
 * <p>
 * This annotation can be applied at both the type level (class) and method level.
 * When applied at the method level, the configuration will only be applied for that
 * specific test method. When applied at both levels, the method-level configuration
 * takes precedence over the class-level configuration.
 * </p>
 *
 * <p>
 * For nested test classes, the annotation can be applied to the nested class,
 * and it will take precedence over any annotation on the parent class.
 * </p>
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Repeatable(JsfTestConfigurations.class)
public @interface JsfTestConfiguration {

    /**
     * @return one or more concrete instances of {@link JsfTestSetup}
     */
    Class<? extends JsfTestSetup>[] value();

}
