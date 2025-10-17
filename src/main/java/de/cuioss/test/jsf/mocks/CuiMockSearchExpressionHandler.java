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

import de.cuioss.tools.string.MoreStrings;
import jakarta.faces.component.ContextCallback;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.search.ComponentNotFoundException;
import jakarta.faces.component.search.SearchExpressionContext;
import jakarta.faces.component.search.SearchExpressionHandler;
import jakarta.faces.component.search.SearchExpressionHint;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Mock variant of {@link SearchExpressionHandler} to be used for unit-tests.
 * The concrete instance can be derived via {@link #retrieve(FacesContext)}
 *
 * @author Oliver Wolff
 */
public class CuiMockSearchExpressionHandler extends SearchExpressionHandler {

    private static final String UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION = "Unable to find component with expression = ";

    private static final String NOT_IMPLEMENTED = "Not implemented";

    @Getter
    @Setter
    private UIComponent resolvedComponent;

    @Getter
    @Setter
    private List<UIComponent> resolvedComponents = new ArrayList<>();

    @Getter
    @Setter
    private String resolvedClientId;

    @Getter
    @Setter
    private List<String> resolvedClientIds = new ArrayList<>();

    /**
     * Shorthand for accessing an instance of {@link CuiMockSearchExpressionHandler}
     *
     * @param context to be used
     * @return the previously configured {@link CuiMockSearchExpressionHandler}
     */
    public static final CuiMockSearchExpressionHandler retrieve(FacesContext context) {
        return (CuiMockSearchExpressionHandler) context.getApplication().getSearchExpressionHandler();

    }

    @Override
    public String resolveClientId(SearchExpressionContext searchExpressionContext, String expression) {
        if (MoreStrings.isEmpty(expression)) {
            throw new ComponentNotFoundException(UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION + expression);
        }
        return resolvedClientId;
    }

    @Override
    public List<String> resolveClientIds(SearchExpressionContext searchExpressionContext, String expressions) {
        if (MoreStrings.isEmpty(expressions)) {
            throw new ComponentNotFoundException(UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION + expressions);
        }
        return resolvedClientIds;
    }

    @Override
    public void resolveComponent(SearchExpressionContext searchExpressionContext, String expression,
        ContextCallback callback) {
        if (MoreStrings.isEmpty(expression)) {
            throw new ComponentNotFoundException(UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION + expression);
        }
        if (null != resolvedComponent) {
            callback.invokeContextCallback(searchExpressionContext.getFacesContext(), resolvedComponent);
        } else if (shouldIgnoreNoResult(searchExpressionContext)) {
            throw new ComponentNotFoundException(UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION + expression);
        }

    }

    @Override
    public void resolveComponents(SearchExpressionContext searchExpressionContext, String expressions,
        ContextCallback callback) {
        requireNonNull(resolvedComponents);
        if (MoreStrings.isEmpty(expressions)) {
            throw new ComponentNotFoundException(UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION + expressions);
        }
        if (resolvedComponents.isEmpty() && shouldIgnoreNoResult(searchExpressionContext)) {
            throw new ComponentNotFoundException("Unable to find components with expression = " + expressions);
        }

        resolvedComponents.forEach(
            component -> callback.invokeContextCallback(searchExpressionContext.getFacesContext(), component));
    }

    private boolean shouldIgnoreNoResult(SearchExpressionContext searchExpressionContext) {
        var expressionHints = searchExpressionContext.getExpressionHints();
        return null == expressionHints || !expressionHints.contains(SearchExpressionHint.IGNORE_NO_RESULT);
    }

    @Override
    public void invokeOnComponent(SearchExpressionContext searchExpressionContext, UIComponent previous,
        String expression, ContextCallback topCallback) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] splitExpressions(FacesContext context, String expressions) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isPassthroughExpression(SearchExpressionContext searchExpressionContext, String expression) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isValidExpression(SearchExpressionContext searchExpressionContext, String expression) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
