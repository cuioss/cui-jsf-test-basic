package de.cuioss.test.jsf.util;

import java.net.URL;
import java.net.URLClassLoader;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockFacesContextFactory;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.apache.myfaces.test.mock.MockHttpSession;
import org.apache.myfaces.test.mock.MockRenderKit;
import org.apache.myfaces.test.mock.MockServletConfig;
import org.apache.myfaces.test.mock.MockServletContext;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory;

import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import de.cuioss.test.jsf.mocks.CuiMockHttpSession;
import de.cuioss.test.jsf.mocks.CuiMockSearchExpressionContextFactory;
import de.cuioss.test.jsf.mocks.CuiMockUIViewRoot;
import lombok.Getter;
import lombok.Setter;

/**
 * Replacement for MyFaces {@link org.apache.myfaces.test.base.junit4.AbstractJsfTestCase}, where
 * the code is initially taken from
 *
 * @author Oliver Wolff
 *
 */
public class JsfRuntimeSetup {

    @Getter
    @Setter
    private Application application = null;

    @Getter
    @Setter
    private MockServletConfig config = null;

    @Getter
    @Setter
    private MockExternalContext externalContext = null;

    @Getter
    @Setter
    private MockFacesContext facesContext = null;

    @Getter
    @Setter
    private MockFacesContextFactory facesContextFactory = null;

    @Getter
    @Setter
    private MockLifecycle lifecycle = null;

    @Getter
    @Setter
    private MockLifecycleFactory lifecycleFactory = null;

    @Getter
    @Setter
    private MockRenderKit renderKit = null;

    @Getter
    @Setter
    private MockHttpServletRequest request = null;

    @Getter
    @Setter
    private MockHttpServletResponse response = null;

    @Getter
    @Setter
    private MockServletContext servletContext = null;

    @Getter
    @Setter
    private MockHttpSession session = null;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader = null;
    private boolean classLoaderSet = false;

    /**
     * <p>
     * Set up instance variables required by this test case.
     * </p>
     */
    public void setUp() {
        // Set up a new thread context class loader
        setUpClassloader();

        // Set up Servlet API Objects
        setUpServletObjects();

        // Set up JSF API Objects
        FactoryFinder.releaseFactories();

        setFactories();

        setUpJSFObjects();
    }

    /**
     * <p>
     * Tear down instance variables required by this test case.
     * </p>
     */
    public void tearDown() {
        application = null;
        config = null;
        externalContext = null;
        if (facesContext != null) {
            facesContext.release();
        }
        facesContext = null;
        lifecycle = null;
        lifecycleFactory = null;
        renderKit = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;
        FactoryFinder.releaseFactories();
        ResourceBundleVarNames.resetNames();

        tearDownClassloader();
    }

    /**
     * Set up the thread context classloader. JSF uses the this classloader
     * in order to find related factory classes and other resources, but in
     * some selected cases, the default classloader cannot be properly set.
     *
     */
    @SuppressWarnings("resource") // owolff: No problem in test context
    private void setUpClassloader() {
        threadContextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread()
        .setContextClassLoader(
                new URLClassLoader(new URL[0], this.getClass()
                        .getClassLoader()));
        classLoaderSet = true;
    }

    /**
     * <p>
     * Setup JSF object used for the test. By default it calls to the following
     * methods in this order:
     * </p>
     *
     * <ul>
     * <li><code>setUpExternalContext();</code></li>
     * <li><code>setUpLifecycle();</code></li>
     * <li><code>setUpFacesContext();</code></li>
     * <li><code>setUpView();</code></li>
     * <li><code>setUpApplication();</code></li>
     * <li><code>setUpRenderKit();</code></li>
     * </ul>
     */
    private void setUpJSFObjects() {
        setUpExternalContext();
        setUpLifecycle();
        setUpFacesContext();
        setUpView();
        setUpApplication();
        setUpRenderKit();
    }

    /**
     * <p>
     * Setup servlet objects that will be used for the test:
     * </p>
     *
     * <ul>
     * <li><code>config</code> (<code>MockServletConfig</code>)</li>
     * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
     * <li><code>request</code> (<code>CuiMockHttpServletRequest</code></li>
     * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
     * <li><code>session</code> (<code>CuiMockHttpSession</code>)</li>
     * </ul>
     *
     */
    private void setUpServletObjects() {
        servletContext = new MockServletContext();
        config = new MockServletConfig(servletContext);
        session = new CuiMockHttpSession(servletContext);
        request = new CuiMockHttpServletRequest();
        request.setHttpSession(session);
        request.setServletContext(servletContext);
        response = new MockHttpServletResponse();
    }

    /**
     * <p>
     * Set JSF factories using FactoryFinder method setFactory.
     * </p>
     *
     */
    private void setFactories() {
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                "org.apache.myfaces.test.mock.MockApplicationFactory");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.MockFacesContextFactory");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                "org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                "org.apache.myfaces.test.mock.MockRenderKitFactory");
        FactoryFinder.setFactory(FactoryFinder.EXCEPTION_HANDLER_FACTORY,
                "org.apache.myfaces.test.mock.MockExceptionHandlerFactory");
        FactoryFinder.setFactory(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.MockPartialViewContextFactory");
        FactoryFinder.setFactory(FactoryFinder.VISIT_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.visit.MockVisitContextFactory");
        FactoryFinder.setFactory(FactoryFinder.CLIENT_WINDOW_FACTORY,
                "org.apache.myfaces.test.mock.MockClientWindowFactory");
        // Cui Extensions
        FactoryFinder.setFactory(FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY,
                CuiMockSearchExpressionContextFactory.class.getName());
    }

    /**
     * Setup the <code>externalContext</code> variable, using the
     * servlet variables already initialized.
     *
     */
    private void setUpExternalContext() {
        externalContext = new MockExternalContext(servletContext, request,
                response);
    }

    /**
     * Setup the <code>lifecycle</code> and <code>lifecycleFactory</code>
     * variables.
     *
     */
    private void setUpLifecycle() {
        lifecycleFactory = (MockLifecycleFactory) FactoryFinder
                .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle) lifecycleFactory
                .getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }

    /**
     * Setup the <code>facesContextFactory</code> and <code>facesContext</code>
     * variable. Before end, by default it override <code>externalContext</code>
     * variable from the value retrieved from facesContext.getExternalContext(),
     * because sometimes it is possible facesContext overrides externalContext
     * internally.
     *
     */
    private void setUpFacesContext() {
        facesContextFactory = (MockFacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext) facesContextFactory.getFacesContext(
                servletContext, request, response, lifecycle);
        if (facesContext.getExternalContext() != null) {
            externalContext = (MockExternalContext) facesContext
                    .getExternalContext();
        }
    }

    /**
     * By default, create an instance of UIViewRoot, set its viewId as "/viewId"
     * and assign it to the current facesContext.
     *
     */
    private void setUpView() {
        UIViewRoot root = new CuiMockUIViewRoot();
        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);
    }

    /**
     * Setup the <code>application</code> variable and before
     * the end by default it is assigned to the <code>facesContext</code>
     * variable, calling <code>facesContext.setApplication(application)</code>
     *
     */
    private void setUpApplication() {
        var applicationFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = applicationFactory.getApplication();
        facesContext.setApplication(application);
    }

    /**
     * Setup the <code>renderKit</code> variable. This is a good place to use
     * <code>ConfigParser</code> to register converters, validators, components
     * or renderkits.
     *
     */
    private void setUpRenderKit() {
        var renderKitFactory = (RenderKitFactory) FactoryFinder
                .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                renderKit);
    }

    private void tearDownClassloader() {
        if (classLoaderSet) {
            Thread.currentThread().setContextClassLoader(threadContextClassLoader);
            threadContextClassLoader = null;
            classLoaderSet = false;
        }
    }
}
