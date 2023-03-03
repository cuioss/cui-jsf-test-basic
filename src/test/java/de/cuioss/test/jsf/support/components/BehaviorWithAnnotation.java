package de.cuioss.test.jsf.support.components;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;

@SuppressWarnings("javadoc")
@FacesBehavior(BehaviorWithAnnotation.BEHAVIOR_ID)
public class BehaviorWithAnnotation extends BehaviorBase implements ClientBehavior {

    public static final String BEHAVIOR_ID = "de.cuioss.test.jsf.support.BehaviorWithAnnotation";

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
