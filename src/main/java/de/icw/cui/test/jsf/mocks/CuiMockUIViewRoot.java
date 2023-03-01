package de.icw.cui.test.jsf.mocks;

import static io.cui.tools.collect.CollectionLiterals.mutableMap;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

public class CuiMockUIViewRoot extends UIViewRoot {

    private final Map<String, UIComponent> componentMap = mutableMap();

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
