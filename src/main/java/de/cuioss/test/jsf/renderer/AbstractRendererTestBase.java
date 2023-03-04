package de.cuioss.test.jsf.renderer;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.apache.myfaces.test.mock.MockResponseWriter;
import org.jdom2.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.junit5.JsfEnabledTestEnvironment;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.test.jsf.renderer.util.HtmlTreeAsserts;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.Getter;

/**
 * Base class for testing implementations of {@link Renderer}. It focuses on conveniences and the
 * basic-api contracts.
 * <h3>Configuration</h3>
 * <p>
 * Documentation on the setup of the JSF-related test-infrastructure can be found at
 * {@link EnableJsfEnvironment}
 * </p>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after initialization and prior to
 * testing the method {@link #configure(Object)} will be called allowing the concrete
 * test-class to do some specific configuration e.g. calling init-methods and such.
 * </p>
 * <p>
 * You can easily access pre-configured instance by calling {@link #getRenderer()}.
 * </p>
 * <h3>API-Tests</h3>
 * Base {@linkplain Renderer} Test. Verify API contract of Renderer for
 * <ul>
 * <li>{@linkplain Renderer#decode(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeBegin(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeChildren(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeEnd(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#convertClientId(FacesContext, String)}</li>
 * </ul>
 * <h3>Contracts</h3>
 * <ul>
 * <li>{@link #assertRenderResult(UIComponent, Document)} and
 * {@link #assertRenderResult(UIComponent, String)} are the main
 * 'business' methods for explicit testing</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @param <R> The renderer being tested
 */
public abstract class AbstractRendererTestBase<R extends Renderer> extends JsfEnabledTestEnvironment
        implements ConfigurationCallBackHandler<R> {

    private static final String NPE_ON_MISSING_CLIENT_ID_EXPECTED =
        "NullPointerException expected on missing ClientId parameter. Use inheritance or implement own check.";

    private static final String NPE_ON_MISSING_PARAMETER_EXPECTED =
        "NullPointerException expected on missing UIComponent parameter. Use inheritance or implement own check.";

    private static final String NPE_ON_MSSING_FC_EXPECTED =
        "NullPointerException expected on missing FacesContext. Use inheritance or implement own check.";

    @Getter
    private R renderer;

    /**
     * Instantiates and initially configures a concrete {@link Renderer}
     */
    @BeforeEach
    void initRenderer() {
        final Class<R> klazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        renderer = new DefaultInstantiator<>(klazz).newInstance();
        configure(renderer);
        if (klazz.isAnnotationPresent(FacesRenderer.class)) {
            getComponentConfigDecorator().registerRenderer(klazz);
        }
    }

    /**
     * @return the configured {@link UIComponent}. <em>Caution: </em> you must always create a
     *         new instance of the component on each call
     */
    protected abstract UIComponent getComponent();

    /**
     * Renders the given component / renderer into a String representation
     *
     * @param toBeRendered the component to be passed to the renderer, must not be null
     * @return the String-result of the rendering
     */
    public String renderToString(final UIComponent toBeRendered) {
        requireNonNull(toBeRendered);
        var output = new StringWriter();
        getFacesContext().setResponseWriter(new MockResponseWriter(output));
        final Renderer testRenderer = getRenderer();
        assertDoesNotThrow(() -> {
            testRenderer.encodeBegin(getFacesContext(), toBeRendered);
            testRenderer.encodeChildren(getFacesContext(), toBeRendered);
            testRenderer.encodeEnd(getFacesContext(), toBeRendered);
        });
        return output.toString();
    }

    /**
     * Calls the renderer and checks the result against the given expected {@link Document}
     *
     * @param toBeRendered the component to be passed to the renderer, must not be null
     * @param expected must not be null
     */
    public void assertRenderResult(final UIComponent toBeRendered, final Document expected) {
        var rendered = renderToString(toBeRendered);
        assertNotNull(emptyToNull(rendered), "Render Result must not be empty.");
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, DomUtils.htmlStringToDocument(rendered));
    }

    /**
     * Shorthand for {@link #assertRenderResult(UIComponent, Document)} and
     * {@link DomUtils#htmlStringToDocument(String)}
     *
     * @param toBeRendered the component to be passed to the renderer, must not be null
     * @param expected must not be null
     */
    public void assertRenderResult(final UIComponent toBeRendered, final String expected) {
        assertNotNull(emptyToNull(expected), "Render Result must not be empty.");
        assertRenderResult(toBeRendered, DomUtils.htmlStringToDocument(expected));
    }

    // API tests
    @Test
    void shouldThrowNPEOnMissingParameterForDecode() {
        assertThrows(NullPointerException.class, () -> renderer.decode(null, getComponent()),
                NPE_ON_MSSING_FC_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.decode(getFacesContext(), null),
                NPE_ON_MISSING_PARAMETER_EXPECTED);
    }

    @Test
    void shouldThrowNPEOnMissingParameterForEncodeBegin() {
        assertThrows(NullPointerException.class, () -> renderer.encodeBegin(null, getComponent()),
                NPE_ON_MSSING_FC_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeBegin(getFacesContext(), null));
    }

    @Test
    void shouldThrowNPEOnMissingParameterForEncodeChildren() {
        assertThrows(NullPointerException.class, () -> renderer.encodeChildren(null, getComponent()),
                NPE_ON_MSSING_FC_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeChildren(getFacesContext(), null),
                NPE_ON_MISSING_PARAMETER_EXPECTED);
    }

    @Test
    void shouldThrowNPEOnMissingParameterForConvertClientId() {
        assertThrows(NullPointerException.class, () -> renderer.convertClientId(null, "SomeId"),
                NPE_ON_MSSING_FC_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.convertClientId(getFacesContext(), null),
                NPE_ON_MISSING_CLIENT_ID_EXPECTED);
    }

    @Test
    void shouldThrowNPEOnMissingParameterForEncodeEnd() {
        assertThrows(NullPointerException.class, () -> renderer.encodeEnd(null, getComponent()),
                NPE_ON_MSSING_FC_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeEnd(getFacesContext(), null),
                NPE_ON_MISSING_PARAMETER_EXPECTED);
    }
}
