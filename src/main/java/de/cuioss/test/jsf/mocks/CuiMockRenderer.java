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

import de.cuioss.tools.property.PropertyHolder;
import jakarta.faces.component.*;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.component.html.HtmlOutcomeTargetLink;
import jakarta.faces.component.html.HtmlOutputLink;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.Renderer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map.Entry;

import static de.cuioss.tools.string.MoreStrings.isEmpty;

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
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        if (component.isRendered()) {
            context.getResponseWriter().startElement(getTagName(component), component);
            writeIdAndName(context, component);

            handleInputText(context, component);

            writeBasicAttributes(context, component);

            if (component instanceof EditableValueHolder valueHolder) {
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
        if (component instanceof HtmlOutcomeTargetLink output && null != output.getTarget()) {
            context.getResponseWriter().writeAttribute(TARGET, output.getTarget(), TARGET);
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
        if (component instanceof HtmlInputText text) {
            context.getResponseWriter().writeAttribute("type", "text", null);
            if (text.isDisabled()) {
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
