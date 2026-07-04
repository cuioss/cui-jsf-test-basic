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

import jakarta.faces.application.Resource;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.StateManagementStrategy;
import jakarta.faces.view.ViewDeclarationLanguage;
import jakarta.faces.view.ViewMetadata;
import org.apache.myfaces.test.mock.MockViewHandler;

import java.beans.BeanInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * In addition to {@link MockViewHandler} this extension provides a
 * {@link #getViewDeclarationLanguage(FacesContext, String)} backed by a small
 * hand-written {@link ViewDeclarationLanguage} stub and a method for dynamically
 * adding composite components:
 * {@link #registerCompositeComponent(String, String, UIComponent)}. In contrast
 * to a mock-library based implementation any number of composite components can
 * be registered.
 *
 * @author Oliver Wolff
 */
public class CuiMockViewHandler extends MockViewHandler {

    private final CompositeComponentViewDeclarationLanguage viewDeclarationLanguage =
        new CompositeComponentViewDeclarationLanguage();

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return viewDeclarationLanguage;
    }

    /**
     * @param libraryName must not be null
     * @param tagName     must not be null
     * @param uiComponent must not be null
     */
    public void registerCompositeComponent(String libraryName, String tagName, UIComponent uiComponent) {
        viewDeclarationLanguage.register(libraryName, tagName, uiComponent);
    }

    /**
     * Minimal {@link ViewDeclarationLanguage} that resolves composite components
     * from an internal map keyed by {@code libraryName + '|' + tagName}. All other
     * operations are unsupported as they are not needed within the test-context.
     */
    private static final class CompositeComponentViewDeclarationLanguage extends ViewDeclarationLanguage {

        private final Map<String, UIComponent> composites = new HashMap<>();

        void register(String libraryName, String tagName, UIComponent uiComponent) {
            composites.put(key(libraryName, tagName), uiComponent);
        }

        private static String key(String libraryName, String tagName) {
            // Composite-component namespaces are often full URIs (e.g.
            // "http://xmlns.jcp.org/jsf/composite/myLib" or "jakarta.faces.composite/myLib").
            // Normalize to the last path segment so a registration by short name still
            // resolves when JSF looks it up via the full namespace URI.
            var normalizedLibrary = libraryName;
            if (normalizedLibrary != null && normalizedLibrary.contains("/")) {
                normalizedLibrary = normalizedLibrary.substring(normalizedLibrary.lastIndexOf('/') + 1);
            }
            return normalizedLibrary + '|' + tagName;
        }

        @Override
        public UIComponent createComponent(FacesContext context, String taglibURI, String tagName,
            Map<String, Object> attributes) {
            return composites.get(key(taglibURI, tagName));
        }

        @Override
        public void buildView(FacesContext context, UIViewRoot root) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UIViewRoot createView(FacesContext context, String viewId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BeanInfo getComponentMetadata(FacesContext context, Resource componentResource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Resource getScriptComponentResource(FacesContext context, Resource componentResource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ViewMetadata getViewMetadata(FacesContext context, String viewId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void renderView(FacesContext context, UIViewRoot view) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UIViewRoot restoreView(FacesContext context, String viewId) {
            throw new UnsupportedOperationException();
        }
    }
}
