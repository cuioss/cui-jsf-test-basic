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
