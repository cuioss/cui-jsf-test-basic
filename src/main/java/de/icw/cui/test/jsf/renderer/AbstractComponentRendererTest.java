package de.icw.cui.test.jsf.renderer;

import static io.cui.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlForm;
import javax.faces.event.FacesEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.Renderer;

import org.jdom2.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.config.renderer.VerifyComponentRendererConfig;
import de.icw.cui.test.jsf.config.renderer.VetoRenderAttributeAssert;
import de.icw.cui.test.jsf.renderer.util.DomUtils;
import io.cui.test.valueobjects.objects.impl.ExceptionHelper;
import io.cui.tools.logging.CuiLogger;
import io.cui.tools.reflect.MoreReflection;
import lombok.Getter;

/**
 * While {@link AbstractRendererTestBase} focuses on API-Contract utility methods, this class
 * provides a number of implicit tests. Simplifying the testing of concrete renderer behavior.
 * <h3>Configuration</h3>
 * You can configure this test by
 * <ul>
 * <li>{@link VerifyComponentRendererConfig}</li>
 * <li>{@link VetoRenderAttributeAssert}</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @param <R> The renderer being tested
 */
public abstract class AbstractComponentRendererTest<R extends Renderer> extends AbstractRendererTestBase<R> {

    private static final CuiLogger log = new CuiLogger(AbstractComponentRendererTest.class);

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
        Optional<VerifyComponentRendererConfig> configOption =
                MoreReflection.extractAnnotation(getClass(), VerifyComponentRendererConfig.class);
        configOption.ifPresent(verifyComponentRendererConfig -> wrapComponentInForm =
                verifyComponentRendererConfig.wrapComponentInForm());
    }

    private void handleRenderAttributeAsserts() {
        activeAsserts = new HashSet<>(Arrays.asList(CommonRendererAsserts.values()));
        final Set<CommonRendererAsserts> vetoes = new HashSet<>();
        MoreReflection.extractAllAnnotations(this.getClass(), VetoRenderAttributeAssert.class)
        .forEach(veto -> vetoes.addAll(mutableList(veto.value())));
        activeAsserts.removeAll(vetoes);
    }

    /**
     * Iterates through all active RendererAttributeAssert and tests them accordingly
     */
    @Test
    public void shouldHandleRendererAttributeAsserts() {
        for (RendererAttributeAssert attributeAssert : activeAsserts) {
            UIComponent component = getWrappedComponent();
            attributeAssert.applyAttribute(component);
            component.processEvent(new PostAddToViewEvent(component));
            Document document = DomUtils.htmlStringToDocument(super.renderToString(component));
            attributeAssert.assertAttributeSet(document.getRootElement());
        }
    }

    /**
     * @return the {@link UIComponent} derived by {@link #getComponent()} or the same wrapped in an
     *         {@link HtmlForm} in case {@link #isWrapComponentInForm()} is {@code true}
     */
    protected UIComponent getWrappedComponent() {
        UIComponent component = getComponent();
        if (wrapComponentInForm) {
            HtmlForm form = new HtmlForm();
            form.getChildren().add(component);
        }
        return component;
    }

    /**
     * Helper method that extracts all queued events from {@link UIViewRoot}. In
     * order to use this method the corresponding test must ensure, that the
     * component under test is child of {@link UIViewRoot}. Otherwise the events can not be
     * extracted
     *
     * @return the plain list of events available at {@link UIViewRoot} at this
     *         time.
     */
    // owolff: Sonar false-positive
    @SuppressWarnings({ "unchecked", "squid:S3655" })
    public List<FacesEvent> extractEventsFromViewRoot() {
        final List<FacesEvent> found = new ArrayList<>();
        final UIViewRoot uiViewRoot = getFacesContext().getViewRoot();
        // Hacky: Private field of myfaces
        Optional<Field> eventField = accessField(UIViewRoot.class, "_events");
        if (eventField.isPresent()) {
            try {
                List<FacesEvent> events = (List<FacesEvent>) eventField.get().get(uiViewRoot);
                if (null != events) {
                    found.addAll(events);
                }
                return found;
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new AssertionError(
                        "Unable to access content fo field '_events' on type javax.faces.component.UIViewRoot", e);
            }
        }
        // Hacky: Private field of mojarra
        eventField = accessField(UIViewRoot.class, "events");
        if (!eventField.isPresent()) {
            fail("javax.faces.component.UIViewRoot provides neither the field 'events' nor '_events'");
        }
        try {
            List<List<FacesEvent>> events = (List<List<FacesEvent>>) eventField.get().get(uiViewRoot);
            if (null != events) {
                events.forEach(found::addAll);
            }
            return found;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new AssertionError(
                    "Unable to access content fo field '_events' on type javax.faces.component.UIViewRoot", e);
        }
    }

    private static Optional<Field> accessField(final Class<?> clazz, final String fieldName) {
        // Lets hack
        try {
            Field eventField = clazz.getDeclaredField(fieldName);
            eventField.setAccessible(true);
            return Optional.of(eventField);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e1) {
            log.debug("Unable to access private fields '{}' onf type {}, reason {}", fieldName, clazz,
                    ExceptionHelper.extractCauseMessageFromThrowable(e1));
            return Optional.empty();
        }
    }
}
