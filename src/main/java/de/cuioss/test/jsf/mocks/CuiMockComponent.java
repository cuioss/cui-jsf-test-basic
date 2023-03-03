package de.cuioss.test.jsf.mocks;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * Simple Mock component to be used for cases where you need an "any-component"
 *
 * @author Oliver Wolff
 *
 */
@FacesComponent(CuiMockComponent.COMPONENT_TYPE)
public class CuiMockComponent extends UIComponentBase {

    /** "mockFamily" */
    public static final String FAMILY = "mockFamily";

    /** "mockRendererType" */
    public static final String RENDERER_TYPE = "mockRendererType";

    /** "mockComponentType" */
    public static final String COMPONENT_TYPE = "mockComponentType";

    @Override
    public String getFamily() {
        return FAMILY;
    }

    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }

}
