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
package de.cuioss.test.jsf.config.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import javax.el.ValueExpression;

import de.cuioss.test.jsf.component.AbstractComponentTest;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;

/**
 * If used on {@link AbstractComponentTest} type configures the property-tests.
 * <p>
 * In essence it checks the getters and setters. As default it assumes the
 * individual properties to not provide a default value. This can be controlled
 * using {@link #defaultValued()}
 *
 * @author Oliver Wolff
 */
@Repeatable(VerifyComponentPropertiesRepeat.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyComponentProperties {

    /**
     * @return a number of properties, identified by their names that are to be
     *         considered for this test: white-list
     */
    String[] of() default {};

    /**
     * @return a number of properties, identified by their names that are to be
     *         treated as having a default values, see
     *         {@link PropertyMetadata#isDefaultValue()}
     */
    String[] defaultValued() default {};

    /**
     * @return a number of properties, identified by their names that are to be
     *         ignore on the tests with {@link ValueExpression}s. <em>Caution</em>
     *         the need of the configuration hints on an invalid implementation,
     *         because all properties should consider {@link ValueExpression}s.
     */
    String[] noValueExpression() default {};

    /**
     * @return a number of properties, identified by their names representing at
     *         least a {@link Collection} that are to be asserted ignoring the
     *         concrete order, see {@link PropertyConfig#assertionStrategy()} and
     *         {@link AssertionStrategy#COLLECTION_IGNORE_ORDER}. The default
     *         implementation will always respect / assert the same order of
     *         elements.
     */
    String[] assertUnorderedCollection() default {};
}
