package de.cuioss.test.jsf.support.components;

import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@SuppressWarnings("javadoc")
@FacesRenderer(componentFamily = RendererWithAnnotation.COMPONENT_FAMILY,
        rendererType = RendererWithAnnotation.RENDERER_TYPE)
public class RendererWithAnnotation extends Renderer {

    public static final String COMPONENT_FAMILY =
        "de.cuioss.test.jsf.context.support.RendererWithAnnotation_family";

    public static final String RENDERER_TYPE =
        "de.cuioss.test.jsf.context.support.RendererWithAnnotation_renderer_type";

}
