package de.icw.cui.test.jsf.mocks;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.search.ComponentNotFoundException;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.context.FacesContext;

import io.cui.tools.string.MoreStrings;
import lombok.Getter;
import lombok.Setter;

/**
 * Mock variant of {@link SearchExpressionHandler} to be used for unit-tests. The concrete instance
 * can be derived via {@link #retrieve(FacesContext)}
 *
 * @author Oliver Wolff
 */
public class CuiMockSearchExpressionHandler extends SearchExpressionHandler {

    /** . */
    private static final String UNABLE_TO_FIND_COMPONENT_WITH_EXPRESSION =
        "Unable to find component with expression = ";

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
        } else if (!shouldIgnoreNoResult(searchExpressionContext)) {
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
        if (resolvedComponents.isEmpty()
                && !shouldIgnoreNoResult(searchExpressionContext)) {
            throw new ComponentNotFoundException("Unable to find components with expression = " + expressions);
        }

        resolvedComponents.forEach(
                component -> callback.invokeContextCallback(searchExpressionContext.getFacesContext(), component));
    }

    private boolean shouldIgnoreNoResult(SearchExpressionContext searchExpressionContext) {
        Set<SearchExpressionHint> expressionHints = searchExpressionContext.getExpressionHints();
        return null != expressionHints && expressionHints.contains(SearchExpressionHint.IGNORE_NO_RESULT);
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

    /**
     * Shorthand for accessing an instance of {@link CuiMockSearchExpressionHandler}
     *
     * @param context to be used
     * @return the previously configured {@link CuiMockSearchExpressionHandler}
     */
    public static final CuiMockSearchExpressionHandler retrieve(FacesContext context) {
        return (CuiMockSearchExpressionHandler) context.getApplication().getSearchExpressionHandler();

    }
}
