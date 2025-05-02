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
package de.cuioss.test.jsf.config.decorator;

import de.cuioss.test.jsf.mocks.CuiMockComponent;
import de.cuioss.test.jsf.mocks.CuiMockRenderer;
import de.cuioss.test.jsf.mocks.CuiMockUIViewRoot;
import de.cuioss.test.jsf.mocks.CuiMockViewHandler;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import jakarta.faces.application.Application;
import jakarta.faces.component.*;
import jakarta.faces.component.behavior.ClientBehavior;
import jakarta.faces.component.behavior.FacesBehavior;
import jakarta.faces.component.html.*;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static de.cuioss.tools.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Helper class acting as runtime-registry for {@link UIComponent},
 * {@link Converter}, {@link Validator} and {@link Renderer}
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ComponentConfigDecorator {

    static final String FORM_RENDERER_ID = "jakarta.faces.Form";
    static final String TEXT_RENDERER_ID = "jakarta.faces.Text";
    static final String SELECT_BOOLEAN_RENDERER_ID = "jakarta.faces.SelectBoolean";
    static final String SELECT_ONE_RENDERER_ID = "jakarta.faces.SelectOne";
    private static final String BEHAVIOR_CLASS_MUST_NOT_BE_NULL = "behaviorClass must not be null";
    private static final String BEHAVIOR_ID_MUST_NOT_BE_NULL = "behaviorId must not be null";
    private static final String RENDERER_MUST_NOT_BE_NULL = "renderer must not be null";
    private static final String RENDERER_TYPE_MUST_NOT_BE_NULL = "rendererType must not be null";
    private static final String FAMILY_MUST_NOT_BE_NULL = "family must not be null";
    private static final String VALIDATOR_ID_MUST_NOT_BE_NULL = "validatorId must not be null";
    private static final String CONVERTER_ID_MUST_NOT_BE_NULL = "converterId must not be null";
    private static final String TARGET_CLASS_MUST_NOT_BE_NULL = "targetClass  must not be null";
    private static final String VALIDATOR_MUST_NOT_BE_NULL = "validator must not be null";
    private static final String COMPONENT_MUST_NOT_BE_NULL = "component must not be null";
    private static final String COMPONENT_TYPE_NOT_BE_NULL = "component must not be null";
    private static final String CONVERTER_MUST_NOT_BE_NULL = "converter  must not be null";

    @NonNull
    private final Application application;

    @NonNull
    private final FacesContext facesContext;

    /**
     * Adds add {@link Validator} to the given id.
     *
     * @param validatorId the id the {@link Validator} should be registered with,
     *                    must not be null
     * @param validator   the actual {@link Validator} class, must not be null.
     * @return the {@link ComponentConfigDecorator} itself in order to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerValidator(final String validatorId,
        final Class<? extends Validator> validator) {
        requireNonNull(validator, VALIDATOR_MUST_NOT_BE_NULL);
        requireNonNull(validatorId, VALIDATOR_ID_MUST_NOT_BE_NULL);
        application.addValidator(validatorId, validator.getName());
        return this;
    }

    /**
     * Adds add {@link Validator} the needed validatorId is retrieved from
     * {@link FacesValidator}.
     *
     * @param validator the actual {@link Validator} class, must not be null.
     *                  To work the {@link Validator} must provide the
     *                  {@link FacesValidator} annotation to identify
     *                  the needed validatorId
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerValidator(final Class<? extends Validator> validator) {
        requireNonNull(validator, VALIDATOR_MUST_NOT_BE_NULL);
        checkArgument(validator.isAnnotationPresent(FacesValidator.class),
            "In order to work this method needs a validator annotated with 'jakarta.faces.validator.FacesValidator', validatorClass:"
                + validator.getName());
        return registerValidator(validator.getAnnotation(FacesValidator.class).value(), validator);
    }

    /**
     * Register a {@link Converter} for a given target class.
     *
     * @param converter   to be registered must not be null
     * @param targetClass must not be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerConverter(final Class<? extends Converter> converter,
        final Class<?> targetClass) {
        requireNonNull(targetClass, TARGET_CLASS_MUST_NOT_BE_NULL);
        requireNonNull(converter, CONVERTER_MUST_NOT_BE_NULL);
        application.addConverter(targetClass, converter.getName());
        return this;
    }

    /**
     * Register a {@link Converter} for a given target class.
     *
     * @param converter to be registered, must not be null and must provide a
     *                  {@link FacesConverter} annotation for deriving targetType or
     *                  converter-id
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerConverter(final Class<? extends Converter> converter) {
        requireNonNull(converter, CONVERTER_MUST_NOT_BE_NULL);
        checkArgument(converter.isAnnotationPresent(FacesConverter.class),
            "In order to work this method needs a converter annotated with 'jakarta.faces.convert.FacesConverter', converterClass:"
                + converter.getName());
        final var facesConverter = converter.getAnnotation(FacesConverter.class);
        if (!Object.class.equals(facesConverter.forClass())) {
            registerConverter(converter, facesConverter.forClass());
        }
        if (!facesConverter.value().isEmpty()) {
            registerConverter(converter, facesConverter.value());
        }
        return this;
    }

    /**
     * Register a {@link Converter} to a given converterId.
     *
     * @param converter   to be registered must not be null
     * @param converterId must not be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerConverter(final Class<? extends Converter> converter,
        final String converterId) {
        requireNonNull(converterId, CONVERTER_ID_MUST_NOT_BE_NULL);
        requireNonNull(converter, CONVERTER_MUST_NOT_BE_NULL);
        application.addConverter(converterId, converter.getName());
        return this;
    }

    /**
     * Registers a {@link UIComponent}
     *
     * @param componentType identifying the component must not be null
     * @param component     the actual component must not be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerUIComponent(final String componentType,
        final Class<? extends UIComponent> component) {
        requireNonNull(componentType, COMPONENT_TYPE_NOT_BE_NULL);
        requireNonNull(component, COMPONENT_MUST_NOT_BE_NULL);
        application.addComponent(componentType, component.getName());
        return this;
    }

    /**
     * Registers a {@link UIComponent}
     *
     * @param component the actual component must not be null must provide the
     *                  {@link FacesComponent} annotation to derive the componentId
     *                  to be registered to.
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerUIComponent(final Class<? extends UIComponent> component) {
        requireNonNull(component, COMPONENT_MUST_NOT_BE_NULL);
        checkArgument(component.isAnnotationPresent(FacesComponent.class),
            "In order to work this method needs a UIComponent annotated with 'jakarta.faces.component.FacesComponent', component:"
                + component.getName());
        return registerUIComponent(component.getAnnotation(FacesComponent.class).value(), component);
    }

    /**
     * Shorthand for registering a {@link CuiMockComponent} with a {@link CuiMockRenderer}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerCuiMockComponentWithRenderer() {
        registerUIComponent(CuiMockComponent.class);
        registerMockRenderer(CuiMockComponent.FAMILY, CuiMockComponent.RENDERER_TYPE);
        return this;
    }

    /**
     * Registers a {@link CuiMockRenderer} for the given attributes.
     *
     * @param family       identifying the component-family the renderer is related
     *                     to, must not be null
     * @param rendererType identifying the type of the renderer is related to, must
     *                     not be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRenderer(final String family, final String rendererType) {
        requireNonNull(family, FAMILY_MUST_NOT_BE_NULL);
        requireNonNull(rendererType, RENDERER_TYPE_MUST_NOT_BE_NULL);
        facesContext.getRenderKit().addRenderer(family, rendererType, new CuiMockRenderer());
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for
     * {@link HtmlOutputText}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForHtmlOutputText() {
        registerMockRenderer(UIOutput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for
     * {@link HtmlSelectBooleanCheckbox}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForHtmlSelectBooleanCheckbox() {
        registerMockRenderer(UISelectBoolean.COMPONENT_FAMILY, SELECT_BOOLEAN_RENDERER_ID);
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for
     * {@link HtmlSelectOneRadio}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForHtmlSelectOneRadio() {
        registerMockRenderer(UISelectOne.COMPONENT_FAMILY, SELECT_ONE_RENDERER_ID);
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for
     * {@link HtmlInputText}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForHtmlInputText() {
        registerMockRenderer(UIInput.COMPONENT_FAMILY, TEXT_RENDERER_ID);
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for {@link HtmlForm}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForHtmlForm() {
        registerMockRenderer(UIForm.COMPONENT_FAMILY, FORM_RENDERER_ID);
        return this;
    }

    /**
     * Shorthand for registering a {@link CuiMockRenderer} for
     * {@link HtmlCommandButton}.
     *
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerMockRendererForCommandButton() {
        facesContext.getRenderKit().addRenderer("jakarta.faces.Command", "jakarta.faces.Button",
            new CuiMockRenderer("CommandButton"));
        return this;
    }

    /**
     * Registers a {@link Renderer}
     *
     * @param family       identifying the component-family the renderer is related
     *                     to, must not be null
     * @param rendererType identifying the type of the renderer is related to, must
     *                     not be null
     * @param renderer     the actual renderer to be registered, must not be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerRenderer(final String family, final String rendererType,
        final Renderer<?> renderer) {
        requireNonNull(family, FAMILY_MUST_NOT_BE_NULL);
        requireNonNull(rendererType, RENDERER_TYPE_MUST_NOT_BE_NULL);
        requireNonNull(renderer, RENDERER_MUST_NOT_BE_NULL);
        facesContext.getRenderKit().addRenderer(family, rendererType, renderer);
        return this;
    }

    /**
     * Registers a {@link Renderer} The family and rendererType will be derived by
     * the mandatory {@link FacesRenderer} annotation
     *
     * @param renderer the actual renderer to be registered, must not be null, must
     *                 provide {@link FacesRenderer} annotation and a no arg public
     *                 constructor.
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerRenderer(final Class<? extends Renderer> renderer) {
        requireNonNull(renderer, RENDERER_MUST_NOT_BE_NULL);
        checkArgument(renderer.isAnnotationPresent(FacesRenderer.class),
            "In order to work this method needs a Renderer annotated with 'jakarta.faces.render.FacesRenderer', renderer:"
                + renderer.getName());
        final Renderer<?> instance;
        instance = new DefaultInstantiator<>(renderer).newInstance();
        final var config = renderer.getAnnotation(FacesRenderer.class);
        //noinspection DataFlowIssue: owolff: The annotation is checked before
        return registerRenderer(config.componentFamily(), config.rendererType(), instance);
    }

    /**
     * Register a {@link ClientBehavior} for a given behaviorId
     *
     * @param behaviorId    the id the {@link ClientBehavior} should be registered
     *                      with, must not be null
     * @param behaviorClass the actual type of the {@link ClientBehavior} must not
     *                      be null
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerBehavior(final String behaviorId,
        final Class<? extends ClientBehavior> behaviorClass) {
        requireNonNull(behaviorId, BEHAVIOR_ID_MUST_NOT_BE_NULL);
        requireNonNull(behaviorClass, BEHAVIOR_CLASS_MUST_NOT_BE_NULL);
        application.addBehavior(behaviorId, behaviorClass.getName());
        return this;
    }

    /**
     * Register a {@link ClientBehavior}. The behaviorId will be extracted by the
     * mandatory {@link FacesBehavior} annotation
     *
     * @param behaviorClass the actual type of the {@link ClientBehavior} must not
     *                      be null and provide the {@link FacesBehavior} annotation
     *                      to extract the correct behaviorId
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerBehavior(final Class<? extends ClientBehavior> behaviorClass) {
        requireNonNull(behaviorClass, BEHAVIOR_CLASS_MUST_NOT_BE_NULL);
        checkArgument(behaviorClass.isAnnotationPresent(FacesBehavior.class),
            "In order to work this method needs a ClientBehavior annotated with 'jakarta.faces.component.behavior.FacesBehavior', behaviorClass:"
                + behaviorClass.getName());
        return registerBehavior(behaviorClass.getAnnotation(FacesBehavior.class).value(), behaviorClass);
    }

    /**
     * Register a composite component to be rendered.
     *
     * @param libraryName the library name like
     *                    "http://xmlns.jcp.org/jsf/composite/" or "
     * @param tagName     the tag name
     * @param uiComponent the component that should be returned as mock /
     *                    placeholder for the composite component
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator registerCompositeComponent(final String libraryName, final String tagName,
        final UIComponent uiComponent) {
        if (!(application.getViewHandler() instanceof CuiMockViewHandler)) {
            application.setViewHandler(new CuiMockViewHandler());
        }
        ((CuiMockViewHandler) application.getViewHandler()).registerCompositeComponent(libraryName, tagName,
            uiComponent);
        return this;
    }

    /**
     * Add a component to the view root to be found when searching via
     * {@link UIViewRoot#findComponent(String)}.
     *
     * @param expr      the expression the component should be found with
     * @param component the component
     * @return the {@link ComponentConfigDecorator} itself to enable a
     * fluent-api style usage
     */
    public ComponentConfigDecorator addUiComponent(String expr, UIComponent component) {
        if (!(facesContext.getViewRoot() instanceof CuiMockUIViewRoot)) {
            facesContext.setViewRoot(new CuiMockUIViewRoot());
        }
        ((CuiMockUIViewRoot) facesContext.getViewRoot()).addUiComponent(expr, component);
        return this;
    }

}
