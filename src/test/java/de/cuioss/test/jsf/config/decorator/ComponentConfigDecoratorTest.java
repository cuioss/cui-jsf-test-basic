/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.config.decorator;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockComponent;
import de.cuioss.test.jsf.mocks.ReverseConverter;
import de.cuioss.test.jsf.support.components.*;
import jakarta.faces.FacesException;
import jakarta.faces.application.Application;
import jakarta.faces.component.*;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlForm;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.render.Renderer;
import jakarta.faces.validator.LengthValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.FORM_RENDERER_ID;
import static de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator.TEXT_RENDERER_ID;
import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@DisplayName("ComponentConfigDecorator")
class ComponentConfigDecoratorTest {

    // UIComponent related methods
    @Test
    @DisplayName("Should register a UIComponent with an explicit id")
    void shouldRegisterUIComponentWithId(ComponentConfigDecorator decorator, Application application) {
        assertUIComponentIsNotRegistered(application, HtmlInputText.COMPONENT_TYPE);
        decorator.registerUIComponent(HtmlInputText.COMPONENT_TYPE, HtmlInputText.class);
        assertNotNull(application.createComponent(HtmlInputText.COMPONENT_TYPE));
        assertEquals(HtmlInputText.class, application.createComponent(HtmlInputText.COMPONENT_TYPE).getClass());
    }

    @Test
    @DisplayName("Should register a UIComponent from its annotation")
    void shouldRegisterUIComponentWithAnnotation(ComponentConfigDecorator decorator, Application application) {
        assertUIComponentIsNotRegistered(application, UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE);
        decorator.registerUIComponent(UiComponentWithAnnotation.class);
        assertNotNull(application.createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE));
        assertEquals(UiComponentWithAnnotation.class,
            application.createComponent(UiComponentWithAnnotation.ANNOTATED_COMPONENT_TYPE).getClass());
    }

    // Renderer related methods
    @Test
    @DisplayName("Should register a renderer by family and id")
    void shouldRegisterRendererByFamilyAndId(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, RendererWithAnnotation.COMPONENT_FAMILY,
            RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.COMPONENT_FAMILY, RendererWithAnnotation.RENDERER_TYPE,
            new RendererWithAnnotation());

        final var renderer = facesContext.getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
            RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    @DisplayName("Should register a renderer from its annotation")
    void shouldRegisterRendererByAnnotation(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, RendererWithAnnotation.COMPONENT_FAMILY,
            RendererWithAnnotation.RENDERER_TYPE);
        decorator.registerRenderer(RendererWithAnnotation.class);

        final var renderer = facesContext.getRenderKit().getRenderer(RendererWithAnnotation.COMPONENT_FAMILY,
            RendererWithAnnotation.RENDERER_TYPE);
        assertNotNull(renderer);
        assertEquals(RendererWithAnnotation.class, renderer.getClass());
    }

    @Test
    @DisplayName("Should register a mock renderer")
    void shouldRegisterMockRenderer(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE);
        decorator.registerMockRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE);
        assertNotNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, HtmlForm.COMPONENT_TYPE));
    }

    @Test
    @DisplayName("Should register the CuiMockComponent with its renderer")
    void shouldRegisterCuiMockComponent(ComponentConfigDecorator decorator, Application application,
        FacesContext facesContext) {
        assertUIComponentIsNotRegistered(application, CuiMockComponent.COMPONENT_TYPE);

        assertRendererIsNotRegistered(facesContext, CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE);
        decorator.registerCuiMockComponentWithRenderer();
        assertNotNull(
            facesContext.getRenderKit().getRenderer(CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE));
        assertNotNull(application.createComponent(CuiMockComponent.COMPONENT_TYPE));
        assertEquals(CuiMockComponent.class,
            application.createComponent(CuiMockComponent.COMPONENT_TYPE).getClass());
    }

    @Test
    @DisplayName("Should register a mock renderer for HtmlOutputText")
    void shouldRegisterMockRendererForHtmlOutput(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UIOutput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlOutputText();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIOutput.COMPONENT_FAMILY, TEXT_RENDERER_ID));
    }

    @Test
    @DisplayName("Should register a mock renderer for HtmlInputText")
    void shouldRegisterMockRendererForHtmlInput(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UIInput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        decorator.registerMockRendererForHtmlInputText();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIInput.COMPONENT_FAMILY, TEXT_RENDERER_ID));
    }

    @Test
    @DisplayName("Should register a mock renderer for HtmlForm")
    void shouldRegisterMockRendererForHtmlForm(ComponentConfigDecorator decorator, FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UIForm.COMPONENT_FAMILY, FORM_RENDERER_ID);
        decorator.registerMockRendererForHtmlForm();
        assertNotNull(facesContext.getRenderKit().getRenderer(UIForm.COMPONENT_FAMILY, FORM_RENDERER_ID));
    }

    @Test
    @DisplayName("Should register a mock renderer for HtmlSelectBooleanCheckbox")
    void shouldRegisterMockRendererForHtmlSelectBooleanCheckbox(ComponentConfigDecorator decorator,
        FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UISelectBoolean.COMPONENT_FAMILY,
            ComponentConfigDecorator.SELECT_BOOLEAN_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectBooleanCheckbox();
        assertNotNull(facesContext.getRenderKit().getRenderer(UISelectBoolean.COMPONENT_FAMILY,
            ComponentConfigDecorator.SELECT_BOOLEAN_RENDERER_ID));
    }

    @Test
    @DisplayName("Should register a mock renderer for HtmlSelectOneRadio")
    void shouldRegisterMockRendererForHtmlSelectOneRadio(ComponentConfigDecorator decorator,
        FacesContext facesContext) {
        assertRendererIsNotRegistered(facesContext, UISelectOne.COMPONENT_FAMILY,
            ComponentConfigDecorator.SELECT_ONE_RENDERER_ID);
        decorator.registerMockRendererForHtmlSelectOneRadio();
        assertNotNull(facesContext.getRenderKit().getRenderer(UISelectOne.COMPONENT_FAMILY,
            ComponentConfigDecorator.SELECT_ONE_RENDERER_ID));
    }

    // Converter related methods
    @Test
    @DisplayName("Should register a converter with an explicit id")
    void shouldRegisterConverterWithId(ComponentConfigDecorator decorator, Application application) {
        assertConverterForIdIsNotRegistered(application, ReverseConverter.CONVERTER_ID);
        decorator.registerConverter(ReverseConverter.class, ReverseConverter.CONVERTER_ID);
        assertNotNull(application.createConverter(ReverseConverter.CONVERTER_ID));
        assertEquals(ReverseConverter.class,
            application.createConverter(ReverseConverter.CONVERTER_ID).getClass());
    }

    @Test
    @DisplayName("Should register a converter for a target type")
    void shouldRegisterConverterWithType(ComponentConfigDecorator decorator, Application application) {
        assertConverterForTypeIsNotRegistered(application, Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class, Serializable.class);
        assertNotNull(application.createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
            application.createConverter(Serializable.class).getClass());
    }

    @Test
    @DisplayName("Should reject a converter whose @FacesConverter defines neither value nor forClass (DOC-12)")
    void shouldRejectConverterWithoutIdOrType(ComponentConfigDecorator decorator) {
        assertThrows(IllegalArgumentException.class, () -> decorator.registerConverter(BareConverter.class),
            "A @FacesConverter with neither value nor forClass must be rejected instead of registering nothing");
    }

    @Test
    @DisplayName("Should register a converter from its annotated type")
    void shouldRegisterConverterWithAnnotatetdType(ComponentConfigDecorator decorator, Application application) {
        assertConverterForTypeIsNotRegistered(application, Serializable.class);
        decorator.registerConverter(ConverterWithTypeAnnotation.class);
        assertNotNull(application.createConverter(Serializable.class));
        assertEquals(ConverterWithTypeAnnotation.class,
            application.createConverter(Serializable.class).getClass());
    }

    @Test
    @DisplayName("Should register a converter from its annotated id")
    void shouldRegisterConverterWithAnnotatedId(ComponentConfigDecorator decorator, Application application) {
        assertConverterForIdIsNotRegistered(application, ConverterWithConverterIdAnnotation.CONVERTER_ID);
        decorator.registerConverter(ConverterWithConverterIdAnnotation.class);
        assertNotNull(application.createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID));
        assertEquals(ConverterWithConverterIdAnnotation.class,
            application.createConverter(ConverterWithConverterIdAnnotation.CONVERTER_ID).getClass());
    }

    // Validator related methods
    @Test
    @DisplayName("Should register a validator with an explicit id")
    void shouldRegisterValidatorWithId(ComponentConfigDecorator decorator, Application application) {
        assertValidatorIsNotRegistered(application, LengthValidator.VALIDATOR_ID);
        decorator.registerValidator(LengthValidator.VALIDATOR_ID, LengthValidator.class);
        assertNotNull(application.createValidator(LengthValidator.VALIDATOR_ID));
        assertEquals(LengthValidator.class, application.createValidator(LengthValidator.VALIDATOR_ID).getClass());
    }

    @Test
    @DisplayName("Should register a validator from its annotation")
    void shouldRegisterValidatorWithValidatorAnnotation(ComponentConfigDecorator decorator, Application application) {
        assertValidatorIsNotRegistered(application, ValidatorWithAnnotation.VALIDATOR_ID);
        decorator.registerValidator(ValidatorWithAnnotation.class);
        assertNotNull(application.createValidator(ValidatorWithAnnotation.VALIDATOR_ID));
        assertEquals(ValidatorWithAnnotation.class,
            application.createValidator(ValidatorWithAnnotation.VALIDATOR_ID).getClass());
    }

    @Test
    @DisplayName("Should fail to register a validator that is missing the FacesValidator annotation")
    void shouldFailToRegisterValidatorWithMissingAnnotation(ComponentConfigDecorator decorator,
        Application application) {
        assertValidatorIsNotRegistered(application, LengthValidator.VALIDATOR_ID);
        assertThrows(IllegalArgumentException.class, () -> decorator.registerValidator(LengthValidator.class));
    }

    // Client Behavior related
    @Test
    @DisplayName("Should register a behavior with an explicit id")
    void shouldRegisterBehaviorWithId(ComponentConfigDecorator decorator, Application application) {
        assertBehaviorIsNotRegistered(application, BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.BEHAVIOR_ID, BehaviorWithAnnotation.class);
        final var behavior = application.createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(BehaviorWithAnnotation.class, behavior.getClass());
    }

    @Test
    @DisplayName("Should register a behavior from its annotation")
    void shouldRegisterBehaviorWithAnnotation(ComponentConfigDecorator decorator, Application application) {
        assertBehaviorIsNotRegistered(application, BehaviorWithAnnotation.BEHAVIOR_ID);
        decorator.registerBehavior(BehaviorWithAnnotation.class);
        final var behavior = application.createBehavior(BehaviorWithAnnotation.BEHAVIOR_ID);
        assertNotNull(behavior);
        assertEquals(BehaviorWithAnnotation.class, behavior.getClass());
    }

    @Test
    @DisplayName("Should fail to register a behavior that is missing the FacesBehavior annotation")
    void shouldFailToRegisterBehaviorWithMissingAnnotation(ComponentConfigDecorator decorator,
        Application application) {
        assertBehaviorIsNotRegistered(application, BehaviorWithoutAnnotation.BEHAVIOR_ID);
        assertThrows(IllegalArgumentException.class, () -> decorator.registerBehavior(BehaviorWithoutAnnotation.class));
    }

    @Test
    @DisplayName("Should add a UIComponent to the view root")
    void addComponent(ComponentConfigDecorator decorator, FacesContext facesContext) {
        final UIComponent input = new HtmlInputText();
        decorator.addUiComponent("test:id", input);
        assertEquals(input, facesContext.getViewRoot().findComponent("test:id"));
    }

    private void assertValidatorIsNotRegistered(final Application application, final String validatorId) {
        assertThrows(FacesException.class, () -> application.createValidator(validatorId));
    }

    private void assertBehaviorIsNotRegistered(final Application application, final String behaviorId) {
        assertThrows(FacesException.class, () -> application.createBehavior(behaviorId));
    }

    private void assertConverterForIdIsNotRegistered(final Application application, final String converterId) {
        assertNull(application.createConverter(converterId));
    }

    private void assertConverterForTypeIsNotRegistered(final Application application, final Class<?> target) {
        final Converter<?> converter = application.createConverter(target);
        assertNull(converter, "Converter is registered " + target);
    }

    private void assertUIComponentIsNotRegistered(final Application application, final String componentType) {
        assertThrows(FacesException.class, () -> application.createComponent(componentType));
    }

    private void assertRendererIsNotRegistered(final FacesContext facesContext, final String family,
        final String rendererType) {
        final Renderer renderer = facesContext.getRenderKit().getRenderer(family, rendererType);
        assertNull(renderer, "Renderer is registered " + rendererType + ", " + family);
    }

    /** A converter whose {@code @FacesConverter} defines neither a value nor a specific forClass. */
    @FacesConverter
    public static class BareConverter implements Converter<Object> {
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return value;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return String.valueOf(value);
        }
    }
}
