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
package de.cuioss.test.jsf.mocks;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewDeclarationLanguage;
import org.apache.myfaces.test.mock.MockViewHandler;
import org.easymock.EasyMock;

/**
 * In addition to {@link MockViewHandler} this extension provides a mocked
 * {@link #getViewDeclarationLanguage(FacesContext, String)} using
 * {@link EasyMock} and a method for dynamically adding Composite-Component:
 * {@link #registerCompositeComponent(String, String, UIComponent)} Technically
 * they have not other use but being defined.
 *
 * @author Oliver Wolff
 */
public class CuiMockViewHandler extends MockViewHandler {

    final ViewDeclarationLanguage mock = EasyMock.niceMock(ViewDeclarationLanguage.class);

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return mock;
    }

    /**
     * @param libraryName must not be null
     * @param tagName     must not be null
     * @param uiComponent must not be null
     */
    public void registerCompositeComponent(String libraryName, String tagName, UIComponent uiComponent) {
        EasyMock.expect(mock.createComponent(EasyMock.anyObject(), EasyMock.eq(libraryName), EasyMock.eq(tagName),
            EasyMock.anyObject())).andReturn(uiComponent).anyTimes();
        EasyMock.replay(mock);
    }
}
