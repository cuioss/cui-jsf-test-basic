package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.FORM_RENDERER_ID;
import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.TEXT_RENDERER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import javax.faces.validator.LengthValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.mocks.CuiMockComponent;
import de.cuioss.test.jsf.mocks.ReverseConverter;
import de.cuioss.test.jsf.support.components.BehaviorWithAnnotation;
import de.cuioss.test.jsf.support.components.BehaviorWithoutAnnotation;
import de.cuioss.test.jsf.support.components.ConverterWithConverterIdAnnotation;
import de.cuioss.test.jsf.support.components.ConverterWithTypeAnnotation;
import de.cuioss.test.jsf.support.components.RendererWithAnnotation;
import de.cuioss.test.jsf.support.components.UiComponentWithAnnotation;
import de.cuioss.test.jsf.support.components.ValidatorWithAnnotation;
import de.cuioss.test.jsf.util.ConfigurableFacesTest;

class ComponentConfigDecoratorTest extends ConfigurableFacesTest {

    private ComponentConfigDecorator decorator;

    @BeforeEach
    void before() {
        decorator = new ComponentConfigDecorator(getApplication(), getFacesContext());
    }

    // UIComponent related methods
    @Test
    void shouldRegisterUIComponentWithId() {
        assertUIComponentIsNotRegistered(HtmlInputText.COMPONENT_TYPE);
        decorator.registerUIComponent(HtmlInputText.COMPONENT_TYPE, HtmlInputText.class);
        assertNotNull(getApplication().createComponent(HtmlInputText.COMPONENT_TYPE));
        assertEquals(HtmlInputText.class, getApplication().createComponent(HtmlInputText.COMPONENT_TYPE).getClass());
    }

    @Test
    void shouldRegisterUIComponentWithAnnotation() {
        assertUIComponentIsNotRegistered(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE);
        decorator.registerUIComponent(UiComponentWithAnnotation.class);
        assertNotNull(getApplication().createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE));
        assertEquals(UiComponentWithAnnotation.class,
                getApplication().createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE).getClass());
    }

    // Renderer related methods
    @Test
    void shouldRegisterRendererByFamilyAndId() {
        assertRendererIsNotRegistered(RendererWithAnnotation.COMPONENT_FAMILY, RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.COMPONENT_FAMILY, RendererWithAnnotation.RENDERER_TYPE,
                new RendererWithAnnotation());

        final var renderer = getFacesContext().getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
                RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    void shouldRegisterRendererByAnnotation() {
        assertRendererIsNotRegistered(RendererWithAnnotation.COMPONENT_FAMILY, RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.class);

        final var renderer = getFacesContext().getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
                RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    void shouldRegisterMockRenderer() {
        assertRendererIsNotRegistered(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE);
        decorator.registerMockRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE);
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE));
    }

    @Test
    void shouldRegisterCuiMockComponent() {
        assertUIComponentIsNotRegistered(CuiMockComponent.COMPONENT_TYPE);

        assertRendererIsNotRegistered(CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE);
        decorator.registerCuiMockComponentWithRenderer();
        assertNotNull(
                getFacesContext().getRenderKit().getRenderer(CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE));
        assertNotNull(getApplication().createComponent(CuiMockComponent.COMPONENT_TYPE));
        assertEquals(CuiMockComponent.class,
                getApplication().createComponent(CuiMockComponent.COMPONENT_TYPE).getClass());
    }

    @Test
    void shouldRegisterMockRendererForHtmlOutput() {
        assertRendererIsNotRegistered(UIOutput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlOutputText();
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UIOutput.COMPONENT_FAMILY, TEXT_RENDERER_ID));
    }

    @Test
    void shouldRegisterMockRendererForHtmlInput() {
        assertRendererIsNotRegistered(UIInput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlInputText();
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UIInput.COMPONENT_FAMILY, TEXT_RENDERER_ID));
    }

    @Test
    void shouldRegisterMockRendererForHtmlForm() {
        assertRendererIsNotRegistered(UIForm.COMPONENT_FAMILY, FORM_RENDERER_ID);
        decorator.registerMockRendererForHtmlForm();
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, FORM_RENDERER_ID));
    }

    @Test
    void shouldRegisterMockRendererForHtmlSelectBooleanCheckbox() {
        assertRendererIsNotRegistered(UISelectBoolean.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTBOOLEAN_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectBooleanCheckbox();
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UISelectBoolean.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTBOOLEAN_RENDERER_ID));
    }

    @Test
    void shouldRegisterMockRendererForHtmlSelectOneRadio() {
        assertRendererIsNotRegistered(UISelectOne.COMPONENT_FAMILY, ComponentConfigDecorator.SELECTONE_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectOneRadio();
        assertNotNull(getFacesContext().getRenderKit().getRenderer(UISelectOne.COMPONENT_FAMILY,
                ComponentConfigDecorator.SELECTONE_RENDERER_ID));
    }

    // Converter related methods
    @Test
    void shouldRegisterConverterWithId() {
        assertConverterForIdIsNotRegistered(ReverseConverter.CONVERTER_ID);
        decorator.registerConverter(ReverseConverter.class, ReverseConverter.CONVERTER_ID);
        assertNotNull(getApplication().createConverter(ReverseConverter.CONVERTER_ID));
        assertEquals(ReverseConverter.class,
                getApplication().createConverter(ReverseConverter.CONVERTER_ID).getClass());
    }

    @Test
    void shouldRegisterConverterWithType() {
        assertConverterForTypeIsNotRegistered(Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class, Serializable.class);
        assertNotNull(getApplication().createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
                getApplication().createConverter(Serializable.class).getClass());
    }

    @Test
    void shouldRegisterConverterWithAnnotatetdType() {
        assertConverterForTypeIsNotRegistered(Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class);
        assertNotNull(getApplication().createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
                getApplication().createConverter(Serializable.class).getClass());
    }

    @Test
    void shouldRegisterConverterWithAnnotatedId() {
        assertConverterForIdIsNotRegistered(ConverterWithConverterIdAnnotation.CONVERTER_ID);
        decorator.registerConverter(ConverterWithConverterIdAnnotation.class);
        assertNotNull(getApplication().createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID));
        assertEquals(ConverterWithConverterIdAnnotation.class,
                getApplication().createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID).getClass());
    }

    // Validator related methods
    @Test
    void shouldRegisterValidatorWithId() {
        assertValidatorIsNotRegistered(LengthValidator.VALIDATOR_ID);
        decorator.registerValidator(LengthValidator.VALIDATOR_ID, LengthValidator.class);
        assertNotNull(getApplication().createValidator(LengthValidator.VALIDATOR_ID));
        assertEquals(LengthValidator.class, getApplication().createValidator(LengthValidator.VALIDATOR_ID).getClass());
    }

    @Test
    void shouldRegisterValidatorWithValidatorAnnotation() {
        assertValidatorIsNotRegistered(ValidatorWithAnnotation.VALIDATOR_ID);
        decorator.registerValidator(ValidatorWithAnnotation.class);
        assertNotNull(getApplication().createValidator(ValidatorWithAnnotation.VALIDATOR_ID));
        assertEquals(ValidatorWithAnnotation.class,
                getApplication().createValidator(ValidatorWithAnnotation.VALIDATOR_ID).getClass());
    }

    @Test
    void shouldFailToRegisterValidatorWithMissingAnnotation() {
        assertValidatorIsNotRegistered(LengthValidator.VALIDATOR_ID);
        assertThrows(IllegalArgumentException.class, () -> decorator.registerValidator(LengthValidator.class));
    }

    // Client Behavior related
    @Test
    void shouldRegisterBehaviorWithId() {
        assertBehaviorIsNotRegistered(BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.BEHAVIOR_ID, BehaviorWithAnnotation.class);
        final var behavior = getApplication().createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(behavior.getClass(), BehaviorWithAnnotation.class);
    }

    @Test
    void shouldRegisterBehaviorWithAnnotation() {
        assertBehaviorIsNotRegistered(BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.class);
        final var behavior = getApplication().createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(behavior.getClass(), BehaviorWithAnnotation.class);
    }

    @Test
    void shouldFailToRegisterBehaviorWithMissingAnnotation() {
        assertBehaviorIsNotRegistered(BehaviorWithoutAnnotation.BEHAVIOR_ID);
        assertThrows(IllegalArgumentException.class, () -> decorator.registerBehavior(BehaviorWithoutAnnotation.class));
    }

    @Test
    void testAddComponent() {
        final UIComponent input = new HtmlInputText();
        decorator.addUiComponent("test:id", input);
        assertEquals(input, getFacesContext().getViewRoot().findComponent("test:id"));
    }

    private void assertValidatorIsNotRegistered(final String validatorId) {
        var application = getApplication();
        assertThrows(FacesException.class, () -> application.createValidator(validatorId));
    }

    private void assertBehaviorIsNotRegistered(final String behaviorId) {
        var application = getApplication();
        assertThrows(FacesException.class, () -> application.createBehavior(behaviorId));
    }

    private void assertConverterForIdIsNotRegistered(final String converterId) {
        var application = getApplication();
        assertNull(application.createConverter(converterId));
    }

    private void assertConverterForTypeIsNotRegistered(final Class<?> target) {
        final Converter<?> converter = getApplication().createConverter(target);
        assertNull(converter, "Converter is registered " + target);
    }

    private void assertUIComponentIsNotRegistered(final String componentType) {
        var application = getApplication();
        assertThrows(FacesException.class, () -> application.createComponent(componentType));

    }

    private void assertRendererIsNotRegistered(final String family, final String rendererType) {
        Renderer renderer;

        renderer = getFacesContext().getRenderKit().getRenderer(family, rendererType);
        assertNull(renderer, "Renderer is registered " + rendererType + ", " + family);
    }
}
