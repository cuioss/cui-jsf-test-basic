package de.icw.cui.test.jsf.mocks;

import static io.cui.tools.collect.CollectionLiterals.mutableList;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;

import org.apache.myfaces.test.mock.MockExternalContext;

import io.cui.tools.logging.CuiLogger;
import io.cui.tools.string.Joiner;
import lombok.Getter;

/**
 * Simulate {@link ConfigurableNavigationHandler}
 *
 * @author i000576
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

    @Override
    public NavigationCase getNavigationCase(final FacesContext context, final String fromAction,
            final String outcome) {

        // Api Check
        requireNonNull(context, "FacesContext must not be null");
        getNavigationCaseCalled = true;
        return getFirstFittingNavigationCase(fromAction, outcome);
    }

    @Override
    public void handleNavigation(final FacesContext context, final String fromAction,
            final String outcome) {

        if (context.getExternalContext() instanceof MockExternalContext) {

            final NavigationCase navigationCase = getNavigationCase(context, fromAction, outcome);

            final MockExternalContext externalContext = (MockExternalContext) context
                    .getExternalContext();

            String newViewId = outcome;
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

        } else {

            throw new UnsupportedOperationException(
                    "handleNavigation is working only with MockExternalContext");

        }
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
     * @param fromAction String
     * @param outcome String
     * @param navigationCase {@link NavigationCase}
     * @return fluent api style
     */
    public CuiMockConfigurableNavigationHandler addNavigationCase(final String fromAction,
            final String outcome,
            final NavigationCase navigationCase) {

        final String key = calculateKey(fromAction, outcome);

        navigationCases.remove(key);

        navigationCases.put(key, new HashSet<>(Arrays.asList(navigationCase)));
        addNavigationWithFromActionCalled = true;
        return this;
    }

    private NavigationCase getFirstFittingNavigationCase(final String fromAction,
            final String outcome) {
        final String key = calculateKey(fromAction, outcome);
        if (navigationCases.containsKey(key)) {
            final List<NavigationCase> list = mutableList(navigationCases.get(key));
            return list.get(0);
        }

        log.warn("Could not find requested navigation case '{}'."
                + " You can programmatically register the navigation case using ApplicationConfigDecorator#registerNavigationCase.",
                key);
        return null;
    }

    private static String calculateKey(final String fromAction, final String outcome) {
        return Joiner.on("|").skipNulls().join(fromAction, outcome);
    }

}
