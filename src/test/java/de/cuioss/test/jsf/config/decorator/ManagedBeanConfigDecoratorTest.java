package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.test.jsf.config.decorator.BeanConfigDecorator.getBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.support.beans.NamedSimpleBean;
import de.cuioss.test.jsf.support.beans.NamedSimpleBeanWOName;
import de.cuioss.test.jsf.util.ConfigurableFacesTest;

class ManagedBeanConfigDecoratorTest extends ConfigurableFacesTest {

    private BeanConfigDecorator decorator;

    @BeforeEach
    void before() {
        decorator = new BeanConfigDecorator(getFacesContext());
    }

    @Test
    void shouldHandleNamedBean() {
        var bean = getBean(NamedSimpleBean.BEAN_NAME, getFacesContext(), NamedSimpleBean.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBean());

        bean = getBean(NamedSimpleBean.BEAN_NAME, getFacesContext(), NamedSimpleBean.class);
        assertNotNull(bean);
        assertEquals(NamedSimpleBean.class, bean.getClass());
    }

    @Test
    void shouldHandleNamedBeanWOValue() {
        var bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, getFacesContext(), NamedSimpleBeanWOName.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBeanWOName());

        bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, getFacesContext(), NamedSimpleBeanWOName.class);
        assertNotNull(bean);
        assertEquals(NamedSimpleBeanWOName.class, bean.getClass());
    }

    @Test
    void shouldFailWithNullAsFacesContext() {
        assertThrows(NullPointerException.class, () -> new BeanConfigDecorator(null));
    }

    @Test
    void shouldHandleMultipleBeans() {
        decorator.register(new NamedSimpleBeanWOName(), NamedSimpleBeanWOName.BEAN_NAME);
        final var bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, getFacesContext(), NamedSimpleBeanWOName.class);
        assertNotNull(bean);
        decorator.register(new NamedSimpleBean(), NamedSimpleBean.BEAN_NAME);
        final var secondBean = getBean(NamedSimpleBean.BEAN_NAME, getFacesContext(), NamedSimpleBean.class);
        assertNotNull(secondBean);
    }
}
