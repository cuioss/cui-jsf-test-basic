package de.cuioss.test.jsf.mocks;

import static de.cuioss.tools.string.MoreStrings.isEmpty;

import java.io.IOException;
import java.util.Map.Entry;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutcomeTarget;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.cuioss.tools.property.PropertyHolder;
import lombok.RequiredArgsConstructor;

/**
 * Simple Mock renderer that is capable of rendering any element by using the
 * simple-name of the given component. In addition it is capable of rendering
 * the 'id'-attribute as 'id' and 'name' in case the component is of type
 * {@link UIInput} or the attribute {@link UIComponent#getId()} is set, the
 * 'styleClass' and 'style' attributes and reacts to the 'rendered'-attribute.
 * It can be configured to use a certain tagname to be rendered by using
 * {@link #CuiMockRenderer(String)}. Otherwise the simple-name of the component
 * will be used.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@SuppressWarnings("resource") // owolff: Not an issue because the is for tests
public class CuiMockRenderer extends Renderer {

    private static final String OUTCOME = "outcome";
    private static final String TARGET = "target";
    private static final String VALUE = "value";
    private static final String STYLE_CLASS = "styleClass";
    private static final String STYLE_ATTRIBUTE = "style";
    private static final String TITLE = "title";
    private static final String DISABLED = "disabled";

    private final String tagName;

    /**
     * Default constructor resulting in the actual component to be used as tagname.
     */
    public CuiMockRenderer() {
        this(null);
    }

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        if (component.isRendered()) {
            context.getResponseWriter().startElement(getTagName(component), component);
            writeIdAndName(context, component);

            handleInputText(context, component);

            writeBasicAttributes(context, component);

            if (component instanceof EditableValueHolder) {
                var valueHolder = (ValueHolder) component;
                context.getResponseWriter().writeAttribute(VALUE, valueHolder.getValue(), VALUE);
            } else if (component instanceof UIOutcomeTarget) {
                handleOutputTarget(context, component);
            } else if (component instanceof HtmlOutputLink) {
                handleHtmlOutputLink(context, component);
            } else if (component instanceof UIOutput) {
                handleUIOutput(context, component);
            }

            handleOutcomeTargetLink(context, component);
        }
    }

    private void handleOutcomeTargetLink(final FacesContext context, final UIComponent component) throws IOException {
        if (component instanceof HtmlOutcomeTargetLink) {
            var output = (HtmlOutcomeTargetLink) component;
            if (null != output.getTarget()) {
                context.getResponseWriter().writeAttribute(TARGET, output.getTarget(), TARGET);
            }
        }
    }

    private void handleUIOutput(final FacesContext context, final UIComponent component) throws IOException {
        var output = (UIOutput) component;
        if (null != output.getValue()) {
            context.getResponseWriter().writeText(output.getValue(), VALUE);
        }
    }

    private void handleHtmlOutputLink(final FacesContext context, final UIComponent component) throws IOException {
        var output = (HtmlOutputLink) component;
        if (null != output.getTarget()) {
            context.getResponseWriter().writeAttribute(TARGET, output.getTarget(), TARGET);
        }
        if (null != output.getValue()) {
            context.getResponseWriter().writeAttribute(VALUE, output.getValue(), VALUE);
        }
    }

    private void handleOutputTarget(final FacesContext context, final UIComponent component) throws IOException {
        var output = (UIOutcomeTarget) component;
        if (null != output.getOutcome()) {
            context.getResponseWriter().writeAttribute(OUTCOME, output.getOutcome(), OUTCOME);
        }
        if (null != output.getValue()) {
            context.getResponseWriter().writeAttribute(VALUE, output.getValue(), VALUE);
        }
    }

    private void writeBasicAttributes(final FacesContext context, final UIComponent component) throws IOException {
        writeAttributeIfPresent(context, component, STYLE_CLASS, "class");
        writeAttributeIfPresent(context, component, STYLE_ATTRIBUTE, STYLE_ATTRIBUTE);
        writeAttributeIfPresent(context, component, TITLE, TITLE);

        for (Entry<String, Object> entry : component.getPassThroughAttributes(true).entrySet()) {
            context.getResponseWriter().writeAttribute(entry.getKey(), entry.getValue(), null);
        }
    }

    private void handleInputText(final FacesContext context, final UIComponent component) throws IOException {
        if (component instanceof HtmlInputText) {
            context.getResponseWriter().writeAttribute("type", "text", null);
            if (((HtmlInputText) component).isDisabled()) {
                context.getResponseWriter().writeAttribute(DISABLED, DISABLED, null);
            }
        }
    }

    private void writeIdAndName(final FacesContext context, final UIComponent component) throws IOException {
        if (!isEmpty(component.getId()) || component instanceof UIInput) {
            var id = component.getClientId();
            context.getResponseWriter().writeAttribute("id", id, null);
            context.getResponseWriter().writeAttribute("name", id, null);
        }
    }

    private static void writeAttributeIfPresent(final FacesContext context, final UIComponent component,
            final String propertyName, final String attributeName) throws IOException {
        var holder = PropertyHolder.from(component.getClass(), propertyName);
        if (holder.isPresent() && holder.get().getReadWrite().isReadable()) {
            var propertyValue = holder.get().readFrom(component);
            if (null != propertyValue) {
                context.getResponseWriter().writeAttribute(attributeName, propertyValue, null);
            }
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (component.isRendered()) {
            context.getResponseWriter().endElement(getTagName(component));
        }
    }

    private String getTagName(final UIComponent component) {
        if (!isEmpty(tagName)) {
            return tagName;
        }
        return component.getClass().getSimpleName();
    }
}
