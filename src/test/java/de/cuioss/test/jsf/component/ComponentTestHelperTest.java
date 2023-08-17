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
package de.cuioss.test.jsf.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.support.componentproperty.ComponentWithCollection;
import de.cuioss.test.jsf.support.componentproperty.MultiValuedComponent;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;

@EnableGeneratorController
@EnableGeneratorRegistry
class ComponentTestHelperTest {

    @Test
    void shouldHandleMissingAnnotationGracefully() {
        assertNotNull(ComponentTestHelper.filterPropertyMetadata(getClass(), new MultiValuedComponent()));
    }

    @Test
    void shouldHandleSimpleProperties() {
        var filterPropertyMetadata = ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class,
                new MultiValuedComponent());
        assertNotNull(filterPropertyMetadata);
        assertEquals(3, filterPropertyMetadata.size());
    }

    @Test
    void shouldFailOnCollectionType() {
        var component = new ComponentWithCollection();
        assertThrows(IllegalStateException.class, () -> {
            ComponentTestHelper.filterPropertyMetadata(ComponentWithCollection.class, component);

        });
    }
}
