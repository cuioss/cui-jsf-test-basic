package de.cuioss.test.jsf.config.decorator;

import static de.cuioss.test.jsf.config.decorator.BeanConfigDecorator.getBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.junit.Before;
import org.junit.Test;

import de.cuioss.test.jsf.support.beans.NamedSimpleBean;
import de.cuioss.test.jsf.support.beans.NamedSimpleBeanWOName;

class ManagedBeanConfigDecoratorTest extends AbstractJsfTestCase {

    private BeanConfigDecorator decorator;

    @Before
    void before() {
        decorator = new BeanConfigDecorator(facesContext);
    }

    @Test
    void shouldHandleNamedBean() {
        var bean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBean());

        bean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNotNull(bean);
        assertEquals(NamedSimpleBean.class, bean.getClass());
    }

    @Test
    void shouldHandleNamedBeanWOValue() {
        var bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBeanWOName());

        bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
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
        final var bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
        assertNotNull(bean);
        decorator.register(new NamedSimpleBean(), NamedSimpleBean.BEAN_NAME);
        final var secondBean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNotNull(secondBean);
    }
}
