package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.cuioss.tools.reflect.MoreReflection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class BeanConfigDecorator {

    private static final String NAME_MUST_NOT_BE_NULL = "Name must not be null";
    private static final String BEAN_MUST_NOT_BE_NULL = "Bean must not be null";

    /** Wraps textual names to EL-expressions. */
    @SuppressWarnings("el-syntax")
    public static final String EL_WRAPPER = "#{%s}";

    /** String constant used for checking if given String is EL-Expression. */
    @SuppressWarnings("el-syntax")
    public static final String EL_START = "#{";

    @NonNull
    private final FacesContext facesContext;

    /**
     * Registers a given Object as jsf-managed bean for the given key
     *
     * @param managedBean to be registered, must not be null
     * @param name to be registered to. May be a correct value-expression or not (actual name only).
     *            Must not be null nor empty.
     * @return the {@link BeanConfigDecorator} itself in order to enable a fluent-api style usage
     */
    public BeanConfigDecorator register(final Object managedBean, final String name) {
        requireNonNull(emptyToNull(name), NAME_MUST_NOT_BE_NULL);
        requireNonNull(managedBean, BEAN_MUST_NOT_BE_NULL);

        final var expressionFactory =
            facesContext.getApplication().getExpressionFactory();

        final var ve =
            expressionFactory.createValueExpression(facesContext.getELContext(),
                    checkManagedBeanKey(name), managedBean.getClass());
        ve.setValue(facesContext.getELContext(), managedBean);

        return this;
    }

    /**
     * Registers a given Object as jsf-managed bean. It checks the given bean for the annotation
     * {@link Named} in oder to extract the corresponding value attribute.
     * If none could be found it uses the {@link Class#getSimpleName()} with the first letter being
     * lower-cased
     *
     * @param managedBean to be registered, must not be null
     * @return the {@link BeanConfigDecorator} itself in order to enable a fluent-api style usage
     */
    public BeanConfigDecorator register(final Object managedBean) {
        requireNonNull(managedBean, BEAN_MUST_NOT_BE_NULL);

        final Class<?> type = managedBean.getClass();
        String name = null;

        final Optional<Named> named =
            MoreReflection.extractAnnotation(type, Named.class);
        if (named.isPresent() && !isEmpty(named.get().value())) {
            name = named.get().value();
        }

        if (null == name) {
            final var builder = new StringBuilder();
            builder.append(Character.toLowerCase(type.getSimpleName().charAt(0)));
            builder.append(type.getSimpleName().substring(1));
            name = builder.toString();
        }
        return register(managedBean, name);
    }

    /**
     * In case the beanKey is not an el expression (starting not with '#{') this method wraps the
     * expression accordingly.
     *
     * @param managedBeanKey must not be null or empty
     * @return the key wrapped as an EL-Expression if needed, otherwise the given key.
     */
    public static String checkManagedBeanKey(final String managedBeanKey) {
        requireNonNull(emptyToNull(managedBeanKey), NAME_MUST_NOT_BE_NULL);
        var elKey = managedBeanKey;
        if (!elKey.startsWith(EL_START)) {
            elKey = String.format(EL_WRAPPER, elKey);
        }
        return elKey;
    }

    /**
     * Utility method for accessing a concrete bean
     *
     * @param name May be a correct value-expression or not (actual name only).
     *            Must not be null nor empty.
     * @param facesContext to be used for accessing the bean
     * @param expectedType identifying the type to be checked
     * @return the registered bean for a given type or null, if none could be found
     */
    public static <T> T getBean(final String name, final FacesContext facesContext,
            final Class<T> expectedType) {
        requireNonNull(facesContext);
        requireNonNull(name);
        requireNonNull(expectedType);
        return facesContext.getApplication().evaluateExpressionGet(facesContext,
                checkManagedBeanKey(name),
                expectedType);
    }
}
