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

import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.renderer.util.DomUtils;
import de.cuioss.test.jsf.renderer.util.HtmlTreeAsserts;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.reflect.MoreReflection;
import de.cuioss.tools.string.MoreStrings;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockResponseWriter;
import org.jdom2.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base class for testing implementations of {@link Renderer}. It focuses on
 * conveniences and the basic-api contracts.
 * <h3>Configuration</h3>
 * <p>
 * Documentation on the setup of the JSF-related test-infrastructure can be
 * found at {@link EnableJsfEnvironment}
 * </p>
 * <p>
 * It acts as an {@link ConfigurationCallBackHandler}, saying after
 * initialization and prior to testing the method {@link #configure(Object)}
 * will be called allowing the concrete test-class to do some specific
 * configuration e.g. calling init-methods and such.
 * </p>
 * <p>
 * You can easily access pre-configured instance by calling
 * the getRenderer() method.
 * </p>
 * <h3>API-Tests</h3> Base {@linkplain Renderer} Test. Verify API contract of
 * Renderer for
 * <ul>
 * <li>{@linkplain Renderer#decode(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeBegin(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeChildren(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#encodeEnd(FacesContext, UIComponent)}</li>
 * <li>{@linkplain Renderer#convertClientId(FacesContext, String)}</li>
 * </ul>
 * <h3>Contracts</h3>
 * <ul>
 * <li>{@link #assertRenderResult(UIComponent, Document, FacesContext)} and
 * {@link #assertRenderResult(UIComponent, String, FacesContext)} are the main 'business'
 * methods for explicit testing</li>
 * </ul>
 *
 * @param <R> The renderer being tested
 * @author Oliver Wolff
 */
@EnableJsfEnvironment(useIdentityResourceBundle = true)
public abstract class AbstractRendererTestBase<R extends Renderer>
    implements ConfigurationCallBackHandler<R> {

    private static final String NPE_ON_MISSING_CLIENT_ID_EXPECTED = "NullPointerException expected on missing ClientId parameter. Use inheritance or implement own check.";

    private static final String NPE_ON_MISSING_PARAMETER_EXPECTED = "NullPointerException expected on missing UIComponent parameter. Use inheritance or implement own check.";

    private static final String NPE_ON_MISSING_FACES_CONTEXT_EXPECTED = "NullPointerException expected on missing FacesContext. Use inheritance or implement own check.";

    @Getter
    private R renderer;

    /**
     * Instantiates and initially configures a concrete {@link Renderer}
     */
    @BeforeEach
    void initRenderer(ComponentConfigDecorator decorator) {
        final Class<R> clazz = MoreReflection.extractFirstGenericTypeArgument(getClass());
        renderer = new DefaultInstantiator<>(clazz).newInstance();
        configure(renderer);
        if (clazz.isAnnotationPresent(FacesRenderer.class)) {
            decorator.registerRenderer(clazz);
        }
    }

    /**
     * @return the configured {@link UIComponent}. <em>Caution: </em> you must
     * always create a new instance of the component on each call
     */
    protected abstract UIComponent getComponent();

    /**
     * Renders the given component / renderer into a String representation
     *
     * @param toBeRendered the component to be passed to the renderer, must not be
     *                     null
     * @param facesContext the FacesContext to be used for rendering
     * @return the String-result of the rendering
     * @throws IOException if an error occurs during rendering
     */
    public String renderToString(final UIComponent toBeRendered, FacesContext facesContext) throws IOException {
        requireNonNull(toBeRendered);
        var output = new StringWriter();
        facesContext.setResponseWriter(new MockResponseWriter(output));
        final Renderer testRenderer = getRenderer();
        testRenderer.encodeBegin(facesContext, toBeRendered);
        testRenderer.encodeChildren(facesContext, toBeRendered);
        testRenderer.encodeEnd(facesContext, toBeRendered);
        return output.toString();
    }

    /**
     * Calls the renderer and checks the result against the given expected
     * {@link Document}
     *
     * @param toBeRendered the component to be passed to the renderer, must not be
     *                     null
     * @param expected     the expected DOM document, must not be null
     * @param facesContext the FacesContext to be used for rendering
     * @throws IOException if an error occurs during rendering
     */
    public void assertRenderResult(final UIComponent toBeRendered, final Document expected, FacesContext facesContext) throws IOException {
        var rendered = assertDoesNotThrow(() -> renderToString(toBeRendered, facesContext));
        assertNotNull(emptyToNull(rendered), "Render result must not be empty.");
        HtmlTreeAsserts.assertHtmlTreeEquals(expected, DomUtils.htmlStringToDocument(rendered));
    }

    /**
     * Shorthand for {@link #assertRenderResult(UIComponent, Document, FacesContext)} and
     * {@link DomUtils#htmlStringToDocument(String)}
     *
     * @param toBeRendered the component to be passed to the renderer, must not be
     *                     null
     * @param expected     the expected HTML string, must not be null
     * @param facesContext the FacesContext to be used for rendering
     * @throws IOException if an error occurs during rendering
     */
    public void assertRenderResult(final UIComponent toBeRendered, final String expected, FacesContext facesContext) throws IOException {
        assertNotNull(emptyToNull(expected), "Render result must not be empty.");
        assertRenderResult(toBeRendered, DomUtils.htmlStringToDocument(expected), facesContext);
    }

    /**
     * Assert, that the given component does not render any output.
     *
     * @param toBeRendered the component to be passed to the renderer, must not be
     *                     null
     * @param facesContext the FacesContext to be used for rendering
     */
    public void assertEmptyRenderResult(final UIComponent toBeRendered, FacesContext facesContext) {
        var rendered = assertDoesNotThrow(() -> renderToString(toBeRendered, facesContext));
        assertNull(MoreStrings.emptyToNull(rendered), "Render result must be empty, but is:\n" + rendered);
    }

    // API tests
    /**
     * Tests that the renderer's decode method properly throws NullPointerException
     * when null parameters are passed.
     *
     * @param facesContext the FacesContext to be used for testing
     */
    @Test
    void shouldThrowNPEOnMissingParameterForDecode(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> renderer.decode(null, getComponent()),
            NPE_ON_MISSING_FACES_CONTEXT_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.decode(facesContext, null),
            NPE_ON_MISSING_PARAMETER_EXPECTED);
    }

    /**
     * Tests that the renderer's encodeBegin method properly throws NullPointerException
     * when null parameters are passed.
     *
     * @param facesContext the FacesContext to be used for testing
     */
    @Test
    void shouldThrowNPEOnMissingParameterForEncodeBegin(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> renderer.encodeBegin(null, getComponent()),
            NPE_ON_MISSING_FACES_CONTEXT_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeBegin(facesContext, null));
    }

    /**
     * Tests that the renderer's encodeChildren method properly throws NullPointerException
     * when null parameters are passed.
     *
     * @param facesContext the FacesContext to be used for testing
     */
    @Test
    void shouldThrowNPEOnMissingParameterForEncodeChildren(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> renderer.encodeChildren(null, getComponent()),
            NPE_ON_MISSING_FACES_CONTEXT_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeChildren(facesContext, null),
            NPE_ON_MISSING_PARAMETER_EXPECTED);
    }

    /**
     * Tests that the renderer's convertClientId method properly throws NullPointerException
     * when null parameters are passed.
     *
     * @param facesContext the FacesContext to be used for testing
     */
    @Test
    void shouldThrowNPEOnMissingParameterForConvertClientId(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> renderer.convertClientId(null, "SomeId"),
            NPE_ON_MISSING_FACES_CONTEXT_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.convertClientId(facesContext, null),
            NPE_ON_MISSING_CLIENT_ID_EXPECTED);
    }

    /**
     * Tests that the renderer's encodeEnd method properly throws NullPointerException
     * when null parameters are passed.
     *
     * @param facesContext the FacesContext to be used for testing
     */
    @Test
    void shouldThrowNPEOnMissingParameterForEncodeEnd(FacesContext facesContext) {
        assertThrows(NullPointerException.class, () -> renderer.encodeEnd(null, getComponent()),
            NPE_ON_MISSING_FACES_CONTEXT_EXPECTED);
        assertThrows(NullPointerException.class, () -> renderer.encodeEnd(facesContext, null),
            NPE_ON_MISSING_PARAMETER_EXPECTED);
    }
}
