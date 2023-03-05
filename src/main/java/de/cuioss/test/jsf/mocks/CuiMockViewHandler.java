package de.cuioss.test.jsf.mocks;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.apache.myfaces.test.mock.MockViewHandler20;
import org.easymock.EasyMock;

/**
 * In addition to {@link MockViewHandler20} this extension provides a mocked
 * {@link #getViewDeclarationLanguage(FacesContext, String)} using {@link EasyMock} and a method for
 * dynamically adding Composite-Component:
 * {@link #registerCompositeComponent(String, String, UIComponent)}
 * Technically they have not other use but being defined.
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockViewHandler extends MockViewHandler20 {

    final ViewDeclarationLanguage mock = EasyMock.niceMock(ViewDeclarationLanguage.class);

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return mock;
    }

    /**
     * @param libraryName must not be null
     * @param tagName must not be null
     * @param uiComponent must not be null
     */
    public void registerCompositeComponent(String libraryName, String tagName, UIComponent uiComponent) {
        EasyMock.expect(mock.createComponent(EasyMock.anyObject(), EasyMock.eq(libraryName), EasyMock.eq(tagName),
                EasyMock.anyObject()))
                .andReturn(uiComponent).anyTimes();
        EasyMock.replay(mock);
    }
}
