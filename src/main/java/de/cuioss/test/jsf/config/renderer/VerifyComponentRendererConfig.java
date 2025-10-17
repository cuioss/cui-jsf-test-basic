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
package de.cuioss.test.jsf.config.renderer;

import de.cuioss.test.jsf.renderer.AbstractComponentRendererTest;
import jakarta.faces.component.html.HtmlForm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used for further configuration of tests based on
 * {@link AbstractComponentRendererTest}
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface VerifyComponentRendererConfig {

    /**
     * @return boolean indicating whether the test should wrap the component to be
     * tested into a {@link HtmlForm} prior to rendering. Defaults to
     * {@code false}, The form itself will not be rendered
     */
    boolean wrapComponentInForm() default false;
}
