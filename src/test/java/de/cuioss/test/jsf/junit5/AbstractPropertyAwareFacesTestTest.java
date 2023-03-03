package de.cuioss.test.jsf.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.support.beans.MediumComplexityBean;
import de.cuioss.test.jsf.util.ConfigurableApplication;

class AbstractPropertyAwareFacesTestTest extends AbstractPropertyAwareFacesTest<MediumComplexityBean> {

    @Test
    void shouldScanBean() {
        assertEquals(MediumComplexityBean.class, super.getTargetBeanClass());
        assertEquals(10, super.getPropertyMetadata().size());
    }

    @Test
    void shouldSetupJsfEnvironment() {
        assertNotNull(getFacesContext());
    }

    @Test
    void shouldInheritUseIdentityBundle() {
        assertEquals(ConfigurableApplication.class, getApplication().getClass());
        assertTrue(((ConfigurableApplication) getApplication()).isUseIdentityResouceBundle());
    }
}
