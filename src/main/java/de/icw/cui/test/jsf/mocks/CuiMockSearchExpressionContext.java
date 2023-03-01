package de.icw.cui.test.jsf.mocks;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Base Class for creating own instance of {@link SearchExpressionContext}
 *
 * @author Oliver Wolff
 *
 */
@RequiredArgsConstructor
public class CuiMockSearchExpressionContext extends SearchExpressionContext {

    @Getter
    private final UIComponent source;

    @Getter
    private final FacesContext facesContext;

    @Getter
    private final Set<VisitHint> visitHints;

    @Getter
    private final Set<SearchExpressionHint> expressionHints;

}
