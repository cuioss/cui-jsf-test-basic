package de.icw.cui.test.jsf.support.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

@SuppressWarnings("javadoc")
@FacesComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE)
public class UiComponentWithAnnotation extends HtmlInputText {

    public static final String ANNOTATED_COMPONENT_TYPE =
        "de.icw.cui.test.jsf.context.support.UiComponentWithAnnotation";
}
