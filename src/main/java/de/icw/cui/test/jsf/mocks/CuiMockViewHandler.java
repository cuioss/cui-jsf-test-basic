package de.icw.cui.test.jsf.mocks;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.apache.myfaces.test.mock.MockViewHandler20;
import org.easymock.EasyMock;

public class CuiMockViewHandler extends MockViewHandler20 {

    final ViewDeclarationLanguage mock = EasyMock.niceMock(ViewDeclarationLanguage.class);

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return mock;
    }

    public void registerCompositeComponent(String libraryName, String tagName, UIComponent uiComponent) {
        EasyMock.expect(mock.createComponent(EasyMock.anyObject(), EasyMock.eq(libraryName), EasyMock.eq(tagName),
                EasyMock.anyObject()))
                .andReturn(uiComponent).anyTimes();
        EasyMock.replay(mock);
    }
}
