/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.mocks;

import static javax.faces.FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY;

import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionContextFactory;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

/**
 * Mock for {@link SearchExpressionContextFactory}. It can be used by accessing
 * the instance of the mock by using {@link #retrieve()} and setting a concrete
 * {@link #setSearchExpressionContext(SearchExpressionContext)} that is used for
 * the actual test. If not an instance of {@link CuiMockSearchExpressionContext}
 * will be returned.
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockSearchExpressionContextFactory extends SearchExpressionContextFactory {

    @Getter
    @Setter
    private SearchExpressionContext searchExpressionContext;

    /**
     * Default Constructor.
     */
    public CuiMockSearchExpressionContextFactory() {
        super(null);
    }

    @Override
    public SearchExpressionContext getSearchExpressionContext(FacesContext context, UIComponent source,
            Set<SearchExpressionHint> expressionHints, Set<VisitHint> visitHints) {
        if (null != searchExpressionContext) {
            return searchExpressionContext;
        }
        return new CuiMockSearchExpressionContext(source, context, visitHints, expressionHints);
    }

    @Override
    public SearchExpressionContextFactory getWrapped() {
        return null;
    }

    /**
     * Shorthand for accessing the configured
     * {@link CuiMockSearchExpressionContextFactory}
     *
     * @return the currently active instance of SearchExpressionContextFactory
     */
    public static final CuiMockSearchExpressionContextFactory retrieve() {
        return (CuiMockSearchExpressionContextFactory) FactoryFinder.getFactory(SEARCH_EXPRESSION_CONTEXT_FACTORY);
    }
}
