package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.faces.application.ProjectStage;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.apache.myfaces.test.mock.MockApplication12;
import org.junit.Before;
import org.junit.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.util.ConfigurableApplication;

@SuppressWarnings("javadoc")
class ApplicationConfigDecoratorTest extends AbstractJsfTestCase {

    private static final String BUNDLE_PATH = "de.cuioss.test.jsf.testbundle";
    private static final String BUNDLE_NAME = "testbundle";

    private static final String INVALID_BUNDLE_PATH = "de.cuioss.test.jsf.notthere";
    private static final String INVALID_BUNDLE_NAME = "invalidbundle";

    private ApplicationConfigDecorator decorator;

    @Before
    public void before() {
        decorator = new ApplicationConfigDecorator(
                application, facesContext);
    }

    @Test
    public void shouldRegisterResourceBundle() {
        assertNull(application.getResourceBundle(facesContext, BUNDLE_NAME));

        decorator.registerResourceBundle(BUNDLE_NAME, BUNDLE_PATH);

        assertNotNull(application.getResourceBundle(facesContext, BUNDLE_NAME));

        assertNull(application.getResourceBundle(facesContext, INVALID_BUNDLE_NAME));

        decorator.registerResourceBundle(INVALID_BUNDLE_NAME, INVALID_BUNDLE_PATH);

        assertNull(application.getResourceBundle(facesContext, INVALID_BUNDLE_NAME));
    }

    @Test
    public void shouldRegisterSupportedLocales() {
        assertFalse(application.getSupportedLocales().hasNext());

        final var locale = Generators.locales().next();
        decorator.registerSupportedLocales(immutableSet(locale));

        assertEquals(locale, application.getSupportedLocales().next());
    }

    @Test
    public void shouldRegisterDefaultLocale() {
        assertNotNull(application.getDefaultLocale());

        final var locale = Generators.locales().next();
        decorator.registerDefaultLocale(locale);

        assertEquals(locale, application.getDefaultLocale());
    }

    @Test
    public void shouldRegisterNavigationCase() {
        decorator.registerNavigationCase("outcome", "/toViewId");
        application.getNavigationHandler().handleNavigation(facesContext, null, "outcome");
        assertEquals("/toViewId", facesContext.getViewRoot().getViewId());
    }

    @Test
    public void shouldRegisterContextPath() {
        decorator.setContextPath("hello");
        assertEquals("hello", request.getContextPath());
    }

    @Test
    public void shouldSetProjectStage() {
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, application.getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, application.getProjectStage());
    }

    @Test
    public void shouldSetInitParameter() {
        final var key = "initKey";
        final var value = "initValue";
        assertNull(facesContext.getExternalContext().getInitParameter(key));

        decorator.addInitParameter(key, value);
        assertEquals(value, facesContext.getExternalContext().getInitParameter(key));
    }

    @Test
    public void shouldSetProjectStageThroughWrapper() {
        decorator =
            new ApplicationConfigDecorator(new ConfigurableApplication(application), facesContext);
        decorator.setProjectStage(ProjectStage.Development);
        assertEquals(ProjectStage.Development, application.getProjectStage());

        decorator.setProjectStage(ProjectStage.Production);
        assertEquals(ProjectStage.Production, application.getProjectStage());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailToSetProjectWithInvalidApplication() {
        decorator =
            new ApplicationConfigDecorator(new ConfigurableApplication(new MockApplication12()),
                    facesContext);
        decorator.setProjectStage(ProjectStage.Development);
    }
}
