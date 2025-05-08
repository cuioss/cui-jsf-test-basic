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
package de.cuioss.test.jsf.renderer;

import de.cuioss.test.jsf.config.renderer.VerifyComponentRendererConfig;
import de.cuioss.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.tools.reflect.FieldWrapper;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.component.html.HtmlForm;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.PostAddToViewEvent;
import jakarta.faces.render.Renderer;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * While {@link AbstractRendererTestBase} focuses on API-Contract utility
 * methods, this class provides a number of implicit tests. Simplifying the
 * testing of concrete renderer behavior.
 * <h3>Configuration</h3> You can configure this test by
 * <ul>
 * <li>{@link VerifyComponentRendererConfig}</li>
 * <li>{@link VetoRenderAttributeAssert}</li>
 * </ul>
 *
 * @param <R> The renderer being tested
 * @author Oliver Wolff
 */
public abstract class AbstractComponentRendererTest<R extends Renderer> extends AbstractRendererTestBase<R> {

    @Getter
    private Set<RendererAttributeAssert> activeAsserts;

    @Getter
    private boolean wrapComponentInForm = false;

    /**
     * Initializes the active {@link RendererAttributeAssert} and checks for the
     * {@link VerifyComponentRendererConfig} annotation
     */
    @BeforeEach
    public void initAbstractComponentRendererTest() {
        handleRenderAttributeAsserts();
        handleConfigAnnotation();
    }

    private void handleConfigAnnotation() {
        Optional<VerifyComponentRendererConfig> configOption = MoreReflection.extractAnnotation(getClass(),
            VerifyComponentRendererConfig.class);
        configOption.ifPresent(verifyComponentRendererConfig -> wrapComponentInForm = verifyComponentRendererConfig
            .wrapComponentInForm());
    }

    private void handleRenderAttributeAsserts() {
        activeAsserts = new HashSet<>(Arrays.asList(CommonRendererAsserts.values()));
        final Set<CommonRendererAsserts> vetoes = new HashSet<>();
        MoreReflection.extractAllAnnotations(this.getClass(), VetoRenderAttributeAssert.class)
            .forEach(veto -> vetoes.addAll(mutableList(veto.value())));
        activeAsserts.removeAll(vetoes);
    }

    /**
     * Iterates through all active RendererAttributeAssert and tests them
     * accordingly
     */
    @Test
    public void shouldHandleRendererAttributeAsserts(FacesContext facesContext) {
        for (RendererAttributeAssert attributeAssert : activeAsserts) {
            var component = getWrappedComponent();
            attributeAssert.applyAttribute(component);
            component.processEvent(new PostAddToViewEvent(component));
            var document = DomUtils.htmlStringToDocument(assertDoesNotThrow(() -> super.renderToString(component, facesContext)));
            attributeAssert.assertAttributeSet(document.getRootElement());
        }
    }

    /**
     * @return the {@link UIComponent} derived by {@link #getComponent()} or the
     * same wrapped in an {@link HtmlForm} in case
     * {@link #isWrapComponentInForm()} is {@code true}
     */
    protected UIComponent getWrappedComponent() {
        var component = getComponent();
        if (wrapComponentInForm) {
            var form = new HtmlForm();
            form.getChildren().add(component);
        }
        return component;
    }

    /**
     * Helper method that extracts all queued events from {@link UIViewRoot}.
     * To use this method, the corresponding test must ensure that the
     * component under test is child of {@link UIViewRoot}.
     * Otherwise, the events cannot be extracted
     *
     * @return the plain list of events available at {@link UIViewRoot} at this
     * time.
     */
    // owolff: Sonar false-positive
    @SuppressWarnings({"unchecked"})
    public List<FacesEvent> extractEventsFromViewRoot(FacesContext facesContext) {
        final List<FacesEvent> found = new ArrayList<>();
        final var uiViewRoot = facesContext.getViewRoot();
        // Hacky: Private field of myfaces
        var eventField = FieldWrapper.from(UIViewRoot.class, "_events");
        if (eventField.isPresent()) {
            var events = eventField.get().readValue(uiViewRoot);
            events.ifPresent(o -> found.addAll((Collection<? extends FacesEvent>) o));
            return found;
        }
        // Hacky: Private field of mojarra
        eventField = FieldWrapper.from(UIViewRoot.class, "events");
        if (eventField.isEmpty()) {
            throw new AssertionError(
                "jakarta.faces.component.UIViewRoot provides neither the field 'events' nor '_events'");
        }
        var events = eventField.get().readValue(uiViewRoot);
        if (events.isPresent()) {
            var eventLists = (List<List<FacesEvent>>) events.get();
            eventLists.forEach(found::addAll);
        }
        return found;

    }

}
