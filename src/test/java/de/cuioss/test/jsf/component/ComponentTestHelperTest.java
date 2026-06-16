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
package de.cuioss.test.jsf.component;

import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.support.componentproperty.ComponentWithCollection;
import de.cuioss.test.jsf.support.componentproperty.MultiValuedComponent;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnableGeneratorController
@EnableGeneratorRegistry
@DisplayName("ComponentTestHelper")
class ComponentTestHelperTest {

    @Test
    @DisplayName("Should return metadata when the requesting class carries no property annotation")
    void shouldHandleMissingAnnotationGracefully() {
        var component = new MultiValuedComponent();

        var metadata = ComponentTestHelper.filterPropertyMetadata(getClass(), component);

        assertNotNull(metadata, "Metadata should be resolved even without a property annotation");
    }

    @Test
    @DisplayName("Should resolve the simple properties declared by the component")
    void shouldHandleSimpleProperties() {
        var component = new MultiValuedComponent();

        var metadata = ComponentTestHelper.filterPropertyMetadata(MultiValuedComponent.class, component);

        assertNotNull(metadata, "Metadata should be resolved for a component with simple properties");
        assertEquals(3, metadata.size(), "All three simple properties should be resolved");
    }

    @Test
    @DisplayName("Should fail when the component exposes a collection-typed property")
    void shouldFailOnCollectionType() {
        var component = new ComponentWithCollection();

        assertThrows(IllegalStateException.class,
            () -> ComponentTestHelper.filterPropertyMetadata(ComponentWithCollection.class, component),
            "Resolving metadata for a collection-typed property should fail");
    }
}
