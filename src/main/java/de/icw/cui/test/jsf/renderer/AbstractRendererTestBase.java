package de.icw.cui.test.jsf.renderer;

import static io.cui.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.apache.myfaces.test.mock.MockResponseWriter;
import org.jdom2.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.junit5.EnableJsfEnvironment;
import de.icw.cui.test.jsf.junit5.JsfEnabledTestEnvironment;
import de.icw.cui.test.jsf.renderer.util.DomUtils;
import de.icw.cui.test.jsf.renderer.util.HtmlTreeAsserts;
import io.cui.test.valueobjects.objects.ConfigurationCallBackHandler;
import io.cui.test.valueobjects.objects.impl.ExceptionHelper;
import io.cui.tools.reflect.MoreReflection;
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
 * <ul>
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
    public void initRenderer() {
        final Class<R> klazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        try {
            renderer = klazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Unable to instantiate renderer, due to " + ExceptionHelper.extractCauseMessageFromThrowable(e));
        }
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
    @SuppressWarnings("resource")
    public String renderToString(final UIComponent toBeRendered) {
        requireNonNull(toBeRendered);
        StringWriter output = new StringWriter();
        getFacesContext().setResponseWriter(new MockResponseWriter(output));
        final Renderer testRenderer = getRenderer();
        try {
            testRenderer.encodeBegin(getFacesContext(), toBeRendered);
            testRenderer.encodeChildren(getFacesContext(), toBeRendered);
            testRenderer.encodeEnd(getFacesContext(), toBeRendered);
        } catch (IOException e) {
            fail("Unable to render du to IOException " + e.getMessage());
        }
        return output.toString();
    }

    /**
     * Calls the renderer and checks the result against the given expected {@link Document}
     *
     * @param toBeRendered the component to be passed to the renderer, must not be null
     * @param expected must not be null
     */
    public void assertRenderResult(final UIComponent toBeRendered, final Document expected) {
        String rendered = renderToString(toBeRendered);
        assertNotNull("Render Result must not be empty.", emptyToNull(rendered));
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
        assertNotNull("Render Result must not be empty.", emptyToNull(expected));
        assertRenderResult(toBeRendered, DomUtils.htmlStringToDocument(expected));
    }

    // API tests
    @Test
    // There is no need for exception handling here:
    @SuppressWarnings({ "squid:S1166" })
    public // see comment: expected
    void shouldThrowNPEOnMissingParameterForDecode() {
        try {
            renderer.decode(null, getComponent());
            fail(NPE_ON_MSSING_FC_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
        try {
            renderer.decode(getFacesContext(), null);
            fail(NPE_ON_MISSING_PARAMETER_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
    }

    /**
     * @throws IOException
     */
    @Test
    // There is no need for exception handling here:
    @SuppressWarnings({ "squid:S1166" })
    public // see comment: expected
    void shouldThrowNPEOnMissingParameterForEncodeBegin() throws IOException {
        try {
            renderer.encodeBegin(null, getComponent());
            fail(NPE_ON_MSSING_FC_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
        try {
            renderer.encodeBegin(getFacesContext(), null);
            fail(NPE_ON_MISSING_PARAMETER_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
    }

    /**
     * @throws IOException
     */
    @Test
    // There is no need for exception handling here:
    @SuppressWarnings({ "squid:S1166" })
    public // see comment: expected
    void shouldThrowNPEOnMissingParameterForEncodeChildren() throws IOException {
        try {
            renderer.encodeChildren(null, getComponent());
            fail(NPE_ON_MSSING_FC_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
        try {
            renderer.encodeChildren(getFacesContext(), null);
            fail(NPE_ON_MISSING_PARAMETER_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
    }

    @Test
    // There is no need for exception handling here:
    @SuppressWarnings({ "squid:S1166" })
    public // see comment: expected
    void shouldThrowNPEOnMissingParameterForConvertClientId() {
        final String someId = "SomeId";
        try {
            renderer.convertClientId(null, someId);
            fail(NPE_ON_MSSING_FC_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
        try {
            renderer.convertClientId(getFacesContext(), null);
            fail(NPE_ON_MISSING_CLIENT_ID_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
    }

    /**
     * @throws IOException
     */
    @Test
    // There is no need for exception handling here:
    @SuppressWarnings({ "squid:S1166" })
    public // see comment: expected
    void shouldThrowNPEOnMissingParameterForEncodeEnd() throws IOException {
        try {
            renderer.encodeEnd(null, getComponent());
            fail(NPE_ON_MSSING_FC_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
        try {
            renderer.encodeEnd(getFacesContext(), null);
            fail(NPE_ON_MISSING_PARAMETER_EXPECTED);
        } catch (final NullPointerException e) {
            // expected
        }
    }
}
