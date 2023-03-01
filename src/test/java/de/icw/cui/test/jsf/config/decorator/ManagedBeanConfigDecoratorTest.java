package de.icw.cui.test.jsf.config.decorator;

import static de.icw.cui.test.jsf.config.decorator.BeanConfigDecorator.getBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.junit.Before;
import org.junit.Test;

import de.icw.cui.test.jsf.support.beans.NamedSimpleBean;
import de.icw.cui.test.jsf.support.beans.NamedSimpleBeanWOName;

@SuppressWarnings("javadoc")
class ManagedBeanConfigDecoratorTest extends AbstractJsfTestCase {

    private BeanConfigDecorator decorator;

    @Before
    public void before() {
        decorator = new BeanConfigDecorator(facesContext);
    }

    @Test
    public void shouldHandleNamedBean() {
        NamedSimpleBean bean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBean());

        bean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNotNull(bean);
        assertEquals(NamedSimpleBean.class, bean.getClass());
    }

    @Test
    public void shouldHandleNamedBeanWOValue() {
        NamedSimpleBeanWOName bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
        assertNull(bean);

        decorator.register(new NamedSimpleBeanWOName());

        bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
        assertNotNull(bean);
        assertEquals(NamedSimpleBeanWOName.class, bean.getClass());
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullAsFacesContext() {
        new BeanConfigDecorator(null);
    }

    @Test
    public void shouldHandleMultipleBeans() {
        decorator.register(new NamedSimpleBeanWOName(), NamedSimpleBeanWOName.BEAN_NAME);
        final NamedSimpleBeanWOName bean = getBean(NamedSimpleBeanWOName.BEAN_NAME, facesContext,
                NamedSimpleBeanWOName.class);
        assertNotNull(bean);
        decorator.register(new NamedSimpleBean(), NamedSimpleBean.BEAN_NAME);
        final NamedSimpleBean secondBean = getBean(NamedSimpleBean.BEAN_NAME, facesContext,
                NamedSimpleBean.class);
        assertNotNull(secondBean);
    }
}
