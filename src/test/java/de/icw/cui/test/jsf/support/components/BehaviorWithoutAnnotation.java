package de.icw.cui.test.jsf.support.components;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;

@SuppressWarnings("javadoc")
public class BehaviorWithoutAnnotation extends BehaviorBase implements ClientBehavior {

    public static final String BEHAVIOR_ID =
        "de.icw.cui.test.jsf.support.BehaviorWithoutAnnotation";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        // not needed for test
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        // not needed for test
        return null;
    }

    @Override
    public String getScript(final ClientBehaviorContext behaviorContext) {
        // not needed for test
        return null;
    }
}
