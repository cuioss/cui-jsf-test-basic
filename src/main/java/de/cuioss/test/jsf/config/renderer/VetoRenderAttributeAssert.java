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
package de.cuioss.test.jsf.config.renderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.cuioss.test.jsf.renderer.AbstractComponentRendererTest;
import de.cuioss.test.jsf.renderer.CommonRendererAsserts;

/**
 * A contract Veto is used for for suppressing certain test-contracts. The
 * default implementation of {@link AbstractComponentRendererTest} tests all
 * contracts that are not vetoed.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VetoRenderAttributeAsserts.class)
public @interface VetoRenderAttributeAssert {

    /**
     * @return the concrete contract to be suppressed / ignored
     */
    CommonRendererAsserts[] value();
}
