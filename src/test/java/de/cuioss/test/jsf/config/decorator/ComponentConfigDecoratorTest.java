package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.FORM_RENDERER_ID;
import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.TEXT_RENDERER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.IntegerConverter;
import javax.faces.render.Renderer;
import javax.faces.validator.LengthValidator;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.junit.Before;
import org.junit.Test;

import de.cuioss.test.jsf.mocks.CuiMockComponent;
import de.cuioss.test.jsf.support.components.BehaviorWithAnnotation;
import de.cuioss.test.jsf.support.components.BehaviorWithoutAnnotation;
import de.cuioss.test.jsf.support.components.ConverterWithConverterIdAnnotation;
import de.cuioss.test.jsf.support.components.ConverterWithTypeAnnotation;
import de.cuioss.test.jsf.support.components.RendererWithAnnotation;
import de.cuioss.test.jsf.support.components.UiComponentWithAnnotation;
import de.cuioss.test.jsf.support.components.ValidatorWithAnnotation;

@SuppressWarnings("javadoc")
class ComponentConfigDecoratorTest extends AbstractJsfTestCase {

    private ComponentConfigDecorator decorator;

    @Before
    public void before() {
        decorator = new ComponentConfigDecorator(application, facesContext);
    }

    // UIComponent related methods
    @Test
    public void shouldRegisterUIComponentWithId() {
        assertUIComponentIsNotRegistered(HtmlInputText.COMPONENT_TYPE);
        decorator.registerUIComponent(HtmlInputText.COMPONENT_TYPE, HtmlInputText.class);
        assertNotNull(application.createComponent(HtmlInputText.COMPONENT_TYPE));
        assertEquals(HtmlInputText.class,
                application.createComponent(HtmlInputText.COMPONENT_TYPE).getClass());
    }

    @Test
    public void shouldRegisterUIComponentWithAnnotation() {
        assertUIComponentIsNotRegistered(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE);
        decorator.registerUIComponent(UiComponentWithAnnotation.class);
        assertNotNull(
                application.createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE));
        assertEquals(UiComponentWithAnnotation.class,
                application.createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE)
                        .getClass());
    }

    // Renderer related methods
    @Test
    public void shouldRegisterRendererByFamilyAndId() {
        assertRendererIsNotRegistered(RendererWithAnnotation.COMPONENT_FAMILY,
                RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
                RendererWithAnnotation.RENDERER_TYPE, new RendererWithAnnotation());

        final var renderer =
            facesContext.getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
                    RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    public void shouldRegisterRendererByAnnotation() {
        assertRendererIsNotRegistered(RendererWithAnnotation.COMPONENT_FAMILY,
                RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.class);

        final var renderer =
            facesContext.getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
                    RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    public void shouldRegisterMockRenderer() {
        assertRendererIsNotRegistered(UIForm.COMPONENT_FAMILY,
                HtmlForm.COMPONENT_TYPE);
        decorator.registerMockRenderer(UIForm.COMPONENT_FAMILY,
                HtmlForm.COMPONENT_TYPE);
        assertNotNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY,
                HtmlForm.COMPONENT_TYPE));
    }

    @Test
    public void shouldRegisterCuiMockComponent() {
        assertUIComponentIsNotRegistered(CuiMockComponent.COMPONENT_TYPE);

        assertRendererIsNotRegistered(CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE);
        decorator.registerCuiMockComponentWithRenderer();
        assertNotNull(facesContext.getRenderKit().getRenderer(CuiMockComponent.FAMILY,
                CuiMockComponent.RENDERER_TYPE));
        assertNotNull(application.createComponent(CuiMockComponent.COMPONENT_TYPE));
        assertEquals(CuiMockComponent.class, application.createComponent(CuiMockComponent.COMPONENT_TYPE).getClass());
    }

    @Test
    public void shouldRegisterMockRendererForHtmlOutput() {
        assertRendererIsNotRegistered(UIOutput.COMPONENT_FAMILY,
                TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlOutputText();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIOutput.COMPONENT_FAMILY,
                TEXT_RENDERER_ID));
    }

    @Test
    public void shouldRegisterMockRendererForHtmlInput() {
        assertRendererIsNotRegistered(UIInput.COMPONENT_FAMILY,
                TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlInputText();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIInput.COMPONENT_FAMILY,
                TEXT_RENDERER_ID));
    }

    @Test
    public void shouldRegisterMockRendererForHtmlForm() {
        assertRendererIsNotRegistered(UIForm.COMPONENT_FAMILY,
                FORM_RENDERER_ID);
        decorator.registerMockRendererForHtmlForm();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY,
                FORM_RENDERER_ID));
    }

    @Test
    public void shouldRegisterMockRendererForHtmlSelectBooleanCheckbox() {
        assertRendererIsNotRegistered(UISelectBoolean.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTBOOLEAN_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectBooleanCheckbox();
        assertNotNull(facesContext.getRenderKit().getRenderer(UISelectBoolean.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTBOOLEAN_RENDERER_ID));
    }

    @Test
    public void shouldRegisterMockRendererForHtmlSelectOneRadio() {
        assertRendererIsNotRegistered(UISelectOne.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTONE_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectOneRadio();
        assertNotNull(facesContext.getRenderKit().getRenderer(UISelectOne.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTONE_RENDERER_ID));
    }

    // Converter related methods
    @Test
    public void shouldRegisterConverterWithId() {
        assertConverterForIdIsNotRegistered(IntegerConverter.CONVERTER_ID);
        decorator.registerConverter(IntegerConverter.class, IntegerConverter.CONVERTER_ID);
        assertNotNull(application.createConverter(IntegerConverter.CONVERTER_ID));
        assertEquals(IntegerConverter.class,
                application.createConverter(IntegerConverter.CONVERTER_ID).getClass());
    }

    @Test
    public void shouldRegisterConverterWithType() {
        assertConverterForTypeIsNotRegistered(Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class, Serializable.class);
        assertNotNull(application.createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
                application.createConverter(Serializable.class).getClass());
    }

    @Test
    public void shouldRegisterConverterWithAnnotatetdType() {
        assertConverterForTypeIsNotRegistered(Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class);
        assertNotNull(application.createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
                application.createConverter(Serializable.class).getClass());
    }

    @Test
    public void shouldRegisterConverterWithAnnotatedId() {
        assertConverterForIdIsNotRegistered(ConverterWithConverterIdAnnotation.CONVERTER_ID);
        decorator.registerConverter(ConverterWithConverterIdAnnotation.class);
        assertNotNull(application.createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID));
        assertEquals(ConverterWithConverterIdAnnotation.class,
                application.createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID)
                        .getClass());
    }

    // Validator related methods
    @Test
    public void shouldRegisterValidatorWithId() {
        assertValidatorIsNotRegistered(LengthValidator.VALIDATOR_ID);
        decorator.registerValidator(LengthValidator.VALIDATOR_ID, LengthValidator.class);
        assertNotNull(application.createValidator(LengthValidator.VALIDATOR_ID));
        assertEquals(LengthValidator.class,
                application.createValidator(LengthValidator.VALIDATOR_ID).getClass());
    }

    @Test
    public void shouldRegisterValidatorWithValidatorAnnotation() {
        assertValidatorIsNotRegistered(ValidatorWithAnnotation.VALIDATOR_ID);
        decorator.registerValidator(ValidatorWithAnnotation.class);
        assertNotNull(application.createValidator(ValidatorWithAnnotation.VALIDATOR_ID));
        assertEquals(ValidatorWithAnnotation.class,
                application.createValidator(ValidatorWithAnnotation.VALIDATOR_ID).getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToRegisterValidatorWithMissingAnnotation() {
        assertValidatorIsNotRegistered(LengthValidator.VALIDATOR_ID);
        decorator.registerValidator(LengthValidator.class);
    }

    // Client Behavior related
    @Test
    public void shouldRegisterBehaviorWithId() {
        assertBehaviorIsNotRegistered(BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.BEHAVIOR_ID,
                BehaviorWithAnnotation.class);
        final var behavior = application.createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(behavior.getClass(), BehaviorWithAnnotation.class);
    }

    @Test
    public void shouldRegisterBehaviorWithAnnotation() {
        assertBehaviorIsNotRegistered(BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.class);
        final var behavior = application.createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(behavior.getClass(), BehaviorWithAnnotation.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToRegisterBehaviorWithMissingAnnotation() {
        assertBehaviorIsNotRegistered(BehaviorWithoutAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithoutAnnotation.class);
    }

    @Test
    public void testAddComponent() {
        final UIComponent input = new HtmlInputText();
        decorator.addUiComponent("test:id", input);
        assertEquals(input, FacesContext.getCurrentInstance().getViewRoot().findComponent("test:id"));
    }

    private void assertValidatorIsNotRegistered(final String validatorId) {
        try {
            application.createValidator(validatorId);
            fail("Validator is registered " + validatorId);
        } catch (final RuntimeException e) {
            // expected
        }
    }

    private void assertBehaviorIsNotRegistered(final String behaviorId) {
        try {
            application.createBehavior(behaviorId);
            fail("Validator is registered " + behaviorId);
        } catch (final RuntimeException e) {
            // expected
        }
    }

    private void assertConverterForIdIsNotRegistered(final String converterId) {
        try {
            final Converter<?> converter = application.createConverter(converterId);
            assertNull("Converter is registered " + converterId, converter);
        } catch (final RuntimeException e) {
            // expected
        }
    }

    private void assertConverterForTypeIsNotRegistered(final Class<?> target) {
        final Converter<?> converter = application.createConverter(target);
        assertNull("Converter is registered " + target, converter);
    }

    private void assertUIComponentIsNotRegistered(final String componentType) {
        UIComponent component;
        try {
            component = application.createComponent(componentType);
            assertNull("UIComponent is registered " + componentType, component);
        } catch (final Exception e) {
            // expected
        }
    }

    private void assertRendererIsNotRegistered(final String family, final String rendererType) {
        Renderer renderer;
        try {
            renderer = facesContext.getRenderKit().getRenderer(family, rendererType);
            assertNull("Renderer is registered " + rendererType + ", " + family, renderer);
        } catch (final Exception e) {
            // expected
        }
    }
}
