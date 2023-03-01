package de.icw.cui.test.jsf.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.icw.cui.test.jsf.support.componentproperty.ComponentWithCollection;
import de.icw.cui.test.jsf.support.componentproperty.MultiValuedComponent;
import io.cui.test.generator.junit.EnableGeneratorController;
import io.cui.test.valueobjects.junit5.EnableGeneratorRegistry;

@EnableGeneratorController
@EnableGeneratorRegistry
class ComponentTestHelperTest {

    @Test
    void shouldHandleMissingAnnotationGracefully() {
        assertNotNull(ComponentTestHelper.filterPropertyMetadata(getClass(),
                new MultiValuedComponent()));
    }

    @Test
    void shouldHandleSimpleProperties() {
        var filterPropertyMetadata =
            ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
                    new MultiValuedComponent());
        assertNotNull(filterPropertyMetadata);
        assertEquals(3, filterPropertyMetadata.size());
    }

    @Test
    void shouldFailOnCollectionType() {
        assertThrows(IllegalStateException.class, () -> {
            ComponentTestHelper.filterPropertyMetadata(ComponentWithCollection.class, new ComponentWithCollection());

        });
    }
}
