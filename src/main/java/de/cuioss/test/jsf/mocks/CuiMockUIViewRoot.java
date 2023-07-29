package de.cuioss.test.jsf.mocks;

import static de.cuioss.tools.collect.CollectionLiterals.mutableMap;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/**
 * Mock variant of {@link UIViewRoot} providing a heloer for adding a
 * {@link UIComponent} at runtime
 *
 * @author Oliver Wolff
 *
 */
public class CuiMockUIViewRoot extends UIViewRoot {

    private final Map<String, UIComponent> componentMap = mutableMap();

    /**
     * @param expr      must not be null
     * @param component must not be null
     */
    public void addUiComponent(String expr, UIComponent component) {
        componentMap.put(expr, component);
    }

    @Override
    public UIComponent findComponent(String expr) {
        if (componentMap.containsKey(expr)) {
            return componentMap.get(expr);
        }
        return super.findComponent(expr);
    }
}
