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

import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import jakarta.faces.application.ConfigurableNavigationHandler;
import jakarta.faces.application.NavigationCase;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockExternalContext;

import java.io.IOException;
import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static java.util.Objects.requireNonNull;

/**
 * Simulate {@link ConfigurableNavigationHandler}
 */
public class CuiMockConfigurableNavigationHandler extends ConfigurableNavigationHandler {

    private static final CuiLogger log = new CuiLogger(CuiMockConfigurableNavigationHandler.class);

    @Getter
    private final Map<String, Set<NavigationCase>> navigationCases = new HashMap<>();

    @Getter
    private boolean getNavigationCaseCalled = false;

    @Getter
    private boolean handleNavigationCalled = false;

    @Getter
    private boolean addNavigationCalled = false;

    @Getter
    private boolean addNavigationWithFromActionCalled = false;

    @Getter
    private String calledOutcome;

    private static String calculateKey(final String fromAction, final String outcome) {
        return Joiner.on("|").skipNulls().join(fromAction, outcome);
    }

    @Override
    public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome) {

        // Api Check
        requireNonNull(context, "FacesContext must not be null");
        getNavigationCaseCalled = true;
        return getFirstFittingNavigationCase(fromAction, outcome);
    }

    @Override
    public void handleNavigation(final FacesContext context, final String fromAction, final String outcome) {

        if (!(context.getExternalContext() instanceof MockExternalContext externalContext)) {

            throw new UnsupportedOperationException("handleNavigation is working only with MockExternalContext");

        }
        final var navigationCase = getNavigationCase(context, fromAction, outcome);

        var newViewId = outcome;
        try {

            if (null == navigationCase) {
                externalContext.redirect(outcome);
            } else {
                newViewId = navigationCase.getToViewId(context);
                externalContext.redirect(newViewId);
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        context.getViewRoot().setViewId(newViewId);
        calledOutcome = outcome;
        handleNavigationCalled = true;
    }

    /**
     * Add NavigationCase for outcome
     *
     * @param outcome
     * @param navigationCase
     * @return the create {@link NavigationCase}
     */
    public CuiMockConfigurableNavigationHandler addNavigationCase(final String outcome,
        final NavigationCase navigationCase) {

        this.addNavigationCase(null, outcome, navigationCase);
        addNavigationCalled = true;
        return this;
    }

    /**
     * Add NavigationCase for attributes fromAction and outcome
     *
     * @param fromAction     String
     * @param outcome        String
     * @param navigationCase {@link NavigationCase}
     * @return fluent api style
     */
    public CuiMockConfigurableNavigationHandler addNavigationCase(final String fromAction, final String outcome,
        final NavigationCase navigationCase) {

        final var key = calculateKey(fromAction, outcome);

        navigationCases.remove(key);

        navigationCases.put(key, new HashSet<>(List.of(navigationCase)));
        addNavigationWithFromActionCalled = true;
        return this;
    }

    private NavigationCase getFirstFittingNavigationCase(final String fromAction, final String outcome) {
        final var key = calculateKey(fromAction, outcome);
        if (navigationCases.containsKey(key)) {
            final List<NavigationCase> list = mutableList(navigationCases.get(key));
            return list.get(0);
        }

        log.warn(
            """
                Could not find requested navigation case '{}'.\
                 You can programmatically register the navigation case using ApplicationConfigDecorator#registerNavigationCase.\
                """,
            key);
        return null;
    }

}
