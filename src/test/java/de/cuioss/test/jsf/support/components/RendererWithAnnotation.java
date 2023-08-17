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
package de.cuioss.test.jsf.support.components;

import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@SuppressWarnings("javadoc")
@FacesRenderer(componentFamily = RendererWithAnnotation.COMPONENT_FAMILY, rendererType = RendererWithAnnotation.RENDERER_TYPE)
public class RendererWithAnnotation extends Renderer {

    public static final String COMPONENT_FAMILY = "de.cuioss.test.jsf.context.support.RendererWithAnnotation_family";

    public static final String RENDERER_TYPE = "de.cuioss.test.jsf.context.support.RendererWithAnnotation_renderer_type";

}
